package com.desafio.testetrinity.Utils.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * CLASSE feita para integrar o retrofit passando a URL base
 *
 */

public class wsClient {
    public static Retrofit getClient() {

        Gson gson = new GsonBuilder()
                .setDateFormat("dd-MM-yyyy'T'HH:mm:ss")
                .create();

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)).build();

        return new Retrofit.Builder()
                .baseUrl("http://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

    }


}
