import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class TestPlan {
    private static final WebDriver driver = new ChromeDriver();

    @BeforeSuite
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", Utils.CHROME_DRIVER_LOCATION);
    }

    @Test(testName = "Login successfully")
    public static void loginSuccessfully() {
        driver.get(Utils.BASE_URL);
        LoginForm loginForm = new LoginForm(driver);
        loginForm.enterUsername();
        loginForm.enterPassword();
        loginForm.pressLoginButton();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        ProductsPage productsPage = new ProductsPage(driver);
        Assert.assertEquals(productsPage.getTitle(), "PRODUCTS");
    }

    @Test(testName = "Add one item to cart")
    public static void verifyItemAdded() {
        driver.get(Utils.BASE_URL);
        LoginForm loginForm = new LoginForm(driver);
        loginForm.enterUsername();
        loginForm.enterPassword();
        loginForm.pressLoginButton();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.addToCartBackpack();
        Assert.assertEquals(productsPage.getCardBadge(), "1");
    }

    @Test(testName = "Wrong Credentials - TC1")
    public static void wrongCredentials() {
        driver.get(Utils.BASE_URL);
        LoginForm loginForm = new LoginForm(driver);
        loginForm.enterUsername();
        loginForm.enterWrongPassword();
        loginForm.pressLoginButton();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Assert.assertEquals(loginForm.getErrorMessage(), "Epic sadface: Username and password do not match any user in this service");
    }

    @Test(testName = "Logout Correctly - TC2")
    public static void logoutCorrectly() {
        driver.get(Utils.BASE_URL);
        LoginForm loginForm = new LoginForm(driver);
        loginForm.enterUsername();
        loginForm.enterPassword();
        loginForm.pressLoginButton();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.pressCollapsibleMenu();
        productsPage.pressLogoutButton();

        Assert.assertEquals(loginForm.getLoginButtonMessage(), "Login");
    }

    @AfterSuite
    public static void cleanUp() {
        driver.manage().deleteAllCookies();
        driver.close();
    }
}