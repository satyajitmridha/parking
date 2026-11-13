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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.plabon.parking.R;
import com.plabon.parking.api.GetCarApiInterface;
import com.plabon.parking.api.SetCarEntryApiInterface;
import com.plabon.parking.api.SetCarExitApiInterface;
import com.plabon.parking.model.User;
import com.plabon.parking.utill.LocalStorage.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CarExitActivity extends AppCompatActivity {

    TextView btnFind,textview_in_date,textview_car_type,textview_car_load_type,textview_out_date_time,textview_total_amount;
    EditText entry_carnumber;
    Button btnCarSubmit,btnPrint;
    View progress;
    String id="",uid="",cno="",indate="",intime="",od="",otime="",ctype="",ltype="",Amount="",date_difference="",rate="",dc="";
    User user;
    Gson gson;
    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_exit);
        progress = findViewById(R.id.progress_bar);
        btnFind = findViewById(R.id.carFindExit);
        entry_carnumber = findViewById(R.id.entry_carnumber);
        textview_in_date = findViewById(R.id.textview_in_date);
        textview_car_type = findViewById(R.id.textview_car_type);
        textview_car_load_type = findViewById(R.id.textview_car_load_type);
        textview_out_date_time = findViewById(R.id.textview_out_date_time);
        textview_total_amount = findViewById(R.id.textview_total_amount);
        btnCarSubmit = findViewById(R.id.btnCarSubmit);
        btnPrint = findViewById(R.id.btnPrint);

        localStorage = new LocalStorage(getApplicationContext());
        gson = new Gson();

        //localStorage = new LocalStorage(getContext());
        user = gson.fromJson(localStorage.getUserLogin(), User.class);
        uid = user.getId();
//Toast.makeText(getApplicationContext(),user.getId(),Toast.LENGTH_LONG).show();
        //s  = DateFormat.format("HH:mm:ss", d.getTime());

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(entry_carnumber.getText().toString().equals("") || entry_carnumber.getText().toString().length()<6){
                    showAlert("Enter Valid Car Number");
                    vibrate(200);
                }else{

                    getCar(entry_carnumber.getText().toString());
                    setDateTime();
                }
                //showAlert("find");
                //showProgressDialog();
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(entry_carnumber.getText().toString().equals("") || entry_carnumber.getText().toString().length()<6){
                    showAlert("Enter Valid Car Number");
                    vibrate(200);
                }else{
                    Intent intent = new Intent(getApplicationContext(), PrintActivity.class);
                    intent.putExtra("NO", entry_carnumber.getText().toString());
                    startActivity(intent);
                }
                //showAlert("find");
                //showProgressDialog();
            }
        });

        btnCarSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.equals("")){
                    showAlert("No Car Found");
                    vibrate(200);
                }else{


                    setCarExit(id,uid,Amount,od,otime,dc);
                }
            }
        });
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
    private void setCarExit(String id,String uid,String tamount,String odd,String otimm,String dcc){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SetCarExitApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        SetCarExitApiInterface api = retrofit.create(SetCarExitApiInterface.class);

        Call<String> call = api.getString(id,uid,tamount,odd,otimm,dcc);

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

                          //  Toast.makeText(getApplicationContext(),"Result " + jsonresponse,Toast.LENGTH_LONG).show();

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
    private void getCar(String ccno){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetCarApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        GetCarApiInterface api = retrofit.create(GetCarApiInterface.class);

        Call<String> call = api.getString(ccno);

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

                            JSONArray dataArray  = obj.getJSONArray("car");
                            indate="";intime="";id="";
                            if (dataArray.length()>0) {
                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    //   Toast.makeText(getContext(),"i" + dataobj.getString("bannar").toString(),Toast.LENGTH_SHORT).show();

                                    id = (dataobj.getString("id"));//"https://iplabon.in/canteen/assets/images/ProductImage/category/);
                                    cno = (dataobj.getString("carnumber"));
                                    indate = (dataobj.getString("indate"));
                                    intime = (dataobj.getString("intimestamp"));
                                    ltype = (dataobj.getString("loadtype"));
                                    ctype = (dataobj.getString("cartype"));
                                    date_difference = (dataobj.getString("date_difference"));
                                    rate = (dataobj.getString("rate"));
                                }
                                textview_in_date.setText("  IN DATE :- " + indate + "  " + intime);
                                textview_car_type.setText("  CAR TYPE :- " + ctype );
                                textview_car_load_type.setText("  LOAD TYPE :- " + ltype );
                                if (date_difference.equals("0")){
                                    textview_total_amount.setText("  Payable Amunt :- " + rate);
                                    Amount = rate;
                                    dc=String.valueOf((Integer.parseInt(date_difference)+1));
                                }else{
                                    Amount = String.valueOf((Integer.parseInt(date_difference) * Integer.parseInt(rate)) + Integer.parseInt(rate));
                                    textview_total_amount.setText("  Payable Amunt :- " + Amount);
                                    dc=String.valueOf((Integer.parseInt(date_difference)+1));
                                }
                                //  Toast.makeText(getApplicationContext(),"Result " + jsonresponse,Toast.LENGTH_LONG).show();
                            }else{
                                showAlert("Car Not Found");
                                textview_in_date.setText("");
                                textview_car_type.setText("  CAR TYPE :- "  );
                                textview_car_load_type.setText("  LOAD TYPE :- "  );
                                textview_total_amount.setText("");
                                vibrate(200);
                            }
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
private void setDateTime(){
    Date d = new Date();
    CharSequence s  = DateFormat.format("yyyy-MM-d", d.getTime());
    CharSequence t  =  DateFormat.format("HH:mm:ss", d.getTime());
    textview_out_date_time.setText("  OUT DATE :- " + s + " " + t);
    od= s.toString();
    otime = t.toString();
}
    public void vibrate(int duration) {
        Vibrator vibs = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibs.vibrate(duration);
    }


}