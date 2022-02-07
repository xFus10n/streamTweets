package com.xfus10n.twitter.propertiesReader;

import java.io.*;
import java.util.Properties;

public class Reader {
    public Reader() {
    }

    public static Properties readProperties(String path) {
        Properties prop = new Properties();
        try (InputStream output = new FileInputStream(path)) {
            prop.load(output);
        } catch (IOException io) {
            io.printStackTrace();
        }
        return prop;
    }
}
