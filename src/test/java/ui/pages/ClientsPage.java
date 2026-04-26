package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ClientsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public ClientsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get("https://coach-platform-nine.vercel.app/dashboard/clients");
    }

    public void createClient(String name) {
        driver.findElement(By.cssSelector("input[placeholder='Имя клиента']"))
                .sendKeys(name);

        driver.findElement(By.cssSelector("button[title='Добавить клиента']"))
                .click();

        wait.until(d -> d.switchTo().alert()).accept();
    }

    public boolean isClientVisible(String name) {
        WebElement clientElement = wait.until(
                d -> d.findElement(By.xpath("//*[text()='" + name + "']"))
        );
        return clientElement.isDisplayed();
    }

    public void openClient(String name) {
        WebElement clientLink = wait.until(
                d -> d.findElement(By.xpath("//a[.//*[text()='" + name + "']]"))
        );

        clientLink.click();

        wait.until(d -> d.getCurrentUrl().contains("/dashboard/clients/"));
    }

    public boolean isClientPageOpened() {
        return driver.getCurrentUrl().contains("/dashboard/clients/");
    }
}