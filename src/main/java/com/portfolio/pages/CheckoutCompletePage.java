package com.portfolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutCompletePage extends BasePage {

    private static final By CONFIRMATION = By.cssSelector("[data-test='complete-header']");

    protected CheckoutCompletePage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.urlContains("checkout-complete"));
    }

    public String getConfirmationMessage() {
        return getText(CONFIRMATION);
    }
}
