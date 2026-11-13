package com.plabon.parkingadmin.api;




import com.plabon.parkingadmin.model.User;
import com.plabon.parkingadmin.model.UserResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestService {

    @POST("api/v1/login")
    Call<UserResult> login(@Body User user);



  /*  @POST("api/v1/allcategory")
    Call<CategoryResult> allCategory(@Body Token token);

    @POST("api/v1/newProduct")
    Call<ProductResult> newProducts(@Body Token token);

    @POST("api/v1/homepage")
    Call<ProductResult> popularProducts(@Body Token token);

    @POST("api/v1/getlist")
    Call<ProductResult> getCategoryProduct(@Body Category category);

    @POST("api/v1/placeorder")
    Call<OrdersResult> confirmPlaceOrder(@Body PlaceOrder placeOrder);

    @POST("api/v1/updateUser")
    Call<UserResult> updateUser(@Body User user);

    @GET("api/v1/product/search")
    Call<ProductResult> searchProduct(@Query("s") String search);
*/

}
