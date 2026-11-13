package com.plabon.parkingadmin.api;

import com.plabon.parkingadmin.api.clients.RestClient;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetPriceListApiInterface {

  String JSONURL = RestClient.BASE_URL + "api/v1/";

  // @GET("json_parsing.php")
  @GET("getcartypeprice.php")
  Call<String> getString(@Query("s") String inputName);

}
