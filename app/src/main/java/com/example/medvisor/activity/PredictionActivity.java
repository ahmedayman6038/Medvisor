package com.example.medvisor.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Patient;
import com.example.medvisor.rest.PatientApi;
import com.example.medvisor.rest.PredictionApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PredictionActivity extends AppCompatActivity {
    private static Retrofit retrofit = null;
    private Button backBtn;
    private ListView symptomsResult;
    private EditText symptomName;
    private ArrayList<String> results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        String currentLang = Locale.getDefault().getLanguage();
        backBtn         = findViewById(R.id.backBtn);
        symptomsResult  = findViewById(R.id.symptomsResult);
        symptomName     = findViewById(R.id.symptomName);
        if(currentLang.equals("ar")){
           backBtn.setBackgroundResource(R.drawable.ic_navigate_next_black_24dp);
        }else {
            backBtn.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
        }
        results = new ArrayList<String>();
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
                    populateList(s.toString());
                }
            }

            private boolean filterLongEnough() {
                return symptomName.getText().toString().trim().length() > 2;
            }
        };
        symptomName.addTextChangedListener(fieldValidatorTextWatcher);
    }

    public void BackHome(View view) {
        Intent homeIntent = new Intent(PredictionActivity.this,HomeActivity.class);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    private void populateList(String name){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        final PredictionApi predictionApi = retrofit.create(PredictionApi.class);
        Call<ArrayList<String>> call = predictionApi.getSymptom(name);
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if(response.code() == 200){
                    results = response.body();
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.activity_listview,results);
                    symptomsResult.setAdapter(arrayAdapter);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }
}
