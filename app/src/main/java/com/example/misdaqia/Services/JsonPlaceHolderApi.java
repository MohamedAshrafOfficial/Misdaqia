package com.example.misdaqia.Services;

import com.example.misdaqia.Model.LoginUserResponse;
import com.example.misdaqia.Model.MainCategory;
import com.example.misdaqia.Model.MainCategoryResponse;
import com.example.misdaqia.Model.registerUserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {


    @FormUrlEncoded
    @POST("register")
    Call<registerUserResponse> createUser(
            @Field("name") String Name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("c_password") String re_password);

    @FormUrlEncoded
    @POST("login")
    Call<LoginUserResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password);

    @GET("getCategoryApi ")
    Call<MainCategoryResponse> getCategories();

    @GET("getMazadApi")
    Call<MainCategoryResponse> getMazadat();


}