package com.plabon.parkingadmin.activity.ui.setprice;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.plabon.parkingadmin.R;
import com.plabon.parkingadmin.activity.ui.newuser.NewuserViewModel;
import com.plabon.parkingadmin.adapter.PriceAdapter;
import com.plabon.parkingadmin.api.CarTypeApiInterface;
import com.plabon.parkingadmin.api.DelCarApiInterface;
import com.plabon.parkingadmin.api.DelCarPriceApiInterface;
import com.plabon.parkingadmin.api.GetPriceListApiInterface;
import com.plabon.parkingadmin.api.SetCarTypeApiInterface;
import com.plabon.parkingadmin.api.SetPriceApiInterface;
import com.plabon.parkingadmin.model.PriceList;
import com.plabon.parkingadmin.model.User;
import com.plabon.parkingadmin.utill.LocalStorage.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class SetPriceFragment extends Fragment {

    private SetPriceViewModel priceViewModel;
    Spinner carttypeSpinner,loadtypeSpinner;
    View progress;
    LocalStorage localStorage;
    User user;
    Gson gson;
    private ArrayList<String> cartypeString = new ArrayList<String>();
    ArrayList<PriceList> pList;
    Button btnCarSubmit;
    EditText editTextPrice;
    private RecyclerView recyclerView;
    private PriceAdapter mAdapter;
    private List<PriceList> orderList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        priceViewModel =
                ViewModelProviders.of(this).get(SetPriceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setprice, container, false);

        editTextPrice = root.findViewById(R.id.edittextPrice);
        btnCarSubmit = root.findViewById(R.id.btnCarSubmit);
        recyclerView = root.findViewById(R.id.rv_price_set);

        pList=new ArrayList<>();

        priceViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        cartypeString.add("Select Car Type");
        carttypeSpinner = root.findViewById(R.id.spinner_cartype);
        loadtypeSpinner = root.findViewById(R.id.spinner_loadtype);
        progress = root.findViewById(R.id.progress_bar);

        String [] values =
                {"Select Load Type","Loaded","Empty","LoadUnload"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        loadtypeSpinner.setAdapter(adapter);

        getBanner();

        btnCarSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextPrice.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Enter Price",Toast.LENGTH_LONG).show();
                }else if(carttypeSpinner.getSelectedItem().toString().equals("Select Car Type")){
                    Toast.makeText(getContext(),"Select Car Type",Toast.LENGTH_LONG).show();
                }else if(loadtypeSpinner.getSelectedItem().toString().equals("Select Load Type")){
                    Toast.makeText(getContext(),"Select Car Load Type",Toast.LENGTH_LONG).show();
                }else{
                    setPrice(carttypeSpinner.getSelectedItem().toString(),loadtypeSpinner.getSelectedItem().toString(),editTextPrice.getText().toString());
                }
            }
        });

        getPriceList();

        return root;
    }

    @Override
    public void onResume() {
        pList.clear();
        getPriceList();
        super.onResume();
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
//Toast.makeText(getContext(),jsonresponse,Toast.LENGTH_LONG).show();
                            JSONArray dataArray  = obj.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);
                                //   Toast.makeText(getContext(),"i" + dataobj.getString("bannar").toString(),Toast.LENGTH_SHORT).show();

                                cartypeString.add(dataobj.getString("type"));//"https://iplabon.in/canteen/assets/images/ProductImage/category/);

                            }

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

    private void initSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, cartypeString);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        carttypeSpinner.setAdapter(adapter);
    }

    private void setPrice(String s,String lt,String p){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SetPriceApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        SetPriceApiInterface api = retrofit.create(SetPriceApiInterface.class);

        Call<String> call = api.getString(s,lt,p);

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

                           // Toast.makeText(getContext(),"Result " + jsonresponse,Toast.LENGTH_LONG).show();
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


    private void getPriceList(){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetPriceListApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        GetPriceListApiInterface api = retrofit.create(GetPriceListApiInterface.class);

        Call<String> call = api.getString("s");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Toast.makeText(getContext(),"Strint--retro",Toast.LENGTH_LONG).show();

                //  Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        hideProgressDialog();
                        //    Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        try {
                            JSONObject obj = new JSONObject(jsonresponse);
                            //JSONArray dataArray= new JSONArray(obj.getJSONArray("data"));
                            JSONArray dataArray  = obj.getJSONArray("data");
                            if (pList.isEmpty()){

                            }else{
                                pList.clear();
                            }

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);
                                //   Toast.makeText(getContext(),"i" + dataobj.getString("bannar").toString(),Toast.LENGTH_SHORT).show();

                                pList.add(new PriceList(
                                        dataobj.getString("id"),
                                        dataobj.getString("cartype"),
                                        dataobj.getString("loadtype"),
                                        dataobj.getString("price")
                                ));


                            }
                            setupOrderRecycleView();
                            //Toast.makeText(getContext(),"Result " + obj,Toast.LENGTH_LONG).show();
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

    private void setupOrderRecycleView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
        recyclerView.setLayoutManager(rvLiLayoutManager);

        PriceAdapter adapter = new PriceAdapter(pList,getContext(),SetPriceFragment.this);

        recyclerView.setAdapter(adapter);

  /*      mAdapter = new PriceAdapter(pList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);*/

    }

/*    public void showAlertYes(String msg){
        //  AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext(),R.style.myDialog);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              //  setCarEntry(carnumber.getText().toString(),spinnerCarType.getSelectedItem().toString(),mobile.getText().toString(),date.getText().toString(),time.getText().toString(),uid,spinnerCarLoadType.getSelectedItem().toString());
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
    }*/


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

    public void delCar(String s){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DelCarPriceApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        DelCarPriceApiInterface api = retrofit.create(DelCarPriceApiInterface.class);

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

                            getPriceList();

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

}