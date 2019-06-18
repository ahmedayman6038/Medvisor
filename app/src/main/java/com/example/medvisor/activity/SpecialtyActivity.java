package com.example.medvisor.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Doctor;
import com.example.medvisor.model.Patient;
import com.example.medvisor.model.Specialty;
import com.example.medvisor.model.Symptom;
import com.example.medvisor.rest.PatientApi;
import com.example.medvisor.rest.RetrofitClient;
import com.example.medvisor.rest.SpecialtyApi;
import com.example.medvisor.service.SpecialtiesAdapter;
import com.example.medvisor.service.SymptomsAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SpecialtyActivity extends AppCompatActivity {
    private ArrayList<Specialty> results;
    private ListView specialties;
    private Retrofit retrofit;
    private ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialty);
        backImg        = findViewById(R.id.backImg);
        specialties  = findViewById(R.id.specialties);
        String currentLang = Locale.getDefault().getLanguage();
        if(currentLang.equals("ar")){
            backImg.setBackgroundResource(R.drawable.ic_navigate_next_black_24dp);
        }else {
            backImg.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
        }
        GetSpecialties();
        specialties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Specialty selectedItem = (Specialty) parent.getItemAtPosition(position);
                Intent doctorIntent = new Intent(SpecialtyActivity.this,DoctorSpecialtyActivity.class);
                doctorIntent.putExtra("Specialty_ID", selectedItem.getId());
                doctorIntent.putExtra("Specialty_Name", selectedItem.getName());
                startActivity(doctorIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        });
    }

    public void BackHome(View view) {
        Intent homeIntent = new Intent(SpecialtyActivity.this,HomeActivity.class);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    private void GetSpecialties(){
        retrofit = RetrofitClient.getClient();
        final SpecialtyApi specialtyApi = retrofit.create(SpecialtyApi.class);
        Call<ArrayList<Specialty>> call = specialtyApi.GetSpecialties();
        call.enqueue(new Callback<ArrayList<Specialty>>() {
            @Override
            public void onResponse(Call<ArrayList<Specialty>> call, Response<ArrayList<Specialty>> response) {
                if(response.code() == 200){
                    results = response.body();
                    SpecialtiesAdapter arrayAdapter = new SpecialtiesAdapter(getApplicationContext(),results);
                    specialties.setAdapter(arrayAdapter);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Specialty>> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }
}
