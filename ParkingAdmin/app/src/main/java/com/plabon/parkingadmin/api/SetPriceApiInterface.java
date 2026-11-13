package com.plabon.parkingadmin.api;

import com.plabon.parkingadmin.api.clients.RestClient;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SetPriceApiInterface {

  String JSONURL = RestClient.BASE_URL + "api/v1/";

  // @GET("json_parsing.php")
  @GET("setcartypeprice.php")
  Call<String> getString(@Query("s") String inputName,
                         @Query("lt") String inputDes,
                         @Query("p") String inputp);

}
