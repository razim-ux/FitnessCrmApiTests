package tests;

import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class ClientTests {

    @Test
    public void getClientsWithAuthTest() {

        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("email", "test1@mail.ru");
        loginBody.put("password", "qwerty321");

        Response loginResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(loginBody)
                .when()
                .post("https://coach-platform-nine.vercel.app/api/auth/login");

        String sessionCookie = loginResponse.getCookie("session");

        Response clientsResponse = RestAssured
                .given()
                .cookie("session", sessionCookie)
                .when()
                .get("https://coach-platform-nine.vercel.app/api/clients");

        System.out.println("Clients status: " + clientsResponse.getStatusCode());
        System.out.println("Clients body: " + clientsResponse.getBody().asString());

        assert clientsResponse.getStatusCode() == 200;
        assert clientsResponse.jsonPath().getBoolean("ok") == true;
        assert clientsResponse.jsonPath().getList("data.clients").size() > 0;
    }

    @Test
    public void createClientTest() {

        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("email", "test1@mail.ru");
        loginBody.put("password", "qwerty321");

        Response loginResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(loginBody)
                .when()
                .post("https://coach-platform-nine.vercel.app/api/auth/login");

        String sessionCookie = loginResponse.getCookie("session");

        Map<String, Object> clientBody = new HashMap<>();
        clientBody.put("name", "Autotest Client");

        Response createResponse = RestAssured
                .given()
                .contentType("application/json")
                .cookie("session", sessionCookie)
                .body(clientBody)
                .when()
                .post("https://coach-platform-nine.vercel.app/api/clients");

        System.out.println("Create status: " + createResponse.getStatusCode());
        System.out.println("Create body: " + createResponse.getBody().asString());

        assert createResponse.getStatusCode() == 201;
        assert createResponse.jsonPath().getBoolean("ok") == true;
    }

    @Test
    public void createClientAndFindInListTest() {

        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("email", "test1@mail.ru");
        loginBody.put("password", "qwerty321");

        Response loginResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(loginBody)
                .when()
                .post("https://coach-platform-nine.vercel.app/api/auth/login");

        String sessionCookie = loginResponse.getCookie("session");

        String clientName = "Autotest " + System.currentTimeMillis();

        Map<String, Object> clientBody = new HashMap<>();
        clientBody.put("name", clientName);

        Response createResponse = RestAssured
                .given()
                .contentType("application/json")
                .cookie("session", sessionCookie)
                .body(clientBody)
                .when()
                .post("https://coach-platform-nine.vercel.app/api/clients");

        System.out.println("Create status: " + createResponse.getStatusCode());
        System.out.println("Create body: " + createResponse.getBody().asString());

        assert createResponse.getStatusCode() == 201;
        assert createResponse.jsonPath().getBoolean("ok") == true;

        String createdClientId = createResponse.jsonPath().getString("data.client.id");

        Response clientsResponse = RestAssured
                .given()
                .cookie("session", sessionCookie)
                .when()
                .get("https://coach-platform-nine.vercel.app/api/clients");

        System.out.println("Clients status: " + clientsResponse.getStatusCode());
        System.out.println("Clients body: " + clientsResponse.getBody().asString());

        assert clientsResponse.getStatusCode() == 200;
        assert clientsResponse.jsonPath().getBoolean("ok") == true;

        boolean found = clientsResponse.jsonPath()
                .getList("data.clients.id")
                .contains(createdClientId);

        assert found == true;
    }

    @Test
    public void createClientWithCyrillicTest() {

        RestAssured.config = RestAssured.config()
                .encoderConfig(
                        io.restassured.config.EncoderConfig.encoderConfig()
                                .defaultContentCharset("UTF-8")
                );

        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("email", "test1@mail.ru");
        loginBody.put("password", "qwerty321");

        Response loginResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(loginBody)
                .when()
                .post("https://coach-platform-nine.vercel.app/api/auth/login");

        String sessionCookie = loginResponse.getCookie("session");

        String clientName = "Автотест " + System.currentTimeMillis();

        Map<String, Object> clientBody = new HashMap<>();
        clientBody.put("name", clientName);

        Response createResponse = RestAssured
                .given()
                .contentType("application/json; charset=UTF-8")
                .accept("application/json")
                .cookie("session", sessionCookie)
                .body(clientBody)
                .when()
                .post("https://coach-platform-nine.vercel.app/api/clients");

        System.out.println("Create status: " + createResponse.getStatusCode());
        System.out.println("Create body: " + createResponse.getBody().asString());

        assert createResponse.getStatusCode() == 201;
        assert createResponse.jsonPath().getBoolean("ok") == true;

        String createdClientId = createResponse.jsonPath().getString("data.client.id");

        Response clientsResponse = RestAssured
                .given()
                .accept("application/json")
                .cookie("session", sessionCookie)
                .when()
                .get("https://coach-platform-nine.vercel.app/api/clients");

        String actualName = clientsResponse.jsonPath()
                .getString("data.clients.find { it.id == '" + createdClientId + "' }.name");

        System.out.println("Expected name: " + clientName);
        System.out.println("Actual name: " + actualName);

        assert actualName.equals(clientName);
    }
}