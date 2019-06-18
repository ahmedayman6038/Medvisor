package com.example.medvisor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Symptom;
import com.example.medvisor.rest.AddCookiesInterceptor;
import com.example.medvisor.rest.PredictionApi;
import com.example.medvisor.rest.ReceivedCookiesInterceptor;
import com.example.medvisor.rest.RetrofitClient;
import com.example.medvisor.service.SymptomsAdapter;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SymptomActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private ImageView backImg;
    private ListView symptomsResult;
    private EditText symptomName;
    private ProgressBar resultProgress;
    private ArrayList<Symptom> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);
        backImg        = findViewById(R.id.backImg);
        symptomsResult  = findViewById(R.id.symptomsResult);
        symptomName     = findViewById(R.id.symptomName);
        resultProgress   = findViewById(R.id.resultProgress);
        String currentLang = Locale.getDefault().getLanguage();
        if(currentLang.equals("ar")){
            backImg.setBackgroundResource(R.drawable.ic_navigate_next_black_24dp);
        }else {
            backImg.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
        }
        results = new ArrayList<Symptom>();

        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (filterLongEnough()) {
                    getSymptoms(s.toString());
                }
            }

            private boolean filterLongEnough() {
                return symptomName.getText().toString().trim().length() > 2;
            }
        };
        symptomName.addTextChangedListener(fieldValidatorTextWatcher);
        symptomsResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Symptom selectedItem = (Symptom) parent.getItemAtPosition(position);
                initilaizePrediction(selectedItem.getId());
            }
        });
    }

    public void BackHome(View view) {
        Intent homeIntent = new Intent(SymptomActivity.this,HomeActivity.class);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    private void getSymptoms(String name){
        symptomsResult.setVisibility(View.INVISIBLE);
        resultProgress.setVisibility(View.VISIBLE);
        retrofit = RetrofitClient.getClient();
        final PredictionApi predictionApi = retrofit.create(PredictionApi.class);
        Call<ArrayList<Symptom>> call = predictionApi.getSymptom(name);
        call.enqueue(new Callback<ArrayList<Symptom>>() {
            @Override
            public void onResponse(Call<ArrayList<Symptom>> call, Response<ArrayList<Symptom>> response) {
                if(response.code() == 200){
                    results = response.body();
                    SymptomsAdapter arrayAdapter = new SymptomsAdapter(getApplicationContext(),results);
                    symptomsResult.setAdapter(arrayAdapter);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
                symptomsResult.setVisibility(View.VISIBLE);
                resultProgress.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(Call<ArrayList<Symptom>> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
                symptomsResult.setVisibility(View.VISIBLE);
                resultProgress.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initilaizePrediction(Integer symptomId){
        retrofit = RetrofitClient.getClient();
        final PredictionApi predictionApi = retrofit.create(PredictionApi.class);
        Call<Integer> call = predictionApi.initializePrediction(symptomId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200){
                    Integer symptom = response.body();
                    Intent predictionIntent = new Intent(SymptomActivity.this,PredictionActivity.class);
                    predictionIntent.putExtra("SYMPTOM_ID", symptom);
                    startActivity(predictionIntent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    finish();
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

    @Override
    public void onStop() {
        symptomsResult.setVisibility(View.VISIBLE);
        resultProgress.setVisibility(View.INVISIBLE);
        super.onStop();
    }
}
