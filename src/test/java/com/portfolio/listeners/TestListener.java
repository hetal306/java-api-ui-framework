package com.portfolio.listeners;

import com.portfolio.driver.DriverFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("START : " + name(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("PASS : " + name(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("FAIL : " + name(result) + "-> " + result.getThrowable());
        takeScreenshot();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("SKIP : " + name(result));
    }

    public void takeScreenshot() {
        if (!DriverFactory.hasDriver()) {
            return;
        }
        try {
            byte[] image = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot on failure", "image/png", new ByteArrayInputStream(image), "png");

        } catch (Exception e) {
            System.out.println("could not take screenshot");
        }
    }

    private String name(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
    }
}
