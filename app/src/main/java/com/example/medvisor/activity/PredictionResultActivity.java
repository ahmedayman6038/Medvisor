package com.example.medvisor.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Assessment;
import com.example.medvisor.model.PredictionResult;
import com.example.medvisor.rest.PredictionApi;
import com.example.medvisor.rest.RetrofitClient;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PredictionResultActivity extends AppCompatActivity {
    private ImageView backImg;
    private int AssessmentID;
    private LinearLayout dynamicDiseases;
    private LinearLayout dynamicDoctors;
    private LayoutInflater inflater;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_result);
        backImg        = findViewById(R.id.backImg);
        dynamicDiseases = findViewById(R.id.dynamic_diseases);
        dynamicDoctors = findViewById(R.id.dynamic_doctors);
        inflater = LayoutInflater.from(getApplicationContext());
        String currentLang = Locale.getDefault().getLanguage();
        if(currentLang.equals("ar")){
            backImg.setBackgroundResource(R.drawable.ic_navigate_next_black_24dp);
        }else {
            backImg.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
        }
        int assessmentId = getIntent().getIntExtra("ASSESSMENT_ID", 0);
        if(assessmentId != 0){
            AssessmentID = assessmentId;
            GetPredictionResult(AssessmentID);
        }
    }

    public void Back(View view) {
        Intent homeIntent = new Intent(PredictionResultActivity.this,PatientPredictionsActivity.class);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    private void GetPredictionResult(int assessmentID){
        retrofit = RetrofitClient.getClient();
        final PredictionApi predictionApi = retrofit.create(PredictionApi.class);
        Call<PredictionResult> call = predictionApi.getPredictionResult(assessmentID);
        call.enqueue(new Callback<PredictionResult>() {
            @Override
            public void onResponse(Call<PredictionResult> call, Response<PredictionResult> response) {
                if(response.code() == 200){
                    PredictionResult result = response.body();
                    for(int i = 0; i <  result.getDiseases().size(); i++){
                        View disease_to_add = inflater.inflate(R.layout.listview_diseases_result, dynamicDiseases,false);
                        TextView diseaseName = disease_to_add.findViewById(R.id.diseaseName);
                        TextView diseaseType = disease_to_add.findViewById(R.id.diseaseType);
                        TextView diseaseDesc = disease_to_add.findViewById(R.id.diseaseDesc);
                        diseaseName.setText(result.getDiseases().get(i).getName());
                        diseaseType.setText(result.getDiseases().get(i).getSpecialty().getName());
                        diseaseDesc.setText(result.getDiseases().get(i).getDetails());
                        dynamicDiseases.addView(disease_to_add);
                    }
                    for(int j = 0; j <  result.getDoctors().size(); j++){
                        View doctor_to_add = inflater.inflate(R.layout.listview_doctors_result, dynamicDoctors,false);
                        TextView doctorName = doctor_to_add.findViewById(R.id.doctorName);
                        Button doctorBtn = doctor_to_add.findViewById(R.id.doctorProfile);
                        doctorBtn.setId(result.getDoctors().get(j).getId());
                        doctorBtn.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Intent doctorIntent = new Intent(PredictionResultActivity.this,DoctorProfileActivity.class);
                                doctorIntent.putExtra("Doctor_ID", v.getId());
                                startActivity(doctorIntent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                finish();
                            }
                        });
                        doctorName.setText(result.getDoctors().get(j).getName());
                        dynamicDoctors.addView(doctor_to_add);
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<PredictionResult> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }

    public void DeleteResult(View view) {
        deletePredictionResult(AssessmentID);
    }

    private void deletePredictionResult(int assessmentID){
        retrofit = RetrofitClient.getClient();
        final PredictionApi predictionApi = retrofit.create(PredictionApi.class);
        Call<Assessment> call = predictionApi.deletePredictionResult(assessmentID);
        call.enqueue(new Callback<Assessment>() {
            @Override
            public void onResponse(Call<Assessment> call, Response<Assessment> response) {
                if(response.code() == 200){
                    Intent homeIntent = new Intent(PredictionResultActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
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
