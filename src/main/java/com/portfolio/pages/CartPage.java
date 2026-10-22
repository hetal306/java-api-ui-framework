package com.portfolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CartPage extends BasePage {
    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By CHECKOUT_BUTTON = By.cssSelector("[data-test='checkout']");
    private static final By REMOVE_ITEM = By.cssSelector("[data-test='remove-");

    protected CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getItemNames() {
        return getTexts(ITEM_NAMES);
    }

    public CartPage removeItem(String productName) {
        click(By.cssSelector("[data-test='remove-" + productName.toLowerCase().replace(' ', '-') + "']"));
        return this;
    }

    public  CheckoutInfoPage proceedToCheckout(){
        click(CHECKOUT_BUTTON);
        return new CheckoutInfoPage(driver);
    }
}
