package com.example.medvisor.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Doctor;
import com.example.medvisor.model.Specialty;
import com.example.medvisor.rest.DoctorApi;
import com.example.medvisor.rest.RetrofitClient;
import com.example.medvisor.rest.SpecialtyApi;
import com.example.medvisor.service.DoctorsAdapter;
import com.example.medvisor.service.SpecialtiesAdapter;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DoctorSpecialtyActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private ImageView backImg;
    private ListView doctorsResult;
    private TextView specialtyNameTxt;
    private ProgressBar resultProgress;
    private ArrayList<Doctor> results;
    private int specialtyId;
    private String specialtyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_specialty);
        specialtyNameTxt = findViewById(R.id.header_text);
        backImg        = findViewById(R.id.backImg);
        doctorsResult  = findViewById(R.id.doctorsResult);
        resultProgress   = findViewById(R.id.resultProgress);
        String currentLang = Locale.getDefault().getLanguage();
        if(currentLang.equals("ar")){
            backImg.setBackgroundResource(R.drawable.ic_navigate_next_black_24dp);
        }else {
            backImg.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
        }
        results = new ArrayList<Doctor>();
        int sid = getIntent().getIntExtra("Specialty_ID", 0);
        String sname = getIntent().getStringExtra("Specialty_Name");
        if(sid != 0){
            specialtyId = sid;
            specialtyName = sname;
            specialtyNameTxt.setText(specialtyName);
            GetDoctorsBySpecialties(specialtyId);
        }
        doctorsResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Doctor selectedItem = (Doctor) parent.getItemAtPosition(position);
                Intent doctorIntent = new Intent(DoctorSpecialtyActivity.this,DoctorProfileActivity.class);
                doctorIntent.putExtra("Doctor_ID", selectedItem.getId());
                startActivity(doctorIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        });
    }

    public void BackHome(View view) {
        Intent homeIntent = new Intent(DoctorSpecialtyActivity.this,SpecialtyActivity.class);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    private void GetDoctorsBySpecialties(int specialtyId) {
        retrofit = RetrofitClient.getClient();
        final DoctorApi doctorApi = retrofit.create(DoctorApi.class);
        Call<ArrayList<Doctor>> call = doctorApi.getDoctorsBySpecialties(specialtyId);
        call.enqueue(new Callback<ArrayList<Doctor>>() {
            @Override
            public void onResponse(Call<ArrayList<Doctor>> call, Response<ArrayList<Doctor>> response) {
                if(response.code() == 200){
                    results = response.body();
                    DoctorsAdapter arrayAdapter = new DoctorsAdapter(getApplicationContext(),results);
                    doctorsResult.setAdapter(arrayAdapter);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Doctor>> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }
}
