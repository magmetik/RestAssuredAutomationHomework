package restassuredhomework;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class GetBookingsFilterByAdditionalNeeds extends BaseUrl {

    BaseUrl BaseUrl = new BaseUrl();

    @Test
    public void BookingFilterByAdditionalNeeds(){

        given()
                .log().all()
                .queryParams("additionalneeds","Breakfast").
                when()
                .get(BaseUrl.baseUrl+"/booking")
                .then()
                .statusCode(200)
                .log().all();
    }


}

