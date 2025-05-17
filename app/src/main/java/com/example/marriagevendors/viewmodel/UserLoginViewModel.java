package com.example.marriagevendors.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.marriagevendors.UserRepository;
import com.example.marriagevendors.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginViewModel extends ViewModel {
    private UserRepository repository;
    private MutableLiveData<LoginResponse> loginResult = new MutableLiveData<>();

    public UserLoginViewModel() {
        repository = new UserRepository();
    }

    public LiveData<LoginResponse> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        repository.login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loginResult.setValue(response.body());
                } else {
                    LoginResponse errorResponse = new LoginResponse();
                    errorResponse.setSuccess(false); // Use setter instead of direct field access
                    errorResponse.setMessage("Login failed. Please try again."); // Use setter
                    loginResult.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                LoginResponse errorResponse = new LoginResponse();
                errorResponse.setSuccess(false); // Use setter instead of direct field access
                errorResponse.setMessage("Network error: " + t.getMessage()); // Use setter
                loginResult.setValue(errorResponse);
            }
        });
    }
}