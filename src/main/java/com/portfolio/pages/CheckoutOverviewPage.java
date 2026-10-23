package com.portfolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutOverviewPage extends BasePage {

    private static final By SUBTOTAL = By.cssSelector("[data-test='subtotal-label']");
    private static final By FINISH_BUTTON = By.cssSelector("[data-test='finish']");

    protected CheckoutOverviewPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.urlContains("checkout-step-two"));
    }

    public String getSubtotal() {
        return getText(SUBTOTAL);
    }

    public CheckoutCompletePage finish() {
        click(FINISH_BUTTON);
        return new CheckoutCompletePage(driver);
    }
}
