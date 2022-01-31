package restassuredhomework;

import org.testng.ITestContext;
import org.testng.annotations.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class PartialUpdateBooking extends BaseUrl {

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
                        .header("Content-Type", "application/json")
                        .body(request)
                        .log().all().
                        when()
                        .post(BaseUrl.baseUrl + "/booking").
                        then()
                        .log().all()
                        .extract()
                        .jsonPath()
                        .get("bookingid");

        context.setAttribute("bookingId", bookingId);

    }

    @DataProvider(name = "dataProvider")
    public Object[][] dataProvider(ITestContext context){

        int bookingId = (int) context.getAttribute("bookingId");

        return new Object[][]{
                {bookingId, 200}
        };
    }



    @Test(dataProvider = "dataProvider")
    public void partialUpdateBookingByBookingId(int bookingId, int statusCode){


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
        System.out.println("token: "+token);

        String requestBody = "{\n" +
                "    \"firstname\" : \"James\",\n" +
                "    \"lastname\" : \"Brown\"\n" +
                "}";

        given()
                .log().all()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("Cookie","token="+token)
                .body(requestBody).
                when()
                .patch(BaseUrl.baseUrl+"/booking/"+bookingId).
                then()
                .log().all()
                .statusCode(statusCode)
                .body("firstname",equalTo("James"))
                .body("lastname",equalTo("Brown"));
    }

    @AfterClass
    public void test(ITestContext context){

        int bookingId = (int) context.getAttribute("bookingId");

        given()
                .log().all().
                when()
                .get(BaseUrl.baseUrl+"/booking/"+bookingId).
                then()
                .log().all()
                .statusCode(200);
    }
}

