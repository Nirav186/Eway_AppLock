package com.nirav.applock.Retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServices {


    @GET("/API.aspx?")
    Call<String> loginUser(@Query("user") String userid, @Query("pass") String password);


    @FormUrlEncoded
    @POST("API.aspx?")
    Call<String> doCreateUserWithField(@Field("ID") String userid,@Field("pass") String password, @Field("NAME") String name,@Field("MobileNo") String mobileno,@Field("EmailID") String emailId);

}

