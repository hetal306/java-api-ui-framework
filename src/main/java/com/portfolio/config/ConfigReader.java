package com.portfolio.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private  static final Properties PROPERTIES = load();

    private ConfigReader(){

    }

    private static Properties load()
    {
        Properties props = new Properties();
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)){
            if (in == null) {
                throw new IllegalStateException(CONFIG_FILE + "not found in classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("could not read file " + CONFIG_FILE, e);
        }
        return props;
    }

    public static String get(String key){
        String value = System.getProperty(key);
        if (isBlank(value)) {
            value = System.getenv(key.toLowerCase().replace('.','_'));
        }
        if (isBlank(value)) {
            value = PROPERTIES.getProperty(key);
        }
        if (isBlank(value)){
            throw new IllegalArgumentException("Missing configuration key :" + key);
        }
        return  value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
