package com.nirav.applock.activities.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nirav.applock.R;
import com.nirav.applock.Retrofit.ApiClient;
import com.nirav.applock.Retrofit.ApiServices;
import com.nirav.applock.activities.pwd.CreatePwdActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText editUserid;
    TextInputEditText editPass;

    TextInputLayout userId;
    TextInputLayout password;

    RelativeLayout btnLogin;

    ApiServices apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();
    }

    private void initview() {
        editUserid = findViewById(R.id.editUserid);
        editPass = findViewById(R.id.editPass);
        btnLogin = findViewById(R.id.btnLogin);
        userId = findViewById(R.id.userId);
        password = findViewById(R.id.password);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editUserid.getText().toString().trim().equals("")){
                    userId.setError("Userid can not be empty");
                }else if (editPass.getText().toString().trim().equals("")){
                    password.setError("Password can not be empty");
                }else {
                    doLogin(editUserid.getText().toString().trim(), editPass.getText().toString().trim());
                }
            }
        });


        editUserid.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!TextUtils.isEmpty(editUserid.getText().toString().trim())) {
                    userId.setError(null);
                }
                else{
                    userId.setError("Userid can not be empty");
                }
            }
        });

        editPass.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!TextUtils.isEmpty(editPass.getText().toString().trim())) {
                    password.setError(null);
                }
                else{
                    password.setError("Password can not be empty");
                }
            }
        });
    }

    private void doLogin(String id, String password) {
        apiInterface = ApiClient.getClient().create(ApiServices.class);
        Call<String> call = apiInterface.loginUser(id, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseData = null;
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e("TAG243", "heffjn" + response.body().toString());
                        Log.e("TAG243", "heffjn" + response.message());

                        if (response.body().startsWith("fail")) {
                            Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            finish();
                        }
                    } else {
                        Log.e("TAG243", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG243", "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "onFailure ", Toast.LENGTH_SHORT).show();
            }

        });
    }
}