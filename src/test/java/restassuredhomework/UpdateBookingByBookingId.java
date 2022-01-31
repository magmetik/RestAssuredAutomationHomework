package restassuredhomework;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class UpdateBookingByBookingId extends BaseUrl {

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

        RequestSpecification restAssuredRequest = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept","application/json")
                .header("Cookie","token="+token)
                .log().all();

        Response response = restAssuredRequest.body(requestBody).put(BaseUrl.baseUrl+"/booking/" + bookingId);
        attachment(restAssuredRequest, baseUrl, response);
        Assert.assertEquals(response.getStatusCode(),200);

    }

    public String attachment(RequestSpecification httpRequest, String baseUrl, Response response) {
        String html = "Url = " + baseUrl + "\n \n" +
                "Request Headers = " + ((RequestSpecificationImpl) httpRequest).getHeaders() + "\n \n" +
                "Request Body = " + ((RequestSpecificationImpl) httpRequest).getBody() + "\n \n" +
                "Response Body = " + response.getBody().asString();
        Allure.addAttachment("Request Detail", html);
        return html;
    }

}

