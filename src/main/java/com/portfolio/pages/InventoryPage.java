package com.portfolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class InventoryPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector("[data-test='title']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");

    protected InventoryPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.urlContains("inventory.html"));
    }

    private static String plugPartialProduct(String productName) {
        return productName.toLowerCase().replace(' ', '-');
    }

    public String getTitle() {
        return getText(PAGE_TITLE);
    }

    public InventoryPage addToCart(String productName) {
        click(By.cssSelector("[data-test='add-to-cart-" + plugPartialProduct(productName) + "']"));
        return this;
    }

    public int getCartCount() {
        var badges = driver.findElements(CART_BADGE);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
    }

    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }
}
