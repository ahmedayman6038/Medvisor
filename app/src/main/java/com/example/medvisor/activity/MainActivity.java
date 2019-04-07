package com.example.medvisor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medvisor.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TINE_OUT = 3000;
    public static final String MyPREFERENCES = "Login_Info" ;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String userID = sharedpreferences.getString("UserID","");
                if(userID.equals("")){
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        }, SPLASH_TINE_OUT);
    }


}
