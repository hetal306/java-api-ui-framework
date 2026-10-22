package com.portfolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage{

    private static final By CONFIRMATION = By.cssSelector("[data-test='complete-header']");

    protected CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public String getConfirmationMessage() {
        return getText(CONFIRMATION);
    }
}
