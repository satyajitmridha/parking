package com.plabon.parkingadmin.api;

import com.plabon.parkingadmin.api.clients.RestClient;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SetUserApiInterface {

  String JSONURL = RestClient.BASE_URL + "api/v1/";

  // @GET("json_parsing.php")
  @GET("setuser.php")
  Call<String> getString(@Query("n") String inputName,
                         @Query("m") String inputDes,
                         @Query("p") String inputp,
                         @Query("d") String inputd);

}
