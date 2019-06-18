package com.example.medvisor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Assessment;
import com.example.medvisor.model.Symptom;
import com.example.medvisor.rest.AddCookiesInterceptor;
import com.example.medvisor.rest.PredictionApi;
import com.example.medvisor.rest.ReceivedCookiesInterceptor;
import com.example.medvisor.rest.RetrofitClient;
import com.example.medvisor.service.PredictionAdapter;
import com.example.medvisor.service.SymptomsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PredictionActivity extends AppCompatActivity {
    private ImageView backImg;
    private Retrofit retrofit;
    private ArrayList<Symptom> symptoms;
    private ListView symptomsList;
    private Integer currentSymptom;
    private Integer patientID;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        backImg             = findViewById(R.id.backImg);
        symptomsList      = findViewById(R.id.symptomsResult);
        String currentLang  = Locale.getDefault().getLanguage();
        if(currentLang.equals("ar")){
            backImg.setBackgroundResource(R.drawable.ic_navigate_next_black_24dp);
        }else {
            backImg.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
        }
        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String UserID = sharedpreferences.getString("UserID","");
        if(!UserID.equals("")){
            patientID = Integer.parseInt(UserID);
        }
        symptoms = new ArrayList<Symptom>();
        int symptomId = getIntent().getIntExtra("SYMPTOM_ID", 0);
        if(symptomId != 0){
            currentSymptom = symptomId;
            startPrediction(symptomId);
        }
        symptomsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Symptom selectedItem = (Symptom) parent.getItemAtPosition(position);
                Integer selectedId = selectedItem.getId();
                currentSymptom = selectedId;
                startPrediction(selectedId);
            }
        });
    }

    private void startPrediction(int symptomId){
        retrofit = RetrofitClient.getClient();
        final PredictionApi predictionApi = retrofit.create(PredictionApi.class);
        Call<ArrayList<Symptom>> call = predictionApi.startPrediction(symptomId);
        call.enqueue(new Callback<ArrayList<Symptom>>() {
            @Override
            public void onResponse(Call<ArrayList<Symptom>> call, Response<ArrayList<Symptom>> response) {
                if(response.code() == 200){
                    symptoms = response.body();
                    if(symptoms.size() > 0){
                        PredictionAdapter arrayAdapter = new PredictionAdapter(getApplicationContext(),symptoms);
                        symptomsList.setAdapter(arrayAdapter);
                        Animation lefttori = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_left);
                        symptomsList.startAnimation(lefttori);
                    }else{
                        endPrediction(patientID);
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Symptom>> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }

    public void BackHome(View view) {
        Intent homeIntent = new Intent(PredictionActivity.this,HomeActivity.class);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    public void symptomNotFounded(View view) {
        startPrediction(currentSymptom);
    }

    private void endPrediction(int patientId){
        retrofit = RetrofitClient.getClient();
        final PredictionApi predictionApi = retrofit.create(PredictionApi.class);
        Call<Assessment> call = predictionApi.endPrediction(patientId);
        call.enqueue(new Callback<Assessment>() {
            @Override
            public void onResponse(Call<Assessment> call, Response<Assessment> response) {
                if(response.code() == 200){
                    Assessment assessment = response.body();
                    Intent endPredictionIntent = new Intent(PredictionActivity.this,EndPredictionActivity.class);
                    endPredictionIntent.putExtra("ASSESSMENT_ID", assessment.getId());
                    startActivity(endPredictionIntent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    finish();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<Assessment> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }
}
