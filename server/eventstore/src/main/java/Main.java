import common.Functions;
import common.StaticRef;
import org.slf4j.LoggerFactory;
import orientdb.ODBEventStore;
import rabbitmq.RabbitMQEventStore;
import java.util.Properties;
import static common.Properties.loadProperties;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello");

        Functions.logger = LoggerFactory.getLogger(Main.class);

        Properties prop = loadProperties("/opt/event-source/config.properties");
        String rabbit_host = "localhost";
        String orientdb_home = "./.tmp/orientdb/eventstore";

        if (prop != null) {
            rabbit_host = prop.getProperty("rabbitmq_host");
            orientdb_home = prop.getProperty("orientdb_home");
        }

        // odbManager starts rabbitMQ after it loads

        StaticRef.odbManager = new ODBEventStore();
        StaticRef.odbManager.odbHome = orientdb_home;
        StaticRef.odbManager.rabbit_host = rabbit_host;
        StaticRef.odbManager.startAsync();

    }

}
