package com.example.medvisor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Login;
import com.example.medvisor.model.Mail;
import com.example.medvisor.model.Patient;
import com.example.medvisor.rest.PatientApi;
import com.example.medvisor.rest.RetrofitClient;

import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FeedbackActivity extends AppCompatActivity {
    private ImageView backImg;
    private Retrofit retrofit;
    private EditText titleTxt;
    private EditText contentTxt;
    private SharedPreferences sharedpreferences;
    private String senderEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        backImg        = findViewById(R.id.backImg);
        titleTxt         = findViewById(R.id.feedbacktitle);
        contentTxt         = findViewById(R.id.feedbackcontent);
        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String email = sharedpreferences.getString("UserEmail","");
        String currentLang = Locale.getDefault().getLanguage();
        if(currentLang.equals("ar")){
            backImg.setBackgroundResource(R.drawable.ic_navigate_next_black_24dp);
        }else {
            backImg.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
        }
        if(!email.equals("")){
            senderEmail = email;
        }
    }

    public void Send(View view) {
        String title = titleTxt.getText().toString();
        String content = contentTxt.getText().toString();
        if(!TextUtils.isEmpty(title) &&
                !TextUtils.isEmpty(content)){
            Mail mail = new Mail(senderEmail,title,content);
            SendFeedback(mail);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_empty_data), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void SendFeedback(Mail mail){
        retrofit = RetrofitClient.getClient();
        final PatientApi patientApi = retrofit.create(PatientApi.class);
        Call<Mail> call = patientApi.snedFeedback(mail);
        call.enqueue(new Callback<Mail>() {
            @Override
            public void onResponse(Call<Mail> call, Response<Mail> response) {
                if(response.code() == 200){
                    Mail result = response.body();
                    Intent homeIntent = new Intent(FeedbackActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    finish();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<Mail> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }

    public void BackHome(View view) {
        Intent homeIntent = new Intent(FeedbackActivity.this,HomeActivity.class);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }
}
