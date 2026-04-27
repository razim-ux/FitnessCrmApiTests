package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import config.TestConfig;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void open() {
        driver.get(TestConfig.BASE_URL + "/login");
    }

    public void login(String email, String password) {
        driver.findElement(By.cssSelector("input[type='email']"))
                .sendKeys(email);

        driver.findElement(By.cssSelector("input[autocomplete='current-password']"))
                .sendKeys(password);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(d -> d.findElement(By.xpath("//*[contains(text(),'Клиенты')]")));

        System.out.println("CURRENT URL AFTER LOGIN: " + driver.getCurrentUrl());
    }
    public void loginWithoutWaitingDashboard(String email, String password) {
        driver.findElement(By.cssSelector("input[type='email']"))
                .sendKeys(email);

        driver.findElement(By.cssSelector("input[autocomplete='current-password']"))
                .sendKeys(password);

        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }
    public boolean isInvalidCredentialsErrorVisible() {
        WebElement errorElement = wait.until(
                d -> d.findElement(By.xpath("//*[text()='Invalid credentials']"))
        );

        return errorElement.isDisplayed();
    }
    public void clickLoginButton() {
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }
}