package com.desafio.testetrinity.Utils.Api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * TOPHEADLINE, o HEAD da chamada da API, onde recebemos a lista de noticiais, o total de noticias, mais o status da requisição
 *
 */

public class TopHeadline {

    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private String totalResults;

    @SerializedName("articles")
    private List<Article> articleList;

    public String getStatus() {
        return status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public List<Article> getArticleList() {
        return articleList;
    }
}
