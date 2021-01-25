package com.desafio.testetrinity.Utils.Api;

import com.google.gson.annotations.SerializedName;

/**
 * Soucer recuperando o ID e o nome do Jornal do Json
 */
public class Source {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
