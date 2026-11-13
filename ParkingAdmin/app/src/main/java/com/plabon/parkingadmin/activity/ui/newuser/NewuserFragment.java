package com.plabon.parkingadmin.activity.ui.newuser;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plabon.parkingadmin.R;
import com.plabon.parkingadmin.adapter.CarAdapter;
import com.plabon.parkingadmin.adapter.UserAdapter;
import com.plabon.parkingadmin.api.DelUserApiInterface;
import com.plabon.parkingadmin.api.GetUserApiInterface;
import com.plabon.parkingadmin.api.SetPriceApiInterface;
import com.plabon.parkingadmin.api.SetUserApiInterface;
import com.plabon.parkingadmin.model.CarList;
import com.plabon.parkingadmin.model.ParkingUser;
import com.plabon.parkingadmin.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class NewuserFragment extends Fragment {

    private NewuserViewModel newuserViewModel;
    View progress;
    EditText editTextName,editTextMobile,editTextDOB,editTextPass;
    Button btnCarSubmit;
    ArrayList<ParkingUser> pList;
    RecyclerView rv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newuserViewModel =
                ViewModelProviders.of(this).get(NewuserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_newuser, container, false);
        pList=new ArrayList<>();
        newuserViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });


        progress = root.findViewById(R.id.progress_bar);
        editTextDOB = root.findViewById(R.id.user_date_of_birth);
        editTextMobile = root.findViewById(R.id.user_mobile);
        editTextName = root.findViewById(R.id.user_name);
        editTextPass = root.findViewById(R.id.user_password);
        btnCarSubmit = root.findViewById(R.id.btnCarSubmit);
        rv = root.findViewById(R.id.rv);

        btnCarSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextDOB.getText().toString().equals("") || editTextMobile.getText().toString().equals("") || editTextName.getText().toString().equals("") || editTextPass.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Set All Data",Toast.LENGTH_LONG).show();
                }else{
                    setPrice(editTextName.getText().toString(),editTextMobile.getText().toString(),editTextPass.getText().toString(),editTextDOB.getText().toString());
                }
            }
        });

getUser();
        return root;
    }

    private void setPrice(String name,String mobile,String pass,String dob){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SetUserApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        SetUserApiInterface api = retrofit.create(SetUserApiInterface.class);

        Call<String> call = api.getString(name,mobile,pass,dob);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        hideProgressDialog();
                        //    Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        try {
                            JSONObject obj = new JSONObject(jsonresponse);
                            showAlert(obj.getString("status"));
                          //  Toast.makeText(getContext(),"Result " + jsonresponse,Toast.LENGTH_LONG).show();
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

    private void hideProgressDialog() {
        progress.setVisibility(View.GONE);
    }

    private void showProgressDialog() {
        progress.setVisibility(View.VISIBLE);
    }

    public void showAlert(String msg){
        //  AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext(),R.style.myDialog);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.myDialog));
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


    private void getUser(){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetUserApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        GetUserApiInterface api = retrofit.create(GetUserApiInterface.class);

        Call<String> call = api.getString("s");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        hideProgressDialog();
                        //    Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        try {
                            JSONObject obj = new JSONObject(jsonresponse);
                            //showAlert(obj.getString("status"));
                            //  Toast.makeText(getContext(),"Result " + jsonresponse,Toast.LENGTH_LONG).show();
                            JSONArray dataArray  = obj.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);
                                //   Toast.makeText(getContext(),"i" + dataobj.getString("bannar").toString(),Toast.LENGTH_SHORT).show();

                                pList.add(new ParkingUser(dataobj.getString("id"),
                                        dataobj.getString("name"),
                                        dataobj.getString("dob"),
                                        dataobj.getString("p"),
                                        dataobj.getString("mobile")));//"https://iplabon.in/canteen/assets/images/ProductImage/category/);

                            }
                            setupOrderRecycleView();
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

    public void delUser(String s){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DelUserApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        DelUserApiInterface api = retrofit.create(DelUserApiInterface.class);

        Call<String> call = api.getString(s);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // hideProgressDialog();
                        //    Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        try {
                            JSONObject obj = new JSONObject(jsonresponse);
                            showAlert(obj.getString("status"));
                            //  Toast.makeText(getContext(),"Result " + jsonresponse,Toast.LENGTH_LONG).show();
                           // JSONArray dataArray  = obj.getJSONArray("data");
                            if (pList.isEmpty()){

                            }else{
                                pList.clear();
                            }

                            getUser();

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
                // hideProgressDialog();
            }
        });
    }

    public void showDelAlertYes(String msg, final String sids){
        //  AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext(),R.style.myDialog);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.myDialog));
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //setCarEntry(carnumber.getText().toString(),spinnerCarType.getSelectedItem().toString(),mobile.getText().toString(),date.getText().toString(),time.getText().toString(),uid,spinnerCarLoadType.getSelectedItem().toString());
                //dialogInterface.dismiss();
                delUser(sids);
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

    private void setupOrderRecycleView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
        rv.setLayoutManager(rvLiLayoutManager);

        UserAdapter adapter = new UserAdapter(pList,getContext(),NewuserFragment.this);

        rv.setAdapter(adapter);
    }
}