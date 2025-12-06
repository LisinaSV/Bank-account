import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import org.junit.jupiter.api.BeforeAll;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class Registration {

    private static final RequestSpecification REQUEST_SPEC = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

//    @BeforeAll
    static void setUpAll() {
        // сам запрос
        given() // "дано"
                .spec(REQUEST_SPEC) // указываем, какую спецификацию используем
                .body(new RegistrationDto("user", "password", "active")) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    private static final Faker FAKER = new Faker(new Locale("en"));


    private Registration() {
    }


    public static void sendRequest(RegistrationDto user) {
        given()
                .spec(REQUEST_SPEC)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        return FAKER.name().username();
    }

    public static String getRandomPassword() {
        return FAKER.internet().password();
    }


    public static class UserGenerator {
        private UserGenerator() {
        }


        public static RegistrationDto getUser(String status) {
            return new RegistrationDto(
                    getRandomLogin(),
                    getRandomPassword(),
                    status
            );
        }


        public static RegistrationDto getRegisteredUser(String status) {
            var user = getUser(status);
            sendRequest(user);
            return user;
        }
    }
}


@Value
class RegistrationDto {
    String login;
    String password;
    String status;
}
