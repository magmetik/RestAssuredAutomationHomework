package restassuredhomework;

import org.testng.annotations.*;
import static io.restassured.RestAssured.given;

public class MultipleGetBookingByBookingId extends BaseUrl {

    BaseUrl BaseUrl = new BaseUrl();

    @DataProvider(name = "dataProvider")
    public Object[][] dataProvider(){
        return new Object[][]{
                {7},
                {3},
                {5},
                {6}
        };
    }

    @Test(dataProvider =  "dataProvider")
    public void getBookingById(int bookingId){

        given()
                .log().all().
                when()
                .get(BaseUrl.baseUrl+"/booking/"+bookingId).
                then()
                .log().all();
    }
}
