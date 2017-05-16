package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static common.Functions.trace;

public class Properties {
    public static java.util.Properties loadProperties(String fileLocation) {
        java.util.Properties prop = null;
        InputStream input = null;

        try {
            input = new FileInputStream(fileLocation);
            prop = new java.util.Properties();
            prop.load(input);

        } catch (IOException ex) {
            trace("can't load properties file");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                trace("Properties loaded");
            }
        }
        return prop;
    }
}
