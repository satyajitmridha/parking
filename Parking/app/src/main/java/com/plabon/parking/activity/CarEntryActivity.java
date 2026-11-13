package com.plabon.parking.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.plabon.parking.R;
import com.plabon.parking.api.CarTypeApiInterface;
import com.plabon.parking.api.SetCarEntryApiInterface;
import com.plabon.parking.model.User;
import com.plabon.parking.utill.LocalStorage.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CarEntryActivity extends AppCompatActivity {

    EditText carnumber,carnumbercheck,mobile,date,time;
    Button btnSubmit,btnPrint;
    View progress;
    List<String> spinnerLables = new ArrayList<String>();
    private ArrayList<String> cartypeString = new ArrayList<String>();
    Spinner spinnerCarType,spinnerCarLoadType;
    String carType="",carLoadType="",uid="";

    User user;
    Gson gson;
    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_entry);

        carnumber = findViewById(R.id.entry_carnumber);
        carnumbercheck = findViewById(R.id.entry_carnumber_check);
        date = findViewById(R.id.car_entry_date);
        time = findViewById(R.id.car_entry_time);
        mobile = findViewById(R.id.car_entry_mobile);
        btnSubmit = findViewById(R.id.btnCarSubmit);
        progress = findViewById(R.id.progress_bar);
        spinnerCarType = findViewById(R.id.cart_type_spinner);
        spinnerCarLoadType = findViewById(R.id.car_load_type_spinner);
        btnPrint = findViewById(R.id.btnPrint);
        spinnerLables.add("Select Car Type");
        cartypeString.add("Select Car Type");

        localStorage = new LocalStorage(getApplicationContext());
        gson = new Gson();

        //localStorage = new LocalStorage(getContext());
        user = gson.fromJson(localStorage.getUserLogin(), User.class);
        uid = user.getId();

        Date d = new Date();
        CharSequence s  = DateFormat.format("yyyy-MM-d", d.getTime());

        date.setText(s);

        s  = DateFormat.format("HH:mm:ss", d.getTime());
        time.setText(s);

        getBanner();

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carnumber.getText().toString().equals("")){
                    showAlert("Enter Car Number");
                    vibrate(200);
                }else {
                    Intent intent = new Intent(getApplicationContext(), PrintActivity.class);
                    intent.putExtra("NO", carnumber.getText().toString());
                    startActivity(intent);
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carnumber.getText().toString().isEmpty() || carnumbercheck.getText().toString().isEmpty()){
                    vibrate(200);
                    showAlert("Enter Car Number");
                    //Toast.makeText(getApplicationContext(), "Enter Car Number", Toast.LENGTH_LONG).show();
                }else {
                    if(spinnerCarLoadType.getSelectedItem().toString().equals("Select Car Load Type")){
                        vibrate(200);
                        showAlert("Select Car Load Type");
                    }else {
                        if(spinnerCarType.getSelectedItem().toString().equals("Select Car Type")) {
                            vibrate(200);
                            showAlert("Select Car Type");
                        }else {
                            if (carnumber.getText().toString().equals(carnumbercheck.getText().toString().trim())) {
                                //setCarEntry(carnumber.getText().toString(),spinnerCarType.getSelectedItem().toString(),mobile.getText().toString(),date.getText().toString(),time.getText().toString(),uid,spinnerCarLoadType.getSelectedItem().toString());

showAlertYes("Confirm");

                            } else {
                                vibrate(200);
                                showAlert("Car Number not Match");
                                //Toast.makeText(getApplicationContext(), "Check Car Number", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        });
    }


    public void vibrate(int duration) {
        Vibrator vibs = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibs.vibrate(duration);
    }


    private void hideProgressDialog() {
        progress.setVisibility(View.GONE);
    }

    private void showProgressDialog() {
        progress.setVisibility(View.VISIBLE);
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

    public void showAlertYes(String msg){
        //  AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext(),R.style.myDialog);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setCarEntry(carnumber.getText().toString(),spinnerCarType.getSelectedItem().toString(),mobile.getText().toString(),date.getText().toString(),time.getText().toString(),uid,spinnerCarLoadType.getSelectedItem().toString());
                //dialogInterface.dismiss();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }



    private void getBanner(){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CarTypeApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        CarTypeApiInterface api = retrofit.create(CarTypeApiInterface.class);

        Call<String> call = api.getString("s");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //  Toast.makeText(MainActivity.this,"Strint--retro",Toast.LENGTH_LONG).show();

                //  Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //    Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        try {
                            JSONObject obj = new JSONObject(jsonresponse);

                            JSONArray dataArray  = obj.getJSONArray("car_json_data");

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);
                                //   Toast.makeText(getContext(),"i" + dataobj.getString("bannar").toString(),Toast.LENGTH_SHORT).show();

                                cartypeString.add(dataobj.getString("type"));//"https://iplabon.in/canteen/assets/images/ProductImage/category/);

                            }

                            //initSpinner();
                            initSpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        hideProgressDialog();
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



    public void initSpinner(){
        String [] values =
                {"Select Car Load Type","Loaded","Empty","LoadUnload"};
        spinnerLables.add("Two Whiller");
        spinnerLables.add("Ten");
        spinnerLables.add("Truck");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cartypeString);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCarType.setAdapter(adapter);

        ArrayAdapter<String> adapterL = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        adapterL.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCarLoadType.setAdapter(adapterL);
    }


    private void setCarEntry(String cno,String ctype,String mobil,String indate,String intime,String entryby,String lttype){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SetCarEntryApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        SetCarEntryApiInterface api = retrofit.create(SetCarEntryApiInterface.class);

        Call<String> call = api.getString(cno,ctype,mobil,indate,intime,entryby,lttype);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //  Toast.makeText(MainActivity.this,"Strint--retro",Toast.LENGTH_LONG).show();

                //  Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //    Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        try {
                            JSONObject obj = new JSONObject(jsonresponse);
                            showAlert(obj.getString("status"));
                           /* JSONArray dataArray  = obj.getJSONArray("status");

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);
                                //   Toast.makeText(getContext(),"i" + dataobj.getString("bannar").toString(),Toast.LENGTH_SHORT).show();

                                cartypeString.add(dataobj.getString("type"));//"https://iplabon.in/canteen/assets/images/ProductImage/category/);

                            }*/

                            Toast.makeText(getApplicationContext(),"Result " + obj.getString("status"),Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        hideProgressDialog();
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

}