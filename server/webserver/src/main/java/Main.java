import jetty.Jetty;
import org.apache.commons.io.FileUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello");

        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("/opt/event-source/config.properties");
            prop.load(input);

            Jetty.staticFolderLocation = prop.getProperty("webserver_staticfiles");


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Jetty jetty = new Jetty();
        jetty.startAsync();
    }
}
