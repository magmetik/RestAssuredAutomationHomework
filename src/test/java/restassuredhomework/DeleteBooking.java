package restassuredhomework;

import org.testng.ITestContext;
import org.testng.annotations.*;
import static io.restassured.RestAssured.given;

public class DeleteBooking extends BaseUrl {

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
                        .header("Content-Type","application/json")
                        .body(request)
                        .log().all().
                        when()
                        .post(BaseUrl.baseUrl+"/booking").
                        then()
                        .log().all()
                        .extract()
                        .jsonPath()
                        .get("bookingid");

        context.setAttribute("bookingId",bookingId);
    }

    @Test
    public void deleteBooking(ITestContext context) {

        int bookingId = (int) context.getAttribute("bookingId");

        String request = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        AuthenticationToken authenticationToken =
                given()
                        .log().all()
                        .header("Content-Type", "application/json")
                        .body(request).
                        when()
                        .post(BaseUrl.baseUrl + "/auth").
                        then()
                        .extract()
                        .body()
                        .as(AuthenticationToken.class);

        String token = authenticationToken.getToken();
        System.out.println("token: "+token);

        given()
                .log().all()
                .header("Content-Type:", "application/json")
                .header("Cookie", "token=" + token).
                when()
                .delete(BaseUrl.baseUrl + "/booking/" + bookingId).
                then()
                .statusCode(201)
                .log().all();
    }

    @AfterClass
    public void bookingByBookingId(ITestContext context){

        int bookingId = (int) context.getAttribute("bookingId");

        given()
                .log().all().
                when()
                .get(BaseUrl.baseUrl+"/booking/"+bookingId).
                then()
                .statusCode(404)
                .log().all();
    }
}

