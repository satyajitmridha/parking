package com.plabon.parkingadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plabon.parkingadmin.R;
import com.plabon.parkingadmin.model.CarList;
import com.plabon.parkingadmin.model.PriceList;

import java.util.List;


public class CarAdapter extends RecyclerView.Adapter<CarAdapter.MyViewHolder> {

    List<CarList> orderList;
    Context context;

    public CarAdapter(List<CarList> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final CarList data = orderList.get(position);
        holder.cartype.setText("  Car Type - " + data.getCartype());
        holder.loadtype.setText("  Load Type - " + data.getLoadtype());
        holder.cno.setText("#" + String.valueOf(position + 1) +" Car Number - " + data.getCarnumber());
        holder.indate.setText("  In Date - " + data.getIndate() +", " + data.getIntimestamp());
        holder.rate.setText("  Rate Rs - " + data.getRate());
       // holder.intime.setText("  In Date - " + data.getIntimestamp());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                //Intent intent = new Intent(context, OrderDetailsActivity.class);
                //intent.putExtra("MY_KEY", ""+order.getOrderid()+"");
                // intent.putExtra("MY_KEYN", ""+foodItem.getName()+"");
                //intent.putExtra("id", foodItem.getId());
                //context.startActivity(intent);
                //  Log.d(TAG, "onClick: click On 111");
               // Toast.makeText(context,"Item Clicked - "+data.getCarnumber(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {

        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cartype, loadtype, indate,rate, status,sdate,cno;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cno = itemView.findViewById(R.id.no);
            cartype = itemView.findViewById(R.id.ctype);
            loadtype = itemView.findViewById(R.id.ltype);
            indate = itemView.findViewById(R.id.ind);
            rate = itemView.findViewById(R.id.rate);
            //staydays = itemView.findViewById(R.id.sd);
            status = itemView.findViewById(R.id.sts);

        }
    }
}
