package restassuredhomework;

import org.testng.ITestContext;
import org.testng.annotations.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UpdateBooking extends BaseUrl {

    BaseUrl BaseUrl = new BaseUrl();

    @BeforeClass
    public void createBooking(ITestContext context) {
        String request = "{\n" +
                "    \"firstname\" : \"Lucas\",\n" +
                "    \"lastname\" : \"Sue\",\n" +
                "    \"totalprice\" : 500,\n" +
                "    \"depositpaid\" : false,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2010-01-01\",\n" +
                "        \"checkout\" : \"2012-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Lunch\"\n" +
                "}";

        int bookingId =
                given()
                        .log().all()
                        .body(request)
                        .header("Content-Type","application/json").
                        when()
                        .post(BaseUrl.baseUrl+"/booking").
                        then()
                        .extract()
                        .jsonPath()
                        .get("bookingid");

        context.setAttribute("bookingId",bookingId);
    }

    @Test
    public void putUpdateBooking(ITestContext context) {

        String request = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        AuthenticationToken authenticationToken =
                given()
                        .log().all()
                        .header("Content-Type","application/json")
                        .body(request).
                        when()
                        .post(BaseUrl.baseUrl+"/auth").
                        then()
                        .extract()
                        .body()
                        .as(AuthenticationToken.class);

        String token = authenticationToken.getToken();

        String requestBody = "{\n" +
                "    \"firstname\" : \"James\",\n" +
                "    \"lastname\" : \"Lucy\",\n" +
                "    \"totalprice\" : 100,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2017-02-05\",\n" +
                "        \"checkout\" : \"2020-08-10\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Dinner\"\n" +
                "}";


        int bookingId = (int) context.getAttribute("bookingId");

        given()
                .log().all()
                .header("Content-Type", "application/json")
                .header("Accept","application/json")
                .header("Cookie","token="+token)
                .body(requestBody).
                when()
                .put(BaseUrl.baseUrl+"/booking/" + bookingId).
                then()
                .statusCode(200)
                .body("firstname",equalTo("James"))
                .body("lastname",equalTo("Lucy"))
                .body("totalprice",lessThan(500))
                .body("depositpaid",not(false))
                .body("bookingdates.checkin",equalTo("2017-02-05"))
                .body("bookingdates.checkout",endsWith("0"))
                .body("additionalneeds",equalTo("Dinner"))
                .log().all();
    }

    @AfterClass
    public void getBookingByBookingId(ITestContext context){

        int bookingId = (int) context.getAttribute("bookingId");

        given()
                .log().all().
                when()
                .get(BaseUrl.baseUrl+"/booking/"+bookingId).
                then()
                .statusCode(200)
                .body("firstname",equalTo("James"))
                .body("bookingdates.checkin",endsWith("5"))
                .body("depositpaid",equalTo(true))
                .body("totalprice",lessThan(500))
                .log().all();
    }
}

