package com.portfolio.driver;

import com.portfolio.config.ConfigReader;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void initDriver() {
        String browser = ConfigReader.get("browser").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");
        String execution = ConfigReader.get("execution").toLowerCase();

        MutableCapabilities options = buildOptions(browser, headless);

        WebDriver driver = switch (execution) {
            case "grid" -> createRemoteDriver(options);
            case "local" -> createLocalDriver(browser, options);
            default -> throw new IllegalArgumentException("unknown execution mode" + execution);
        };
        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver not initialised. call init first");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }

    private static MutableCapabilities buildOptions(String browser, boolean headless) {
        return switch (browser) {
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new", "--windows-size=1920,1080");
                }
                yield options;
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless", "--width=1920", "--height=1080");
                }
                yield options;
            }
            default -> throw new IllegalArgumentException("unsupported browser");
        };
    }

    private static WebDriver createLocalDriver(String browser, MutableCapabilities options) {
        return switch (browser) {
            case "chrome" -> new ChromeDriver((ChromeOptions) options);
            case "firefox" -> new FirefoxDriver((FirefoxOptions) options);
            default -> throw new IllegalArgumentException("unsupported browser");
        };
    }

    private static WebDriver createRemoteDriver(MutableCapabilities options) {
        try {
            return new RemoteWebDriver(URI.create(ConfigReader.get("grid.url")).toURL(), options);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid grid.url ", e);
        }
    }
}

