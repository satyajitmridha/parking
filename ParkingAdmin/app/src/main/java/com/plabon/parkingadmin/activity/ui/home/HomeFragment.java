package com.plabon.parkingadmin.activity.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.plabon.parkingadmin.activity.ReportActivity;
import com.plabon.parkingadmin.adapter.CarAdapter;
import com.plabon.parkingadmin.adapter.PriceAdapter;
import com.plabon.parkingadmin.api.GetInCarListApiInterface;
import com.plabon.parkingadmin.api.GetPriceListApiInterface;
import com.plabon.parkingadmin.api.clients.RestClient;
import com.plabon.parkingadmin.model.CarList;
import com.plabon.parkingadmin.model.PriceList;

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


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    RecyclerView rv;
    ArrayList<CarList> pList;
    View progress;
    TextView btnFind,btnFindCar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        rv= root.findViewById(R.id.rv_home);
        btnFind= root.findViewById(R.id.find);
        btnFindCar= root.findViewById(R.id.findCarNo);
        pList=new ArrayList<>();
        progress = root.findViewById(R.id.progress_bar);
getCarList();

btnFind.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        /*Intent intent = new Intent(getContext(), ReportActivity.class);
        startActivity(intent);*/
        String url = RestClient.BASE_URL + "api/v1/report.php";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
});

        btnFindCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        /*Intent intent = new Intent(getContext(), ReportActivity.class);
        startActivity(intent);*/
                String url = RestClient.BASE_URL + "api/v1/reportno.php";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return root;
    }
    private void getCarList(){
        // progressDialog.show();
        showProgressDialog();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetInCarListApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        GetInCarListApiInterface api = retrofit.create(GetInCarListApiInterface.class);

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

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);
                                //   Toast.makeText(getContext(),"i" + dataobj.getString("bannar").toString(),Toast.LENGTH_SHORT).show();

                                pList.add(new CarList(
                                        dataobj.getString("carnumber"),
                                        dataobj.getString("cartype"),
                                        dataobj.getString("loadtype"),
                                        dataobj.getString("intimestamp"),
                                        dataobj.getString("indate"),
                                        dataobj.getString("outdate"),
                                        dataobj.getString("outtimestamp"),
                                        dataobj.getString("staydays"),
                                        dataobj.getString("amount"),
                                        dataobj.getString("collectedby"),
                                        dataobj.getString("statusnow"),
                                        dataobj.getString("rate")
                                ));


                            }
                            setupOrderRecycleView();
                           // Toast.makeText(getContext(),"Result " + jsonresponse,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            Toast.makeText(getContext(),"Result " + e.toString(),Toast.LENGTH_LONG).show();
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
        rv.setLayoutManager(rvLiLayoutManager);

        CarAdapter adapter = new CarAdapter(pList,getContext());

        rv.setAdapter(adapter);
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
}