package com.kidane.yosief.InstagramDemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Kidane Yosief on 12/1/2015.
 */
public class LogInClass  extends AppCompatActivity implements View.OnClickListener {


    private Button btnConnect;
    private InstagramApp mApp;
    private HashMap<String, String> Arraymap = new HashMap<String, String>();
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Arraymap = mApp.getUserInfo();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(getApplicationContext(), "Check your network.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login_activity);

        // Instagram variables

        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(this);
        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID,
                ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                btnConnect.setText("Disconnect");
                mApp.fetchUserName(handler);
                startactivity();
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
            connectionMang();

    }

    public void onClick(View v) {
        if (v == btnConnect) {
            connectionMang();
        }
    }


    public void connectionMang(){
        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    LogInClass.this);
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    mApp.resetAccessToken();
                                    // btnConnect.setVisibility(View.VISIBLE);
                                    btnConnect.setText("Connect");
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                    btnConnect.setText("Disconnect");
                                    Log.i("pict", mApp.getUserInfo() + " 5");
                                    startactivity();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();

        } else {
            mApp.authorize();
        }

    }

    public void startactivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
