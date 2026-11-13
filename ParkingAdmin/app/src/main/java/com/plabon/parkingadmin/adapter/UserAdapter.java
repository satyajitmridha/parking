package com.plabon.parkingadmin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.plabon.parkingadmin.R;
import com.plabon.parkingadmin.activity.DashHomeActivity;
import com.plabon.parkingadmin.activity.ui.newuser.NewuserFragment;
import com.plabon.parkingadmin.api.DelUserApiInterface;
import com.plabon.parkingadmin.api.GetPriceListApiInterface;
import com.plabon.parkingadmin.api.GetUserApiInterface;
import com.plabon.parkingadmin.model.CarList;
import com.plabon.parkingadmin.model.ParkingUser;
import com.plabon.parkingadmin.model.PriceList;
import com.plabon.parkingadmin.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    List<ParkingUser> orderList;
    Context context;
    Fragment fragment;

    public UserAdapter(List<ParkingUser> orderList, Context context, Fragment fragment) {
        this.orderList = orderList;
        this.context = context;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_user, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final ParkingUser data = orderList.get(position);
        holder.name.setText("  Name - " + data.getName());
        holder.mobile.setText("  Mobile - " + data.getMobile());
       // holder.dob.setText(" DOB - " + data.g());
        holder.pass.setText("  Pass - " + data.getP());
        //holder.rate.setText("  Rate Rs - " + data.getRate());
       // holder.intime.setText("  In Date - " + data.getIntimestamp());



        holder.btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                //Intent intent = new Intent(context, OrderDetailsActivity.class);
                //intent.putExtra("MY_KEY", ""+order.getOrderid()+"");
                // intent.putExtra("MY_KEYN", ""+foodItem.getName()+"");
                //intent.putExtra("id", foodItem.getId());
                //context.startActivity(intent);
                //  Log.d(TAG, "onClick: click On 111");
               // getUser(data.getId());
               if (fragment instanceof NewuserFragment) {
                    ((NewuserFragment)fragment).showDelAlertYes("Want to Delete",data.getId());
                }
               // NewuserFragment newuserFragment=new NewuserFragment();
               // newuserFragment.delUser(data.getId());
               // Toast.makeText(context,"Item Clicked - "+data.getId(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getUser(String s){
        // progressDialog.show();
        //showProgressDialog();
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
                            //showAlert(obj.getString("status"));
                              Toast.makeText(context,"Deleted " ,Toast.LENGTH_LONG).show();
//                            JSONArray dataArray  = obj.getJSONArray("data");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //  Toast.makeText(MainActivity.this,"Strint--" + jsonresponse,Toast.LENGTH_LONG).show();
                        //writeTv(jsonresponse);

                    } else {
                       // hideProgressDialog();
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

    @Override
    public int getItemCount() {

        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, mobile, dob,pass,btndel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);
            dob = itemView.findViewById(R.id.dob);
           // indate = itemView.findViewById(R.id.ind);
            pass = itemView.findViewById(R.id.pass);
            //staydays = itemView.findViewById(R.id.sd);
            btndel = itemView.findViewById(R.id.btndel);

        }
    }
}
