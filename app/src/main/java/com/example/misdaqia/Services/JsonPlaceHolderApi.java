package com.example.misdaqia.Services;

import com.example.misdaqia.Model.Category;
import com.example.misdaqia.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
 import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {


    @FormUrlEncoded
    @POST("registerapi")
    Call<User> CreateUser(
            @Field("name") String Name,
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("email_verified_at") String email_verified);

    @GET("getvichleapi")
    Call<Category> getCategories();

//    @FormUrlEncoded
//    @POST("register")
//    Call<User> CreateUser(@Body User user);


}