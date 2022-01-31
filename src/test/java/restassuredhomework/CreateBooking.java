package restassuredhomework;

import io.restassured.response.ValidatableResponse;
import org.testng.ITestContext;
import org.testng.annotations.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateBooking extends BaseUrl {

    BaseUrl BaseUrl = new BaseUrl();

    @Test
    public void createBooking(ITestContext context) {

        String request = "{\n" +
                "    \"firstname\" : \"Jim\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        ValidatableResponse response =
                given()
                        .log().all()
                        .header("Content-Type","application/json")
                        .body(request).
                        when()
                        .post(BaseUrl.baseUrl+"/booking").
                        then()
                        .log().all()
                        .statusCode(200)
                        .body("booking.firstname",equalTo("Jim"))
                        .body("booking.lastname",equalTo("Brown"))
                        .body("booking.totalprice",lessThanOrEqualTo(111))
                        .body("booking.depositpaid",not("false"))
                        .body("booking.bookingdates.checkin",startsWith("2"))
                        .body("booking.bookingdates.checkout",endsWith("1"))
                        .body("booking.additionalneeds",equalTo("Breakfast"));

        int bookingId = response.extract().jsonPath().get("bookingid");

        context.setAttribute("bookingId",bookingId);

    }

    @AfterMethod
    public void getAllBooking(){

        given()
                .log().all().
                when()
                .get(BaseUrl.baseUrl+"/booking").
                then()
                .statusCode(200)
                .log().all();
    }


    @AfterClass
    public void deleteBooking(ITestContext context){

        int bookingId = (int) context.getAttribute("bookingId");

        String request = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        AuthenticationToken authenticationToken =
                given()
                        .header("Content-Type","application/json")
                        .body(request)
                        .log().all().
                        when()
                        .post(BaseUrl.baseUrl+"/auth").
                        then()
                        .extract()
                        .body()
                        .as(AuthenticationToken.class);

        String token = authenticationToken.getToken();

        given()
                .log().all()
                .header("Content-Type:","application/json")
                .header("Cookie","token="+token).
                when()
                .delete(BaseUrl.baseUrl+"/booking/"+bookingId).
                then()
                .statusCode(201)
                .log().all();
    }
}
