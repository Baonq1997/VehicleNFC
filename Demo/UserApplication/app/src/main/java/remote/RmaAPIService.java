package remote;

import com.google.gson.JsonObject;

import java.util.List;

import model.Order;
import model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RmaAPIService {
    @GET("/test")
    Call<String> getServer();

    @GET("/user/{id}")
    Call<User> getUserById(@Path("id") Integer userId);

//
//    @GET("/meter/{id}")
//    Call<Meter> getMeterById(@Path("id") Integer meterId);

    @POST("/transaction/create")
    @Headers({"Content-Type: application/json"})
    Call<Order> sendTransactionToServer(@Body Order transaction);

    @POST("/user/login")
    @FormUrlEncoded
    Call<User> login(@Field("phone") String phone, @Field("password") String password);

    @GET("/order/get-order/{id}")
    Call<Order> getOrderById(@Path("id") Integer orderId);

    @GET("/order/open-order/{userId}")
    Call<Order> getOpenOrderByUserId(@Path("userId") Integer userId);


    @POST("/user/top-up")
    @FormUrlEncoded
    Call<User> topUp(@Field("userId") String userId, @Field("amount") double amount);

    @GET("/order/orders")
    Call<List<Order>> getOrderByUserId(@Query("userId") Integer userId);

    @POST("/user/create-user")
    @Headers({"Content-Type: application/json"})
    Call<String> sendUserToServer(@Body User user);


    @GET("user/get-user-by-phone")
    Call<User> getUserByPhone(@Query("phoneNumber") String phone);

    @POST("/user/confirm-user")
    @FormUrlEncoded
    Call<Boolean> verifyNumber(@Field("phone") String phone, @Field("confirmCode") String code);

    @POST("/user/confirm-reset-password")
    @FormUrlEncoded
    Call<Boolean> verifyResetPassword(@Field("phone") String phone, @Field("confirmCode") String code);

    @POST("/user/request-new-confirm")
    @FormUrlEncoded
    Call<Boolean> resendCode(@Field("phone") String phone);

    @GET("/user/request-reset-password")
    Call<Boolean> requestResetPassword(@Query("phoneNumber") String phone);

    @POST("/user/reset-password")
    @FormUrlEncoded
    Call<Boolean> updatePasswordByPhone(@Field("phoneNumber") String phone, @Field("newPassword") String pass);

    @POST("/user/change-password")
    @FormUrlEncoded
    Call<Boolean> changePassword(@Field("phoneNumber") String phone,
                                 @Field("oldPassword") String currentPass, @Field("newPassword") String newPass);

//    @GET("/bulk/3d78ccdddf5bd1c43a6587ff/USD")
//    Call<JsonObject> getUSD();

    @GET
    Call<JsonObject> getUSD(@Url String url);

    @GET("/user/register-token")
    Call<Boolean> sendDeviceTokenToServer(@Query("token") String token, @Query("PhoneNumber") String phoneNumber);
}
