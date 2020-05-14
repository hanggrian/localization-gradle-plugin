package com.example;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class LocalizationTest {

    @Test public void in() {
        Properties properties = load("my_strings");
        assertEquals("Tentang", properties.getProperty("about"));
        assertEquals("Beranda", properties.getProperty("home"));
    }

    @Test public void en() {
        Properties properties = load("my_strings_en");
        assertEquals("About", properties.getProperty("about"));
        assertEquals("Home", properties.getProperty("home"));
    }

    private Properties load(String name) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(name + ".properties");
        Properties properties = new Properties();
        try {
            properties.load(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
