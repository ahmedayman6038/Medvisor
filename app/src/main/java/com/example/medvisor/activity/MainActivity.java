package com.example.medvisor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Login;
import com.example.medvisor.model.Patient;
import com.example.medvisor.rest.PatientApi;
import com.example.medvisor.rest.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TINE_OUT = 1000;
    public static final String MyPREFERENCES = "User_Info" ;
    private SharedPreferences sharedpreferences;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String userEmail = sharedpreferences.getString("UserEmail","");
                String userPassword = sharedpreferences.getString("UserPassword","");
                if(userEmail.equals("") || userPassword.equals("")){
                    Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(loginIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }else{
                    Login user = new Login(userEmail, userPassword);
                    loginToSystem(user);
                }
            }
        }, SPLASH_TINE_OUT);
    }

    private void loginToSystem(Login user){
        retrofit = RetrofitClient.getClient();
        final PatientApi patientApi = retrofit.create(PatientApi.class);
        Call<Patient> call = patientApi.login(user,"system");
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if(response.code() == 200){
                    Patient patient = response.body();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("UserID", Integer.toString(patient.getId()));
                    editor.putString("UserName", patient.getName());
                    editor.putString("UserEmail", patient.getEmail());
                    editor.putString("UserPassword", patient.getPassword());
                    editor.commit();
                    Intent homeIntent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }else{
                    Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(loginIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Patient> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }
}
