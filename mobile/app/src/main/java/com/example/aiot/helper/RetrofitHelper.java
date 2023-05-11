package com.example.aiot.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static final String token = "aio_DePt82n5ijnuAg6f0IySeNpCrqle";
    private static Retrofit retrofit = null;

    public static Retrofit init() {
        return init("https://io.adafruit.com/api/v2/hieplh/feeds/");
    }

    public static Retrofit init(String baseUrl) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> chain.proceed(
                            chain.request().newBuilder()
                                    .addHeader("X-AIO-Key", token)
                                    .addHeader("Accept", "application/json")
                                    .build()
                    ))
                    .build();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .baseUrl(baseUrl)
                    .build();
        }
        return retrofit;
    }

    public static Gson gsonWithDate(){
        final GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Date.class, new JsonDeserializer() {
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                try {
                    return df.parse(json.getAsString());
                } catch (final java.text.ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });

        return builder.create();
    }
}
