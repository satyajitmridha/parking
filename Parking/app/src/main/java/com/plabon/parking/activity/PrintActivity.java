package com.plabon.parking.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.plabon.parking.R;
import com.plabon.parking.api.GetCarApiInterface;
import com.plabon.parking.api.GetCarPrintApiInterface;
import com.zj.btsdk.BluetoothService;

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

public class PrintActivity extends AppCompatActivity {
    static final String TAG = "PrintDemoActivity";
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    Button btnSearch,btnOrderDelivered,btnSettDeliveryBoy;
    Button btnSendDraw,orderaccept;
    Button btnSend,btnFind,btnShareBill;
    EditText cno;
    TextView tvCarNo,tvSerial,tvCarDetails;

    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    Context context;
    View viewOtpLayout;
    View progress;
    String id="",uid="",cano="",indate="",intime="",od="",otime="",ctype="",ltype="",Amount="",date_difference="",rate="",dc="",statusnow="";

    String msg = "";
    String msgP = "";
    String msgWhatsapp = "";
    String msgWhatsappNumber = "";
    String DIVIDER = "--------------------------------";
    String DIVIDER_DOUBLE = "==============================";
    String BREAK = "\n";
    String SPACE5 = "     ";
    String SPACE4 = "   ";
    String carno="";

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(context, "Connect successful",
                                    Toast.LENGTH_SHORT).show();
//                            btnClose.setEnabled(true);
                            btnSend.setEnabled(true);
                            //    btnSendDraw.setEnabled(true);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d(TAG, "Bluetooth is connecting");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.d(TAG, "Bluetooth state listen or none");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Toast.makeText(context, "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    //  btnClose.setEnabled(false);
                    btnSend.setEnabled(false);
                    //   btnSendDraw.setEnabled(false);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    Toast.makeText(context, "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        progress = findViewById(R.id.progress_bar);
        btnShareBill = findViewById(R.id.btnShareBill);
        cno= findViewById(R.id.cno);
        Intent intent = getIntent();
        carno = intent.getStringExtra("NO");
        cno.setText(carno.toString());
        tvCarNo = findViewById(R.id.tvcno);
        tvSerial = findViewById(R.id.tvserial);
        tvCarDetails = findViewById(R.id.tvCarDetails);

        context = this;
        mService = new BluetoothService(this, mHandler);

