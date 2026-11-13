package com.plabon.parking.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plabon.parking.MainActivity;
import com.plabon.parking.R;
import com.plabon.parking.api.CarTypeApiInterface;
import com.plabon.parking.api.GetCollectionApiInterface;
import com.plabon.parking.model.User;
import com.plabon.parking.utill.LocalStorage.LocalStorage;

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

public class DashBoardActivity extends AppCompatActivity {
    CardView btnNewCar,btnExitCar;
    TextView textViewlogin,texview_logout,pd,d;
    ImageView print;
    View progress;
    String pcol="",dcol="",uid="";
    private Context mContext;
    private PopupWindow mPopupWindow;
    private Activity mActivity;
    private RelativeLayout mRelativeLayout;

    User user;
    Gson gson;
    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        btnNewCar = findViewById(R.id.btnNewEntry);
        btnExitCar = findViewById(R.id.btnExit);
        textViewlogin = findViewById(R.id.texview_login);
        progress = findViewById(R.id.progress_bar);
        texview_logout = findViewById(R.id.texview_logout);
        d = findViewById(R.id.d);
        pd = findViewById(R.id.pd);
        print = findViewById(R.id.print);

        mContext = getApplicationContext();

        // Get the activity
        mActivity = DashBoardActivity.this;

        localStorage = new LocalStorage(getApplicationContext());
        gson = new Gson();

        user = gson.fromJson(localStorage.getUserLogin(), User.class);
        textViewlogin.setText("Login as - " + user.getId() + ", " + user.getName() + ", " + user.getMobile());

        uid = user.getId();
        getCollection();
        btnNewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startmain = new Intent(getApplicationContext(), CarEntryActivity.class);
                startActivity(startmain);
            }
        });

        btnExitCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startmain = new Intent(getApplicationContext(), CarExitActivity.class);
                startActivity(startmain);
            }
        });
        texview_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrintActivity.class);
                intent.putExtra("NO", "0");
                startActivity(intent);
            }
        });
        textViewlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  pop();
                showOptions(DashBoardActivity.this);
            }
        });
    }

    private void getCollection(){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetCollectionApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        GetCollectionApiInterface api = retrofit.create(GetCollectionApiInterface.class);

        Call<String> call = api.getString(uid);

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

                            //dcol=obj.getString("d");
                            //pcol=obj.getString("pd");
                            JSONArray dataArray  = obj.getJSONArray("d");
                            JSONObject dataobj = dataArray.getJSONObject(0);
                            dcol=dataobj.getString("dcol");
                            JSONArray dataArraypd  = obj.getJSONArray("pd");
                            JSONObject dataobjpd = dataArraypd.getJSONObject(0);
                            pcol=dataobjpd.getString("dcol");

                            /*for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);
                                //   Toast.makeText(getContext(),"i" + dataobj.getString("bannar").toString(),Toast.LENGTH_SHORT).show();

                                //cartypeString.add(dataobj.getString("type"));//"https://iplabon.in/canteen/assets/images/ProductImage/category/);

                            }*/

                            d.setText("Rs " + dcol);
                            pd.setText("Rs "+pcol);

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

    private void logOut(){
        localStorage.logoutUser();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }


    public void ShowPop(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        //LayoutInflater inflater = ((LayoutInflater) getApplicationContext()).getLayoutInflater();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.pop,
                null);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setView(dialogLayout, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        WindowManager.LayoutParams wlmp = dialog.getWindow()
                .getAttributes();
        wlmp.gravity = Gravity.BOTTOM;


        Button btnCamera = (Button) dialogLayout.findViewById(R.id.cng);



        builder.setView(dialogLayout);

        dialog.show();
    }

    private void pop(){

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.pop,null);


        mPopupWindow = new PopupWindow(
                customView,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
    }

    private PopupWindow showOptions(Context mcon){
        try{
            LayoutInflater inflater = (LayoutInflater) mcon.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.pop,null);
            final PopupWindow optionspu = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            optionspu.setAnimationStyle(R.style.popup_window_animation);
            optionspu.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            optionspu.setFocusable(true);
            optionspu.setOutsideTouchable(true);
            optionspu.update(0, 0, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            optionspu.showAtLocation(layout, Gravity.BOTTOM, 0, 0);

            TextView btnCng;
            final EditText pas;
            btnCng = layout.findViewById(R.id.cng);
            pas = layout.findViewById(R.id.pas);

            btnCng.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pas.getText().toString().equals("")){
                        showAlert("Enter New Password");
                    }else{
                        showAlert("Enter New Password");
                       // optionspu.dismiss();
                    }

                }
            });

            return optionspu;
        }
        catch (Exception e){e.printStackTrace();
            return null;}
    }
}