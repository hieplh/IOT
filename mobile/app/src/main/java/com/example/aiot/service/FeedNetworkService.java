package com.example.aiot.service;

import com.example.aiot.model.Feed;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FeedNetworkService {

    @GET("{sensorId}/data")
    Call<List<Object>> getFeed(@Path("sensorId") String sensorId);

    @POST("{sensorId}/data")
    Call<Feed> createFeed(@Path("sensorId") String sensorId, @Body Object obj);
}
