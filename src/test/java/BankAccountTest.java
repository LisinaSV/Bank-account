import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;


class BankAccountTest {
    @BeforeAll
    static void setUpAll() { SelenideLogger.addListener( "allure", new AllureSelenide()); }

    @AfterAll
    static void tearDownAll() { SelenideLogger.removeListener("allure"); }
    @BeforeEach
    void setup() {
        Selenide.open("http://localhost:9999");
//            Configuration.baseUrl = "http://localhost:9999";
//            Configuration.timeout = 15000;
//            Configuration.headless = true;
//            ChromeOptions chromeOptions = new ChromeOptions();
//            chromeOptions.addArguments("--no-sandbox");
//            chromeOptions.addArguments("--disable-dev-shm-usage");
//            chromeOptions.addArguments("--headless=new");
//            chromeOptions.addArguments("--remote-allow-origins=*");
//            System.setProperty("webdriver.chrome.options", String.valueOf(chromeOptions.toJson()));
//            open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with registered active user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {

        var registeredUser = Registration.UserGenerator.getRegisteredUser("active");

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();


        $("h2")
                .shouldHave(Condition.exactText("Личный кабинет"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = Registration.UserGenerator.getUser("active");

        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = Registration.UserGenerator.getRegisteredUser("blocked");

        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button.button").click();


        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible, Duration.ofSeconds(10));
    }

}