        if (!mService.isAvailable()) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }



    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mService.isBTopen()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        btnSearch = (Button) this.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new ClickEvent());
        btnSend = (Button) this.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new ClickEvent());
        btnFind = findViewById(R.id.finnd);
        btnFind.setOnClickListener(new ClickEvent());

        btnSend.setEnabled(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
                } else {
                    finish();
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    con_dev = mService.getDevByMac(address);

                    mService.connect(con_dev);
                }
                break;
        }
    }


    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v == btnSearch) {
                try {
                    Intent serverIntent = new Intent(context, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                } catch (Exception e) {
                    //e.printStackTrace();
                    Toast.makeText(PrintActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                }

            } else if (v == btnSend) {
              /*  msg += "Grand Total: Rp." + "20.000" + BREAK;
                msg += DIVIDER_DOUBLE + BREAK;
                msg += "Payment Type: Cash" + BREAK;
                msg += "Total Amount Paid: Rp." + "50.000" + BREAK;
                msg += "Balance: Rp." + "30.000" + BREAK;
                msg += DIVIDER + BREAK + BREAK;*/
//
//                byte[] cmd = new byte[3];
//                cmd[0] = 0x1b;
//                cmd[1] = 0x21;
//                cmd[2] |= 0x10;
//                mService.write(cmd);
//                mService.sendMessage(header, "GBK");
//                cmd[2] &= 0xEF;
//                mService.write(cmd);
//                mService.sendMessage(msg, "GBK");
                mService.sendMessage(""+ msg, "GBK");
                msg="";
            }else if (v == btnFind) {
                if (cno.getText().toString().equals("")){
                    showAlert("Enter Car Number");
                }else{
                    getCar(cno.getText().toString());
                }

            }
        }
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
                .baseUrl(GetCarPrintApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        GetCarPrintApiInterface api = retrofit.create(GetCarPrintApiInterface.class);

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
                                    cano = (dataobj.getString("carnumber"));
                                    indate = (dataobj.getString("indate"));
                                    intime = (dataobj.getString("intimestamp"));
                                    ltype = (dataobj.getString("loadtype"));
                                    ctype = (dataobj.getString("cartype"));
                                    date_difference = (dataobj.getString("staydays"));
                                    rate = (dataobj.getString("rate"));
                                    statusnow = (dataobj.getString("statusnow"));
                                    od= (dataobj.getString("outdate"));
                                    otime= (dataobj.getString("outtimestamp"));
                                    Amount= (dataobj.getString("amount"));
                                }
                                tvCarNo.setText("Car No :- " + cano );
                                tvSerial.setText("Serial :- " + id );


                                if (date_difference.equals("0")){
                                    //textview_total_amount.setText("  Payable Amunt :- " + rate);
                                   // Amount = rate;
                                    dc=String.valueOf((Integer.parseInt(date_difference)+1));
                                }else{
                                   // Amount = String.valueOf((Integer.parseInt(date_difference) * Integer.parseInt(rate)) + Integer.parseInt(rate));
                                   // textview_total_amount.setText("  Payable Amunt :- " + Amount);
                                    dc=String.valueOf((Integer.parseInt(date_difference)+0));
                                }

                                if (statusnow.equals("IN")) {
                                    tvCarDetails.setText("Car Type :- " + ctype + "\nLoad Type :- " + ltype  + "\nIn date :- " + indate  + "\nIn Time :- " + intime  + "\nDays :- " + dc  + "\nRate :- " + rate  + "\nTotal Rs :- " + Amount + "\nAt Parking :- " + statusnow );
                                    msg = "---- Shefali Sushama Parking --- " + "\n\nSerial :- " + id + "\nCar Number :- " + cano + "\nCar Type :- " + ctype + "\nLoad Type :- " + ltype + "\nIn date :- " + indate + ", " + intime + "At Parking :- " + statusnow + "\nDays :- " + dc + ",  Rate :- " + rate + "\nTotal Rs :- " + Amount + BREAK + BREAK + BREAK + "--------------Signature" + BREAK + BREAK + BREAK + BREAK;
                                }else{
                                    tvCarDetails.setText("Car Type :- " + ctype + "\nLoad Type :- " + ltype  + "\nIn date :- " + indate  + "\nIn Time :- " + intime + "\nOut date :- " + od  + "\nOut Time :- " + otime  + "\nDays :- " + dc  + "\nRate :- " + rate  + "\nTotal Rs :- " + Amount );
                                    msg = "---- Shefali Sushama Parking --- " + "\n\nSerial :- " + id + "\nCar Number :- " + cano + "\nCar Type :- " + ctype + "\nLoad Type :- " + ltype + "\nIn date :- " + indate + ", " + intime + "\nOut date :- " + od  + ", " + otime  + "At Parking :- " + statusnow + "\nDays :- " + dc + ",  Rate :- " + rate + "\nTotal Rs :- " + Amount + BREAK + BREAK + BREAK + "--------------Signature" + BREAK + BREAK + BREAK + BREAK;
                                }

                                //  Toast.makeText(getApplicationContext(),"Result " + jsonresponse,Toast.LENGTH_LONG).show();
                            }else{
                                showAlert("Car Not Found");
                               /* textview_in_date.setText("");
                                textview_car_type.setText("  CAR TYPE :- "  );
                                textview_car_load_type.setText("  LOAD TYPE :- "  );
                                textview_total_amount.setText("");
                                vibrate(200);*/
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
}