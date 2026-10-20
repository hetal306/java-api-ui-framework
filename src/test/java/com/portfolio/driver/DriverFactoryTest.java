package com.portfolio.driver;

import com.portfolio.config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DriverFactoryTest {

    @BeforeMethod
    public void setUp() {
        DriverFactory.initDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    @Test
    public void openSauceDemoHomePage() throws InterruptedException {
        DriverFactory.getDriver().get(ConfigReader.get("ui.base.url"));
        //adding sleep to check browser visibility with headless false mode
        Thread.sleep(5000);
        Assert.assertEquals(DriverFactory.getDriver().getTitle(), "Swag Labs");
    }
}
