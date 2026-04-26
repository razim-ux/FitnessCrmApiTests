package tests;

import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AuthTests {

    @Test
    public void successfulLoginTest() {
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body("{\"email\":\"test1@mail.ru\",\"password\":\"qwerty321\"}")
                .when()
                .post("https://coach-platform-nine.vercel.app/api/auth/login");

        System.out.println("Status checked: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());

        assert response.getStatusCode() == 200;
        assert response.jsonPath().getBoolean("ok") == true;
        assert response.jsonPath().getString("coach.email").equals("test1@mail.ru");
    }

    @Test
    public void wrongPasswordLoginTest() {
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body("{\"email\":\"test1@mail.ru\",\"password\":\"wrong123\"}")
                .when()
                .post("https://coach-platform-nine.vercel.app/api/auth/login");

        System.out.println("Status checked: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());

        assert response.getStatusCode() == 401;
        assert response.jsonPath().getBoolean("ok") == false;
        assert response.jsonPath().getString("error").equals("Invalid credentials");
    }
}