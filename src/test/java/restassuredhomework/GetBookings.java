package restassuredhomework;

import org.testng.annotations.*;
import static io.restassured.RestAssured.given;

public class GetBookings extends BaseUrl{

    BaseUrl BaseUrl = new BaseUrl();

    @BeforeClass
    public void createBooking(){

        String request = "{\n" +
                "    \"firstname\" : \"Sandy\",\n" +
                "    \"lastname\" : \"Blue\",\n" +
                "    \"totalprice\" : 200,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2015-01-01\",\n" +
                "        \"checkout\" : \"2015-02-04\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        given()
                .log().all()
                .header("Content-Type","application/json")
                .body(request).
                when()
                .post(BaseUrl.baseUrl+"/booking").
                then()
                .log().all();
    }

    @Test
    public void getAllBookings(){

        given()
                .log().all().
                when()
                .get(BaseUrl.baseUrl+"/booking").
                then()
                .statusCode(200)
                .log().all();
    }
}
