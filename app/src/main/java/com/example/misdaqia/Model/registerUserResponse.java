package com.example.misdaqia.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class registerUserResponse {

    @SerializedName("success")
    @Expose
    private Success successInfo;

    @SerializedName("data")
    @Expose
    private User userInfo;


    public Success getSuccessInfo() {
        return successInfo;
    }


    public User getUserInfo() {
        return userInfo;
    }
}





