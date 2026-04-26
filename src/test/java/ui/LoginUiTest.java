package ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.pages.LoginPage;
import ui.pages.ClientsPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginUiTest {

    private WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    private void login() {
        driver.get("https://coach-platform-nine.vercel.app/login");

        driver.findElement(By.cssSelector("input[type='email']"))
                .sendKeys("test1@mail.ru");

        driver.findElement(By.cssSelector("input[autocomplete='current-password']"))
                .sendKeys("qwerty321");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> d.getCurrentUrl().contains("/dashboard"));
    }

    @Test
    void successfulLogin() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();
        loginPage.login("test1@mail.ru", "qwerty321");

        assertTrue(
                driver.getCurrentUrl().contains("/dashboard"),
                "После логина должен открыться dashboard"
        );
    }

    @Test
    void createClient() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();
        loginPage.login("test1@mail.ru", "qwerty321");

        ClientsPage clientsPage = new ClientsPage(driver);

        String clientName = "Автотест UI";

        clientsPage.open();
        clientsPage.createClient(clientName);

        assertTrue(clientsPage.isClientVisible(clientName));
    }
    @Test
    void openClientAfterCreate() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();
        loginPage.login("test1@mail.ru", "qwerty321");

        ClientsPage clientsPage = new ClientsPage(driver);

        String clientName = "Автотест UI 2";

        clientsPage.open();
        clientsPage.createClient(clientName);

        assertTrue(clientsPage.isClientVisible(clientName));

        clientsPage.openClient(clientName);

        assertTrue(
                clientsPage.isClientPageOpened(),
                "Должна открыться страница клиента"
        );
    }
    @Test
    void failedLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.loginWithoutWaitingDashboard("test1@mail.ru", "wrongpassword");
        assertTrue(
                loginPage.isInvalidCredentialsErrorVisible(),
                "Ошибка Invalid credentials должна отображаться"
        );
    }
    @Test
    void logout() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login("test1@mail.ru", "qwerty321");
        driver.findElement(By.xpath("//button[text()='Выйти']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> d.getCurrentUrl().contains("/login"));
        assertTrue(
                driver.getCurrentUrl().contains("/login"),
                "После logout пользователь должен попасть на страницу логина"
        );
    }
    @Test
    void loginWithEmptyFields() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.clickLoginButton();
        assertTrue(
                driver.getCurrentUrl().contains("/login"),
                "При пустых полях пользователь не должен войти"
        );
    }
}