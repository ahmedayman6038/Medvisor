package com.example.medvisor.activity;

import com.example.medvisor.R;
import com.example.medvisor.model.Patient;
import com.example.medvisor.rest.PatientApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private static Retrofit retrofit = null;
    SharedPreferences sharedpreferences;
    private EditText usernameTxt;
    private EditText emailTxt;
    private EditText passwordTxt;
    private DatePicker birthDate;
    private RadioButton male;
    ProgressBar registerProgress;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharedpreferences   = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        usernameTxt         = findViewById(R.id.username);
        emailTxt            = findViewById(R.id.email);
        passwordTxt         = findViewById(R.id.password);
        birthDate           = findViewById(R.id.birthdate);
        male                = findViewById(R.id.male);
        registerBtn         = findViewById(R.id.registerBtn);
        registerProgress    = findViewById(R.id.registerProgress);
    }

    public void Register (View view) {
        String username     = usernameTxt.getText().toString();
        String email        = emailTxt.getText().toString();
        String password     = passwordTxt.getText().toString();
        Date   birthdate    = getDateFromDatePicker(birthDate);
        String Gender       = (male.isChecked()) ? "M" : "F";
        if(!TextUtils.isEmpty(username) &&
                !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(password)) {
            if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                if(password.length() >= 6){
                    Patient user = new Patient(username,email,password,birthdate,Gender);
                    RegisterToSystem(user);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_short_password), Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_email), Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), ""+getString(R.string.error_empty_data), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    void RegisterToSystem(Patient user) {
        registerBtn.setVisibility(View.INVISIBLE);
        registerProgress.setVisibility(View.VISIBLE);
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        final PatientApi patientApi = retrofit.create(PatientApi.class);
        Call<Patient> call = patientApi.register(user);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if(response.code() == 200){
                    Patient patient = response.body();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("UserID", Integer.toString(patient.getId()));
                    editor.putString("UserName", patient.getName());
                    editor.putString("UserEmail", patient.getEmail());
                    editor.commit();
                    Intent homeIntent = new Intent(RegisterActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                }else if(response.code() == 400){
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_taken_data), Toast.LENGTH_SHORT);
                    toast.show();
                    registerBtn.setVisibility(View.VISIBLE);
                    registerProgress.setVisibility(View.INVISIBLE);
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                    registerBtn.setVisibility(View.VISIBLE);
                    registerProgress.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<Patient> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure register",""+throwable.getLocalizedMessage());
                registerBtn.setVisibility(View.VISIBLE);
                registerProgress.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void Login(View view) {
        Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }

    public Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }
}
