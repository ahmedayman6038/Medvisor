package com.example.medvisor.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.medvisor.R;

import java.util.Locale;

public class EndPredictionActivity extends AppCompatActivity {
    private ImageView backImg;
    private int AssessmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_prediction);
        backImg        = findViewById(R.id.backImg);
        String currentLang = Locale.getDefault().getLanguage();
        if(currentLang.equals("ar")){
            backImg.setBackgroundResource(R.drawable.ic_navigate_next_black_24dp);
        }else {
            backImg.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
        }
        int assessmentId = getIntent().getIntExtra("ASSESSMENT_ID", 0);
        if(assessmentId != 0){
            AssessmentID = assessmentId;
        }
    }

    public void PredictionResult(View view) {
        Intent resultIntent = new Intent(EndPredictionActivity.this,PredictionResultActivity.class);
        resultIntent.putExtra("ASSESSMENT_ID", AssessmentID);
        startActivity(resultIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }

    public void BackHome(View view) {
        Intent homeIntent = new Intent(EndPredictionActivity.this,HomeActivity.class);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }
}
