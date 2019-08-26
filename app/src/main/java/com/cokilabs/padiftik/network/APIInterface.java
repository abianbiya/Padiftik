package com.cokilabs.padiftik.network;

/**
 * Created by Jim Geovedi on 3/30/2017.
 */


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    // account
    @POST("api/login")
    Call<ResponseBody> login(
            @Query("username") String username,
            @Query("password") String password
    );

    @GET("api/get/pengumuman")
    Call<ResponseBody> getPengumumans();

    @GET("api/get/status")
    Call<ResponseBody> getStatus();


    @POST("api/set/status")
    Call<ResponseBody> setStatus(
            @Query("status") Integer status
    );

    @POST("api/post/pengumuman")
    Call<ResponseBody> postPengumuman(
            @Query("pengumuman") String pengumuman
    );

    @FormUrlEncoded
    @POST("email/ortu/cek")
    Call<ResponseBody> authOrtu(
            @Field("pin") String token
    );



}