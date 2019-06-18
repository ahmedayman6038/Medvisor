package com.example.medvisor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Assessment;
import com.example.medvisor.model.Patient;
import com.example.medvisor.model.Specialty;
import com.example.medvisor.model.Symptom;
import com.example.medvisor.rest.PatientApi;
import com.example.medvisor.rest.RetrofitClient;
import com.example.medvisor.rest.SpecialtyApi;
import com.example.medvisor.service.AssessmentsAdapter;
import com.example.medvisor.service.SpecialtiesAdapter;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PatientPredictionsActivity extends AppCompatActivity {
    private ArrayList<Assessment> results;
    private ListView assessments;
    private Retrofit retrofit;
    private ImageView backImg;
    private SharedPreferences sharedpreferences;
    private int patientId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_predictions);
        backImg        = findViewById(R.id.backImg);
        assessments  = findViewById(R.id.assessments);
        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String userId = sharedpreferences.getString("UserID","");
        String currentLang = Locale.getDefault().getLanguage();
        if(currentLang.equals("ar")){
            backImg.setBackgroundResource(R.drawable.ic_navigate_next_black_24dp);
        }else {
            backImg.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
        }
        if(!userId.equals("")){
            patientId = Integer.parseInt(userId);
            GetAssessments();
        }
        assessments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Assessment selectedItem = (Assessment) parent.getItemAtPosition(position);
                Intent resultIntent = new Intent(PatientPredictionsActivity.this,PredictionResultActivity.class);
                resultIntent.putExtra("ASSESSMENT_ID", selectedItem.getId());
                startActivity(resultIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        });
    }

    public void BackHome(View view) {
        Intent homeIntent = new Intent(PatientPredictionsActivity.this,HomeActivity.class);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    private void GetAssessments(){
        retrofit = RetrofitClient.getClient();
        final PatientApi patientApi = retrofit.create(PatientApi.class);
        Call<ArrayList<Assessment>> call = patientApi.getPatientResults(patientId);
        call.enqueue(new Callback<ArrayList<Assessment>>() {
            @Override
            public void onResponse(Call<ArrayList<Assessment>> call, Response<ArrayList<Assessment>> response) {
                if(response.code() == 200){
                    results = response.body();
                    AssessmentsAdapter arrayAdapter = new AssessmentsAdapter(getApplicationContext(),results);
                    assessments.setAdapter(arrayAdapter);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Assessment>> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }
}
