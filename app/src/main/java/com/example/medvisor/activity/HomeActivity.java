package com.example.medvisor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.medvisor.R;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private TextView welcomeTxt;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String userName = sharedpreferences.getString("UserName","");
        String currentLang = Locale.getDefault().getLanguage();
        drawerLayout = findViewById(R.id.drawerLayout);
        welcomeTxt = findViewById(R.id.welcome);
        String name = "";
        if(currentLang.equals("ar")){
            name = "مرحبا " + userName;
        }else {
            name = "Welcome " + userName;
        }
        String welcome = name + ".\n" +getString(R.string.user_welcome);
        welcomeTxt.setText(welcome);
    }

    public void ShowMenu(View view) {
        drawerLayout.openDrawer(Gravity.START);
    }

    public void HideMenu(View view) {
        drawerLayout.closeDrawer(Gravity.START);
    }

    public void Logout(View view) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        Intent loginIntent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }

    public void startPrediction(View view) {
        Intent predictionIntent = new Intent(HomeActivity.this,PredictionActivity.class);
        startActivity(predictionIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }
}
