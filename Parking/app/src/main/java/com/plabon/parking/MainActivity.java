package com.plabon.parking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.plabon.parking.activity.DashBoardActivity;
import com.plabon.parking.activity.LoginActivity;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_WRITE_FIELS = 102;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(2 * 1000);

                    // After 5 seconds redirect to another intent
                   // checkAppPermissions();
                    go_next();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // start thread
        background.start();
    }

    public void go_next() {
        if (isLoggedIn()) {
            Intent startmain = new Intent(MainActivity.this, DashBoardActivity.class);
            startActivity(startmain);
        } else {
            Intent startmain = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(startmain);
        }
        finish();
    }

    private boolean isLoggedIn() {
        return false;
    }

}