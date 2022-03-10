import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class test {

    @Tag("1api")
    @Test
    @DisplayName("Погружение в API")
    public void mortyCharacter() {

        Response response1 = given()
                .baseUri("https://rickandmortyapi.com/")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/character/2")
                .then()
                .extract().response();

        String infoMortySmith = response1.getBody().asString();
        JSONObject jsonMortySmith = new JSONObject(infoMortySmith);
        JSONArray epWithMortySmith = jsonMortySmith.getJSONArray("episode");
        int epCount = epWithMortySmith.length();
        String lastEp = epWithMortySmith.getString(epCount - 1);
        System.out.println("Последний эпизод, где появлялся Морти Смит - " + lastEp);


        Response response2 = given()
                .baseUri("https://rickandmortyapi.com/")
                .contentType(ContentType.JSON)
                .when()
                .get(lastEp)
                .then()
                .extract().response();

        String lastMortySmithEp = response2.getBody().asString();
        JSONObject jsonLastEp = new JSONObject(lastMortySmithEp);
        JSONArray charInLastEp = jsonLastEp.getJSONArray("characters");
        int charCount = charInLastEp.length();
        String lastChar = charInLastEp.getString(charCount - 1);
        System.out.println("Последний персонаж последнего эпизода - " + lastChar);

    }

    @Tag("2api")
    @Test
    @DisplayName("Углубление в API")
    public void test1() throws IOException {
        JSONObject requestBody = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/test1.json"))));
        requestBody.put("name", "Tomato");
        requestBody.put("job", "Eat maket");

        Response response3 = given()
                .baseUri("https://reqres.in/")
                .contentType("application/json;charset=UTF-8")
                .log().all()
                .when()
                .body(requestBody.toString())
                .post("/api/users")
                .then()
                .statusCode(201)
                .log().all()
                .extract().response();

        String Tomato = response3.getBody().asString();
        JSONObject json = new JSONObject(Tomato);
        Assertions.assertEquals(json.getString("name"), "Tomato");
        Assertions.assertEquals(json.getString("job"), "Eat maket");
    }

}
