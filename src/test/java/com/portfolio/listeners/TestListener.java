package com.portfolio.listeners;

import com.portfolio.db.ResultsRepository;
import com.portfolio.driver.DriverFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ResultsRepository.reset();   // fresh table for every run
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("START : " + name(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("PASS : " + name(result));
        ResultsRepository.save(className(result), result.getMethod().getMethodName(), "PASS", duration(result));

    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("FAIL : " + name(result) + "-> " + result.getThrowable());
        takeScreenshot();
        ResultsRepository.save(className(result), result.getMethod().getMethodName(), "FAIL", duration(result));

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("SKIP : " + name(result));
        ResultsRepository.save(className(result), result.getMethod().getMethodName(), "SKIP", duration(result));

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

    private String className(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName();
    }

    private long duration(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}
