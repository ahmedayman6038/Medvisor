package com.example.medvisor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Doctor;
import com.example.medvisor.rest.DoctorApi;
import com.example.medvisor.rest.RetrofitClient;
import com.example.medvisor.service.DoctorsAdapter;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DoctorProfileActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private TextView doctorNameTxt;
    private TextView doctorCityTxt;
    private TextView doctorAddressTxt;
    private TextView doctorInformationTxt;
    private TextView doctorRateTxt;
    private RatingBar rateDoctor;
    private ImageView backImg;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        doctorNameTxt = findViewById(R.id.doctorName);
        doctorCityTxt = findViewById(R.id.doctorCity);
        doctorAddressTxt = findViewById(R.id.doctorAddress);
        doctorInformationTxt = findViewById(R.id.doctorInformation);
        doctorRateTxt = findViewById(R.id.doctorRate);
        rateDoctor = findViewById(R.id.rateDoctor);
        backImg        = findViewById(R.id.backImg);
        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        final String UserId = sharedpreferences.getString("UserID","");
        String currentLang = Locale.getDefault().getLanguage();
        if(currentLang.equals("ar")){
            backImg.setBackgroundResource(R.drawable.ic_navigate_next_black_24dp);
        }else {
            backImg.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
        }
        final int doctorId = getIntent().getIntExtra("Doctor_ID", 0);
        if(doctorId != 0){
            getDoctorData(doctorId);
        }
        getRateDoctor(doctorId,Integer.parseInt(UserId));
        rateDoctor.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                RateDoctor(doctorId,Integer.parseInt(UserId),(int)Math.ceil(rating));
            }
        });
    }

    private void RateDoctor(int doctorId, int patientId, int value){
        retrofit = RetrofitClient.getClient();
        final DoctorApi doctorApi = retrofit.create(DoctorApi.class);
        Call<Object> call = doctorApi.rateDoctor(doctorId, patientId, value);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){

                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }

    private void getRateDoctor(int doctorId, int patientId){
        retrofit = RetrofitClient.getClient();
        final DoctorApi doctorApi = retrofit.create(DoctorApi.class);
        Call<Integer> call = doctorApi.GetRate(doctorId, patientId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200){
                    Integer result = response.body();
                    rateDoctor.setRating(result);
                }else if(response.code() == 404){
                    rateDoctor.setRating(0);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }

    private void getDoctorData(final int doctorId){
        retrofit = RetrofitClient.getClient();
        final DoctorApi doctorApi = retrofit.create(DoctorApi.class);
        Call<Doctor> call = doctorApi.getDoctorProfile(doctorId);
        call.enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if(response.code() == 200){
                    Doctor doctor = response.body();
                    doctorNameTxt.setText(doctor.getName());
                    doctorCityTxt.setText(doctor.getCity());
                    doctorAddressTxt.setText(doctor.getAddress());
                    doctorRateTxt.setText(String.valueOf((int)Math.floor(doctor.getRating())) + " / 5");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        doctorInformationTxt.setText(Html.fromHtml(doctor.getInformation(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        doctorInformationTxt.setText(Html.fromHtml(doctor.getInformation()));
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<Doctor> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }

    public void Back(View view) {
       /* Intent homeIntent = new Intent(DoctorProfileActivity.this,HomeActivity.class);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);*/
        finish();
    }
}
