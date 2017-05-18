import common.Functions;
import common.StaticRef;
import org.slf4j.LoggerFactory;
import orientdb.ODBEventStore;
import rabbitmq.RabbitMQEventStore;

import java.io.File;
import java.util.Properties;

import static common.Functions.trace;
import static common.Properties.loadProperties;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Functions.logger = LoggerFactory.getLogger(Main.class);
        String parentFolder         =  new File("").getAbsoluteFile().getParent();

        // these settings are to run from within IntelliJ
        String rabbit_host          = "localhost";
        String orientdb_home        = "./.tmp/orientdb/eventstore";
        String odbPathToCreate      = parentFolder + "/.docker/eventstore/osql/create_eventstore.osql";

        // docker will have this properties file copied over
        Properties prop             = loadProperties("/opt/event-source/config.properties");
        if (prop != null) {
            rabbit_host             = prop.getProperty("rabbitmq_host");
            orientdb_home           = prop.getProperty("odb_eventsource_home");
            odbPathToCreate         = prop.getProperty("odb_eventsource_createdb");
        }

        // NOTE: odbManager starts rabbitMQ after it loads
        StaticRef.odbManager                    = new ODBEventStore();
        StaticRef.odbManager.odbHome            = orientdb_home;
        StaticRef.odbManager.odbPathToCreate    = odbPathToCreate;
        StaticRef.odbManager.rabbit_host        = rabbit_host;
        StaticRef.odbManager.startAsync();

    }

}
