package restassuredhomework;

import org.testng.annotations.Test;
import java.util.*;
import static io.restassured.RestAssured.given;

public class GetBookingsFilterByName extends BaseUrl {

    BaseUrl BaseUrl = new BaseUrl();

    @Test
    public void getAllBookingsFilterByName(){

        Map<String,Object> parameters = new HashMap<>();
        parameters.put("firstname","Sally");
        parameters.put("lastname","Brown");

        given()
                .log().all()
                .queryParams(parameters).
                when()
                .get(BaseUrl.baseUrl+"/booking").
                then()
                .statusCode(200)
                .log().all();
    }
}

