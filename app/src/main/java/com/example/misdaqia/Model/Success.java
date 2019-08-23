package com.example.misdaqia.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Success {


    @SerializedName("token")
    @Expose
    private String token;


    @SerializedName("name")
    @Expose
    private String name;

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }
}
