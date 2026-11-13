package com.plabon.parking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.plabon.parking.MainActivity;
import com.plabon.parking.R;
import com.plabon.parking.api.clients.RestClient;
import com.plabon.parking.model.User;
import com.plabon.parking.model.UserResult;
import com.plabon.parking.utill.LocalStorage.LocalStorage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout btnLogin;
    User user;
    Gson gson;
    EditText editTextMobile,editTextPassword;
    View progress;
    LocalStorage localStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnContinue);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        editTextMobile = findViewById(R.id.et_login_email);
        editTextPassword = findViewById(R.id.et_login_pass);
        progress = findViewById(R.id.progress_bar);

        localStorage = new LocalStorage(getApplicationContext());
        gson = new Gson();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
               // Intent startmain = new Intent(getApplicationContext(), DashBoardActivity.class);
               // startActivity(startmain);
            }
        });

        if (localStorage.isUserLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
            finish();
        }else{

        }
    }

    private void checkValidation() {
        // Get email id and password
        final String getMobile = editTextMobile.getText().toString();
        final String getPassword = editTextPassword.getText().toString();

        // Check for both field is empty or not
        if (getMobile.equals("") || getMobile.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            Toast.makeText(getApplicationContext(),"Enter All Valid Data",Toast.LENGTH_LONG).show();
            vibrate(200);
        } else {
            user = new User(getMobile, getPassword);
            login(user);
        }
    }

    private void login(User user) {
        showProgressDialog();
        Call<UserResult> call = RestClient.getRestService(getApplicationContext()).login(user);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.d("Response Login:=>", response.body() + "");
                if (response != null) {

                    UserResult userResult = response.body();
                    if (userResult.getCode() == 200) {
                        // Toast.makeText(getApplicationContext(), gson.toJson(userResult.getUser()), Toast.LENGTH_LONG).show();
                        String userString = gson.toJson(userResult.getUser());
                        hideProgressDialog();
                        localStorage.createUserLoginSession(userString);
                        // Toast.makeText(getContext(), userString, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), userResult.getStatus(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                        finish();
                    } else {
                        hideProgressDialog();
                        Toast.makeText(getApplicationContext(), userResult.getStatus(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "Incorrect Data", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Log.d("Error==> ", t.getMessage());

            }
        });
    }

    private void hideProgressDialog() {
        progress.setVisibility(View.GONE);
    }

    private void showProgressDialog() {
        progress.setVisibility(View.VISIBLE);
    }
    public void vibrate(int duration) {
        Vibrator vibs = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibs.vibrate(duration);
    }



}