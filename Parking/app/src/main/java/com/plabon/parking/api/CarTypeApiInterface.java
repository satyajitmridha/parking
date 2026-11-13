package com.plabon.parking.api;

import com.plabon.parking.api.clients.RestClient;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CarTypeApiInterface {

  String JSONURL = RestClient.BASE_URL + "api/v1/";

  // @GET("json_parsing.php")
  @GET("cartype.php")
  Call<String> getString(@Query("s") String inputStuid);

}
