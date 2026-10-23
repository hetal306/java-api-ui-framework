package com.portfolio.ui;

import com.portfolio.base.BaseUiTest;
import com.portfolio.config.ConfigReader;
import com.portfolio.driver.DriverFactory;
import com.portfolio.pages.CartPage;
import com.portfolio.pages.CheckoutInfoPage;
import com.portfolio.pages.InventoryPage;
import com.portfolio.pages.LoginPage;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

@Feature("Purchase flow")
public class PurchaseFlowTest extends BaseUiTest {

    private InventoryPage loginAndOpenInventory() {
        return new LoginPage(DriverFactory.getDriver())
                .open()
                .loginAs(ConfigReader.get("ui.username"), ConfigReader.get("ui.password"));
    }

    @Test
    public void addingItemUpdatesCartBadge() {
        InventoryPage inventory = loginAndOpenInventory()
                .addToCart("Sauce Labs Backpack")
                .addToCart("Sauce Labs Bike Light");
        Assert.assertEquals(inventory.getCartCount(), 2);
    }

    @Test
    public void removingItemFromCart() {
        CartPage cartPage = loginAndOpenInventory()
                .addToCart("Sauce Labs Backpack")
                .addToCart("Sauce Labs Bike Light")
                .openCart()
                .removeItem("Sauce Labs Bike Light");

        Assert.assertEquals(cartPage.getItemNames(), Arrays.asList("Sauce Labs Backpack"));
    }

    @DataProvider(name = "orders")
    public Object[][] orders() {
        return new Object[][] {
                {new String[] {"Sauce Labs Backpack"}, "$29.99"},
                {new String[] {"Sauce Labs Backpack", "Sauce Labs Bike Light"}, "$39.98"},
                {new String[] {"Sauce Labs Bike Light", "Sauce Labs Onesie"}, "$17.98"}
        };
    }

    @Test(dataProvider = "orders", groups = "smoke")
    public void userCanCompletePurchase(String[] products, String expectedSubTotal) {
        InventoryPage inventoryPage = loginAndOpenInventory();
        for (String product : products) {
            inventoryPage.addToCart(product);
        }

        var overview = inventoryPage.openCart()
                .proceedToCheckout()
                .continueWith("Test", "User", "33691");

        Assert.assertTrue(overview.getSubtotal().endsWith(expectedSubTotal),
                "unexpected subtotal : " + overview.getSubtotal());
        Assert.assertEquals(overview.finish().getConfirmationMessage(), "Thank you for your order!");
    }

    @DataProvider(name = "incompleteCheckoutInfo")
    public Object[][] incompleteCheckoutInfo() {
        return new Object[][] {
                {"", "User", "33602", "First Name is required"},
                {"Test", "", "33602", "Last Name is required"},
                {"Test", "User", "", "Postal Code is required"}
        };
    }

    @Test(dataProvider = "incompleteCheckoutInfo")
    public void checkoutRequiresAllFields(String first, String last, String zip, String expectedError) {
        CheckoutInfoPage info = loginAndOpenInventory()
                .addToCart("Sauce Labs Backpack")
                .openCart()
                .proceedToCheckout()
                .attemptContinue(first, last, zip);

        Assert.assertEquals(info.getErrorMessage(), "Error: " + expectedError);
    }
}
