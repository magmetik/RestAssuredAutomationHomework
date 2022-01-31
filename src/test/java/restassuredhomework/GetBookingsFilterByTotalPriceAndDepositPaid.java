package restassuredhomework;

import org.testng.annotations.Test;
import java.util.*;
import static io.restassured.RestAssured.given;

public class GetBookingsFilterByTotalPriceAndDepositPaid extends BaseUrl {

    BaseUrl BaseUrl = new BaseUrl();

    @Test
    public void bookingFilterByTotalPriceAndDepositPaid(){

        Map<String,Object> filterParameters = new HashMap<>();
        filterParameters.put("totalprice",111);
        filterParameters.put("depositpaid",true);

        given()
                .log().all()
                .queryParams(filterParameters).
                when()
                .get(BaseUrl.baseUrl+"/booking").
                then()
                .statusCode(200)
                .log().all();
    }
}
