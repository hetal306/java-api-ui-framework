package com.portfolio.ui;

import com.portfolio.base.BaseUiTest;
import com.portfolio.config.ConfigReader;
import com.portfolio.driver.DriverFactory;
import com.portfolio.pages.InventoryPage;
import com.portfolio.pages.LoginPage;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Feature("Login")
public class LoginTest extends BaseUiTest {

    @Test
    public void validUserCanLogin() {
        InventoryPage inventoryPage = new LoginPage(DriverFactory.getDriver())
                .open()
                .loginAs(ConfigReader.get("ui.username"), ConfigReader.get("ui.password"));

        Assert.assertEquals(inventoryPage.getTitle(), "Products");
    }

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return new Object[][] {
                {"standard_user", "wrong_password", "Username and password do not match"},
                {"locked_out_user", "secret_sauce", "this user has been locked out"},
                {"", "secret_sauce", "Username is required"},
                {"standard_user", "", "Password is required"}
        };
    }

    @Test(dataProvider = "invalidLogins")
    public void invalidLogin(String username, String password, String expectedError) {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver())
                .open()
                .attemptLogin(username, password);

        Assert.assertTrue(loginPage.getErrorMessage().contains(expectedError), "unexpected error message " + loginPage.getErrorMessage());
    }
}
