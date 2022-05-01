package com.example.projectem8.Interfice;

import com.example.projectem8.ModelFlickr.FlickrModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCall2 {

    @GET("services/rest/?method=flickr.photos.search&api_key=79d466885188b99d6762980d64029892&format=json&nojsoncallback=1")
    Call<FlickrModel> getData(@Query("lat") String lat, @Query("lon") String lon);

}
