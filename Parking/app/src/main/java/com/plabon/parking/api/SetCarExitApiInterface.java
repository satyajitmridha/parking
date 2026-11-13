package com.plabon.parking.api;

import com.plabon.parking.api.clients.RestClient;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SetCarExitApiInterface {

  String JSONURL = RestClient.BASE_URL + "api/v1/";

  // @GET("json_parsing.php")
  @GET("setCarExitPay.php")
  Call<String> getString(@Query("id") String id,
                         @Query("uid") String uid,
                         @Query("amount") String am,
                         @Query("od") String od,
                         @Query("otim") String otim,
                         @Query("dc") String dc);

}
