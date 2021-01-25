package com.desafio.testetrinity.Utils.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Arquivo criado para indexça a URL base mais o continente com a Query country que é requisito na API e mais a Query apikey, que é minha chave da API
 * TUDO ISSO NO TOP HEADLINE
 */

public interface Service {

    @GET("top-headlines")
    Call<TopHeadline> getTopHeadlines(@Query("country") String country, @Query("apiKey") String apiKey);
}
