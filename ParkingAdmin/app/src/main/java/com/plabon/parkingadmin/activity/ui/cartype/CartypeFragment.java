package com.plabon.parkingadmin.activity.ui.cartype;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.plabon.parkingadmin.R;
import com.plabon.parkingadmin.activity.ui.newuser.NewuserFragment;
import com.plabon.parkingadmin.activity.ui.newuser.NewuserViewModel;
import com.plabon.parkingadmin.adapter.CarTypeAdapter;
import com.plabon.parkingadmin.adapter.UserAdapter;
import com.plabon.parkingadmin.api.CarTypeApiInterface;
import com.plabon.parkingadmin.api.DelCarApiInterface;
import com.plabon.parkingadmin.api.DelUserApiInterface;
import com.plabon.parkingadmin.api.GetUserApiInterface;
import com.plabon.parkingadmin.api.SetCarTypeApiInterface;
import com.plabon.parkingadmin.model.CarTypeList;
import com.plabon.parkingadmin.model.ParkingUser;
import com.plabon.parkingadmin.model.User;
import com.plabon.parkingadmin.utill.ConnectionClass;
import com.plabon.parkingadmin.utill.LocalStorage.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class CartypeFragment extends Fragment {

    private CartypeViewModel newuserViewModel;
    View progress;
    LocalStorage localStorage;
    User user;
    Gson gson;
    ArrayList<CarTypeList> pList;
    RecyclerView rv;

    EditText caartype,crtypedes;
    Button btnCarSubmit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newuserViewModel =
                ViewModelProviders.of(this).get(CartypeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cartype, container, false);

        caartype = root.findViewById(R.id.cartype);
        crtypedes = root.findViewById(R.id.cartyprdes);
        btnCarSubmit = root.findViewById(R.id.btnCarSubmit);
        progress = root.findViewById(R.id.progress_bar);
        rv = root.findViewById(R.id.rv);
        pList=new ArrayList<>();
        newuserViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        //Toast.makeText(getContext(),"error in RS",Toast.LENGTH_LONG).show();

/*
        Connection con = ConnectionClass.CONN();

        if(con==null)
        {
            String msg="Error in SQL Connection";
            Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
        }
        else
        {

            try {
                Toast.makeText(getContext(),"try",Toast.LENGTH_LONG).show();
                ResultSet rs;
                Statement st;

                st = con.createStatement();

                // set the result in result set then fetch it
                rs = st.executeQuery("Select * From cartype");

                if (rs.next()){
                    Toast.makeText(getActivity()," EmpId :. " + rs.getString("cartype")+ " etc..",Toast.LENGTH_LONG).show();

                }else{
                    // you have no record
                    Toast.makeText(getContext(),"error in RS",Toast.LENGTH_LONG).show();

                }


            }catch (Exception e){
                Toast.makeText(getContext(),"error in e " +e.toString(),Toast.LENGTH_LONG).show();
            }
        }

*/


        btnCarSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (caartype.getText().toString().equals("") || crtypedes.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Enter All Details",Toast.LENGTH_LONG).show();
                }else{
                    getBanner(caartype.getText().toString(),crtypedes.getText().toString());
                }
            }
        });


getCarType();

        return root;
    }


    private void getBanner(String s,String sd){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SetCarTypeApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        SetCarTypeApiInterface api = retrofit.create(SetCarTypeApiInterface.class);

        Call<String> call = api.getString(s,sd);

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
                            //Toast.makeText(getContext(),"Result " + jsonresponse,Toast.LENGTH_LONG).show();
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

    private void hideProgressDialog() {
        progress.setVisibility(View.GONE);
    }

    private void showProgressDialog() {
        progress.setVisibility(View.VISIBLE);
    }

    private void getCarType(){
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

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        hideProgressDialog();
                        //    Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        try {
                            JSONObject obj = new JSONObject(jsonresponse);
                            //showAlert(obj.getString("status"));
                             // Toast.makeText(getContext(),"Result " + jsonresponse,Toast.LENGTH_LONG).show();
                            JSONArray dataArray  = obj.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);
                                //   Toast.makeText(getContext(),"i" + dataobj.getString("bannar").toString(),Toast.LENGTH_SHORT).show();

                                pList.add(new CarTypeList(dataobj.getString("id"),
                                        dataobj.getString("type"),
                                        dataobj.getString("des")));//"https://iplabon.in/canteen/assets/images/ProductImage/category/);

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

    public void delCar(String s){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DelCarApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        DelCarApiInterface api = retrofit.create(DelCarApiInterface.class);

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

                            getCarType();

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
                delCar(sids);
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

        CarTypeAdapter adapter = new CarTypeAdapter(pList,getContext(), CartypeFragment.this);

        rv.setAdapter(adapter);
    }
}