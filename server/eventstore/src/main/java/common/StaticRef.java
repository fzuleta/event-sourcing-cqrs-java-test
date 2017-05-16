package common;

import orientdb.ODBEventStore;
import rabbitmq.RabbitMQEventStore;

public class StaticRef {
    public static RabbitMQEventStore rabbitMQ;
    public static ODBEventStore odbManager;
}
