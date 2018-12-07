package remote;

import model.Location;
import model.Meter;
import model.Order;
import model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by elpsychris on 03/12/2018.
 */

public interface RmaAPIService {
    @GET("/test")
    Call<String> getServer();

    @GET("/user/get-user/{id}")
    Call<User> getUserById(@Path("id") String userId);

    @GET("/order/open-order/{userId}")
    Call<Order> getOpenOrderByUserId(@Path("userId") String userId);


    @GET("/location/get/{id}")
    Call<Location> getLocationById(@Path("id") Integer locationId);

    @POST("/order/create")
    @Headers({"Content-Type: application/json"})
    Call<Order> sendTransactionToServer(@Body Order order );

    @POST("/user/update-user-sms")
    @Headers({"Content-Type: application/json"})
    Call<User> updateUserSmS(@Body User user);

}
