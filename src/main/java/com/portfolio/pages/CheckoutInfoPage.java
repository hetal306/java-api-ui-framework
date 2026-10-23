package com.portfolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutInfoPage extends BasePage {

    private static final By FIRST_NAME = By.cssSelector("[data-test='firstName']");
    private static final By LAST_NAME = By.cssSelector("[data-test='lastName']");
    private static final By POSTAL_CODE = By.cssSelector("[data-test='postalCode']");
    private static final By CONTINUE_BUTTON = By.cssSelector("[data-test='continue']");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    protected CheckoutInfoPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.urlContains("checkout-step-one"));
    }

    public CheckoutOverviewPage continueWith(String firstName, String lastName, String postalCode) {
        fillDetails(firstName, lastName, postalCode);
        return new CheckoutOverviewPage(driver);
    }

    public CheckoutInfoPage attemptContinue(String firstName, String lastName, String postalCode) {
        fillDetails(firstName, lastName, postalCode);
        return this;
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    private void fillDetails(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);
        click(CONTINUE_BUTTON);
    }
}
