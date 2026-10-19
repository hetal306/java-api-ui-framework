package com.portfolio.config;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ConfigReaderTest {

    @Test
    public void readsDefaultValueFromPropertiesFile(){
        Assert.assertEquals(ConfigReader.get("ui.base.url"), "https://www.saucedemo.com");
    }

    @Test
    public void convertsTypes() {
        Assert.assertEquals(ConfigReader.getInt("explicit.wait.seconds"), 10);
        Assert.assertFalse(ConfigReader.getBoolean("headless"));
    }

    @Test
    public void systemPropertyOverridesFile() {
        System.setProperty("explicit.wait.seconds", "25");
        try {
            Assert.assertEquals(ConfigReader.getInt("explicit.wait.seconds"), 25);
        } finally {
            System.clearProperty("explicit.wait.seconds");
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void missingKeyFailsFast() {
        ConfigReader.get("does.not.exist");
    }
}
