package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class Animals {
    public RequestSpecification requestSpecification;

    public Response response;

    public ResponseBody body;

    public String accessToken;

    public String types;

    public String unauthorizedTitle;

    @Given("I am an authenticated user")
    public void iAmAnAuthenticatedUser() throws IOException {

        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/server.properties");
        properties.load(fis);

        RestAssured.baseURI = "https://api.petfinder.com/v2";
        response = RestAssured.given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", properties.getProperty("API.Key"))
                .formParam("client_secret", properties.getProperty("Secret"))
                .when().post(RestAssured.baseURI + "/oauth2/token");

        body = response.getBody();

        String responseBody = body.asString();

        System.out.println(responseBody);

        JsonPath jsonPath = response.jsonPath();

        accessToken = jsonPath.getJsonObject("access_token").toString();

    }

    @When("I hit the get animals api url")
    public void iHitTheGetAnimalsApiUrl() {

        RestAssured.baseURI = "https://api.petfinder.com/v2";
        response = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get(RestAssured.baseURI + "/types");

        JsonPath jsonPath = response.jsonPath();

        types = jsonPath.getJsonObject("types.name[0]").toString();

        System.out.println(types);


    }

    @Then("I get {int} as the response code")
    public void iGetAsTheResponseCode(int arg0) {
        int expectedStatusCode = response.getStatusCode();
        assertEquals(expectedStatusCode, arg0);
    }

    @Then("I get animals in the response body of the api")
    public void iGetAnimalsInTheResponseBodyOfTheApi() {

        assertEquals("Dog", types);
        
    }

    @Given("I am an unauthenticated user")
    public void iAmAnUnauthenticatedUser() {

        RestAssured.baseURI = "https://api.petfinder.com/v2";
        response = RestAssured.given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .when().post(RestAssured.baseURI + "/oauth2/token");

        body = response.getBody();

        String responseBody = body.asString();

        System.out.println(responseBody);
        
    }

    @Given("I hit the get animals api url without access token")
    public void iHitTheGetAnimalsApiUrlWithoutAccessToken() {

        RestAssured.baseURI = "https://api.petfinder.com/v2";
        response = RestAssured.given()
                .when()
                .get(RestAssured.baseURI + "/types");

        JsonPath jsonPath = response.jsonPath();

        unauthorizedTitle = jsonPath.getJsonObject("title").toString();
        assertEquals(unauthorizedTitle,"Unauthorized");

    }
}
