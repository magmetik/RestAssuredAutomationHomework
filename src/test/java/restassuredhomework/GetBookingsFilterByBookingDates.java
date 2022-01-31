package restassuredhomework;

import org.testng.annotations.Test;
import java.util.*;
import static io.restassured.RestAssured.given;

public class GetBookingsFilterByBookingDates extends BaseUrl {

    BaseUrl BaseUrl = new BaseUrl();

    @Test
    public void bookingFilterByBookingDates(){

        Map<String,Object> queryParameters = new HashMap<>();
        queryParameters.put("bookingdates.checkin","2014-03-13");
        queryParameters.put("bookingdates.checkout","2014-05-21");

        given()
                .log().all()
                .queryParams(queryParameters).
                when()
                .get(BaseUrl.baseUrl+"/booking").
                then()
                .statusCode(200)
                .log().all();
    }

}

