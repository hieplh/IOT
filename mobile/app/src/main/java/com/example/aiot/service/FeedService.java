package com.example.aiot.service;

import android.content.Context;
import android.util.Log;

import com.example.aiot.helper.RetrofitHelper;
import com.example.aiot.model.Feed;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FeedService {

    private Context context;
    private AppDatabase db;
    private Retrofit retrofit;
    private Thread threadFetchSensorData = null;
    private Runnable runnable = null;
    private boolean isStart = false;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public FeedService(Context context, AppDatabase db) {
        this.context = context;
        this.db = db;
        this.retrofit = RetrofitHelper.init();
    }

    public Feed getLast(String feedKey) {
        return db.feedRepository().getLast(feedKey);
    }

    public List<Feed> getAll(String feedKey) {
        return db.feedRepository().getAll(feedKey);
    }

    public void create(String sensorId, Object value) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"value\":").append(((boolean) value) ? 1 : 0);
        json.append("}");
        Object payload = new Gson().fromJson(json.toString(), Object.class);
        retrofit.create(FeedNetworkService.class).createFeed(sensorId, payload).enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Log.i("feed", response.toString());
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e("feed", t.toString());
            }
        });
    }

    public void insert(List<Feed> feeds) {
        Feed feed = feeds.get(0);
        try {
            db.feedRepository().insertAll(feed);
        } catch (Exception e) {
             Log.w("feed", "Error Feed: " + feed + " | Msg: " + e);
        }
    }

    public void startFetchDataSensor() {
        startFetchDataSensor(2000);
    }

    public void startFetchDataSensor(final int delay) {
        if (threadFetchSensorData == null || threadFetchSensorData.isInterrupted()) {
            isStart = true;
            runnable = () -> {
                while (isStart) {
                    fetchSensorData("sensor.sensor-1");
                    fetchSensorData("sensor.sensor-2");

                    try {
                        Thread.sleep(delay);
                    } catch (Exception e) {
                    }
                }
            };
            threadFetchSensorData = new Thread(runnable, "threadFetchSensorData");
            threadFetchSensorData.start();
        }
    }

    public void stopFetchDataSensor() {
        isStart = false;
        runnable = null;
        threadFetchSensorData.interrupt();
        threadFetchSensorData = null;
    }

    private void fetchSensorData(String sensorId) {
        retrofit.create(FeedNetworkService.class).getFeed(sensorId).enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response.isSuccessful()) {
                    List<Feed> feeds = new ArrayList<>();
                    response.body().forEach(r -> {
                        LinkedTreeMap linkedTreeMap = (LinkedTreeMap) r;
                        try {
                            feeds.add(new Feed(
                                    (String) linkedTreeMap.get("id"),
                                    (String) linkedTreeMap.get("value"),
                                    ((Double) linkedTreeMap.get("feed_id")).longValue(),
                                    (String) linkedTreeMap.get("feed_key"),
                                    formatter.parse((String) linkedTreeMap.get("created_at")),
                                    ((Double) linkedTreeMap.get("created_epoch")).longValue(),
                                    (String) linkedTreeMap.get("expiration")
                            ));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    insert(feeds);
                } else {
                    Log.w("feed", "Code: " + response.code() + " | Msg: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Log.w("feed", "Error: " + t);
                t.printStackTrace();
            }
        });
    }
}
