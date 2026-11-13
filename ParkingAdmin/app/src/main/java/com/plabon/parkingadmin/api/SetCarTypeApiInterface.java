package com.plabon.parkingadmin.api;

import com.plabon.parkingadmin.api.clients.RestClient;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SetCarTypeApiInterface {

  String JSONURL = RestClient.BASE_URL + "api/v1/";

  // @GET("json_parsing.php")
  @GET("setcartype.php")
  Call<String> getString(@Query("s") String inputName,
                         @Query("sdes") String inputDes);

}
