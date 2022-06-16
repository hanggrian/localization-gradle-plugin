package com.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LocalizationTest {
    @Test
    public void in() {
        Properties properties = load("strings_in");
        assertEquals("Tentang", properties.getProperty("about"));
        assertEquals("Beranda", properties.getProperty("home"));
    }

    @Test
    public void en() {
        Properties properties = load("strings");
        assertEquals("About", properties.getProperty("about"));
        assertEquals("Home", properties.getProperty("home"));
    }

    @Test
    public void en_US() {
        Properties properties = load("strings_en_US");
        assertEquals("About2", properties.getProperty("about"));
        assertEquals("Home2", properties.getProperty("home"));
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
