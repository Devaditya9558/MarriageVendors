package com.example.marriagevendors.Services;

import com.example.marriagevendors.Model.LoginRequest;
import com.example.marriagevendors.Model.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServices {
    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
}