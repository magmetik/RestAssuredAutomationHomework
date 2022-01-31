package restassuredhomework;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class GetBookingsFilterByTotalPrice extends BaseUrl {

    BaseUrl BaseUrl = new BaseUrl();

    @Test
    public void getBookingFilterByTotalPrice(){

        given()
                .log().all()
                .queryParam("totalprice",111).
                when()
                .get(BaseUrl.baseUrl+"/booking").
                then()
                .statusCode(200)
                .log().all();
    }


}
