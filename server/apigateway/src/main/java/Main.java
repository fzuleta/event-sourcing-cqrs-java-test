import common.Functions;
import common.StaticReferences;
import jetty.Jetty;
import org.slf4j.LoggerFactory;
import rabbitmq.RabbitMQ;
import java.util.Properties;
import static common.Properties.loadProperties;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello");

        Functions.logger = LoggerFactory.getLogger(Main.class);

        Properties prop = loadProperties("/opt/event-source/config.properties");
        String rabbit_host = "localhost";
        if (prop != null) {
            rabbit_host = prop.getProperty("rabbitmq_host");
        }

        StaticReferences.rabbitMQ = new RabbitMQ();
        StaticReferences.rabbitMQ.host = rabbit_host;
        StaticReferences.rabbitMQ.startAsync();

        Jetty jetty = new Jetty();
        jetty.startAsync();
    }
}
