package com.plabon.parking.api;

import com.plabon.parking.api.clients.RestClient;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SetCarEntryApiInterface {

  String JSONURL = RestClient.BASE_URL + "api/v1/";

  // @GET("json_parsing.php")
  @GET("setCarEntry.php")
  Call<String> getString(@Query("cno") String cno,
                         @Query("ctype") String ctype,
                         @Query("m") String m,
                         @Query("indate") String indate,
                         @Query("intime") String intime,
                         @Query("entryby") String entryby,
                         @Query("ltype") String ltype);

}
