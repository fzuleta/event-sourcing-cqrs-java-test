import jetty.Jetty;
import org.apache.commons.io.FileUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static common.Properties.loadProperties;


public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello");

        Properties prop = loadProperties("/opt/event-source/config.properties");
        Jetty.staticFolderLocation = "/Users/gg/Desktop/event-source/client/dice";

        if (prop != null) {
            Jetty.staticFolderLocation = prop.getProperty("webserver_staticfiles");
        }

        Jetty jetty = new Jetty();
        jetty.startAsync();
    }
}
