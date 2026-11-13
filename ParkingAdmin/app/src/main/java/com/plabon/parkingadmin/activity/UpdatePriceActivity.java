package com.plabon.parkingadmin.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.plabon.parkingadmin.R;
import com.plabon.parkingadmin.api.SetPriceApiInterface;
import com.plabon.parkingadmin.api.SetPriceUpdateApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UpdatePriceActivity extends AppCompatActivity {

    TextView ct,lt;
    EditText price;
    Button btnUpdate;
    View progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_price);
        ct = findViewById(R.id.ctype);
        lt= findViewById(R.id.ltype);
        price = findViewById(R.id.edittextPrice);
        btnUpdate = findViewById(R.id.btnCarSubmit);
        progress = findViewById(R.id.progress_bar);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("ID");
        String cts = intent.getStringExtra("CT");
        String lts = intent.getStringExtra("CL");

        ct.setText("  Car Type : " + cts.toString());
        lt.setText("  Load Type : " + lts.toString());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (price.getText().toString().equals("")){
                    showAlert("Enter Price");
                }else{
                    setPrice(id,price.getText().toString());
                }
            }
        });

    }

    private void setPrice(String s,String p){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SetPriceUpdateApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        SetPriceUpdateApiInterface api = retrofit.create(SetPriceUpdateApiInterface.class);

        Call<String> call = api.getString(s,p);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //  Toast.makeText(MainActivity.this,"Strint--retro",Toast.LENGTH_LONG).show();

                //  Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        hideProgressDialog();
                        //    Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        try {
                            JSONObject obj = new JSONObject(jsonresponse);
                            showAlert(obj.getString("status"));
                            //showAlert(date);
                           // Toast.makeText(getApplicationContext(),"Update " + date,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //  Toast.makeText(MainActivity.this,"Strint--" + jsonresponse,Toast.LENGTH_LONG).show();
                        //writeTv(jsonresponse);

                    } else {
                        hideProgressDialog();
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideProgressDialog();
            }
        });
    }
    public void showAlert(String msg){
        //  AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext(),R.style.myDialog);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void hideProgressDialog() {
        progress.setVisibility(View.GONE);
    }

    private void showProgressDialog() {
        progress.setVisibility(View.VISIBLE);
    }
}