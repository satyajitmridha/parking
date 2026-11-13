package com.plabon.parkingadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.plabon.parkingadmin.R;
import com.plabon.parkingadmin.activity.UpdatePriceActivity;
import com.plabon.parkingadmin.activity.ui.cartype.CartypeFragment;
import com.plabon.parkingadmin.activity.ui.setprice.SetPriceFragment;
import com.plabon.parkingadmin.model.PriceList;


import java.util.List;


public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.MyViewHolder> {

    List<PriceList> orderList;
    Context context;
    Fragment fragment;

    public PriceAdapter(List<PriceList> orderList, Context context, Fragment fragment) {
        this.orderList = orderList;
        this.context = context;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_price_list, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final PriceList data = orderList.get(position);
        holder.cartype.setText("# Car Type : " + data.getCartype());
        holder.loadtype.setText("  Load Type : " + data.getLoadtype());
        holder.price.setText("  Rate : " + data.getPrice());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                Intent intent = new Intent(context, UpdatePriceActivity.class);
                intent.putExtra("ID", ""+data.getId()+"");
                intent.putExtra("CT", ""+data.getCartype() +"");
                intent.putExtra("CL", data.getLoadtype() +"");
                context.startActivity(intent);
                //  Log.d(TAG, "onClick: click On 111");
               // Toast.makeText(context,"Item Clicked - "+data.getId(),Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment instanceof SetPriceFragment) {
                    ((SetPriceFragment)fragment).showDelAlertYes("Want to Delete",data.getId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cartype, loadtype, price, status,btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cartype = itemView.findViewById(R.id.cartype);
            loadtype = itemView.findViewById(R.id.loadtype);
            price = itemView.findViewById(R.id.pice);
            btnDelete = itemView.findViewById(R.id.btndelete);
            //status = itemView.findViewById(R.id.status);

        }
    }
}
