package com.example.projectem8.Interfice;

import com.example.projectem8.Model.ModelApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCall {

    @GET("json?")
    Call<ModelApi> getData(@Query("lat") String lat, @Query("lng") String lng);

}
