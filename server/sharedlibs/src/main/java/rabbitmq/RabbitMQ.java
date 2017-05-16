package rabbitmq;


import com.google.common.util.concurrent.AbstractService;
import com.rabbitmq.client.*;
import common.Functions;
import common.StaticReferences;
import jetty.EndPointReply;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

import static common.Functions.trace;

public class RabbitMQ extends AbstractService {
    public String host = "localhost";
    public String user = "root";
    public String pass = "root";
    public int qos = 1;
    Connection connection = null;
    Channel channel = null;

    // Exchange types
    private final String FANOUT = "fanout";
    private final String DIRECT = "direct";
    private final String HEADERS = "headers";
    private final String TOPIC = "topic";

    @Override
    protected void doStart() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(user);
        factory.setPassword(pass);
        try {
            connection = factory.newConnection();
            channel = createChannel(connection, qos);

            doInit();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void doInit() throws Exception {

    }
    @Override
    protected void doStop() {
        try {
            if (channel != null) channel.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AMQP.Exchange.DeclareOk createExchange(Channel channel, String exchangeName, String type) throws Exception {
        AMQP.Exchange.DeclareOk result = channel.exchangeDeclare(exchangeName, type);
        System.out.println("Declared Exchange " + exchangeName);
        return result;
    }

    public AMQP.Queue.DeclareOk createQueue(Channel channel, String queueName, Boolean durable, Boolean exclusive, Boolean autoDelete) throws Exception {
        AMQP.Queue.DeclareOk result = channel.queueDeclare(queueName, durable, exclusive, autoDelete, null);
        System.out.println("Declared Queue " + queueName);
        return result;
    }

    public String getRandQueue() throws IOException {
        return channel.queueDeclare().getQueue();
    }

    public void queueBind(Channel channel, String queueName, String exchangeName, String routingKey) throws Exception{
        channel.queueBind(queueName, exchangeName, routingKey);
    }

    public Channel createChannel(Connection connection, Integer qos) throws Exception {
        Channel channel = connection.createChannel();
        // qos = messages that can be sent to a server at a time
        if (qos != null) {
            channel.basicQos(qos);
        }
        return channel;
    }

    public void basicPublish(String exchange, String queueName, AMQP.BasicProperties properties, String message) throws Exception {
        channel.basicPublish(exchange, queueName, properties, message.getBytes());
    }

    public String call(String requestQueue, String message) throws IOException, InterruptedException {
        // correlationId
        String corrId = UUID.randomUUID().toString();

        // make a random queue so the user can understand where it's coming from
        String replyQueueName = getRandQueue();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueue, props, message.getBytes("UTF-8"));

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body, "UTF-8"));
                }

                // delete the queue once the message is consumed
                channel.queueDelete(replyQueueName);
            }
        });

        return response.take();
    }

    protected void createRPCConsumer(String queue, Function<String, EndPointReply> fcn){

        // create the consumer
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(properties.getCorrelationId())
                        .build();
                EndPointReply endPointReply = new EndPointReply();
                try {
                    String message = new String(body,"UTF-8");
                    endPointReply = fcn.apply(message);

                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    try {
                        channel.basicPublish("", properties.getReplyTo(), replyProps, endPointReply.toString().getBytes("UTF-8"));
                    } catch(Exception e) {
                        Functions.logger.info("RabbitMQ.java > createRPCConsumer > properties.routingKey is null");
                    }
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        try {
            channel.basicConsume(queue, false, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
