import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static java.awt.SystemColor.text;
import static org.hamcrest.Matchers.equalTo;

public class BankAccountTest {

    private static RequestSpecification requestSpec;
    private final Faker faker = new Faker(new Locale("ru"));

    @BeforeAll
    static void setUp() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    void shouldCreateUser() {
        String login = faker.name().username();
        String password = faker.internet().password();
        given()
                .spec(requestSpec)
                .body("{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}")
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200)
                .header("ContentType" ,"application/json;charset=UTF-8")
                .contentType(ContentType.JSON)
                .body("login", equalTo(login))
                .body("password", equalTo(password));
    }
}
