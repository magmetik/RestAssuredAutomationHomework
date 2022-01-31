package restassuredhomework;

import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetBookingByBookingId extends BaseUrl {

    BaseUrl BaseUrl = new BaseUrl();

    @BeforeClass
    public void createBooking(ITestContext context){

        String request = "{\n" +
                "    \"firstname\" : \"Jimmy\",\n" +
                "    \"lastname\" : \"Lucas\",\n" +
                "    \"totalprice\" : 150,\n" +
                "    \"depositpaid\" : false,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2020-01-01\",\n" +
                "        \"checkout\" : \"2021-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        int bookingId =
                given()
                        .log().all()
                        .header("Content-Type","application/json")
                        .body(request).
                        when()
                        .post(BaseUrl.baseUrl+"/booking").
                        then()
                        .log().all()
                        .extract()
                        .jsonPath()
                        .get("bookingid");

        context.setAttribute("bookingId",bookingId);
    }

    @DataProvider(name = "dataProvider")
    public Object[][] dataProvider(ITestContext context){

        int bookingId = (int) context.getAttribute("bookingId");

        return new Object[][]{
                {bookingId, 200}
        };
    }

    @Test(dataProvider = "dataProvider")
    public void getBookingById(int bookingId, int statusCode){

        given()
                .log().all().
                when()
                .get(BaseUrl.baseUrl+"/booking/"+bookingId).
                then()
                .statusCode(statusCode)
                .body("firstname",equalTo("Jimmy"))
                .body("lastname",equalTo("Lucas"))
                .body("totalprice",lessThan(200))
                .body("depositpaid",equalTo(false))
                .body("bookingdates.checkin",startsWith("2"))
                .body("bookingdates.checkout",endsWith("1"))
                .body("additionalneeds",equalTo("Breakfast"))
                .log().all();
    }
}
