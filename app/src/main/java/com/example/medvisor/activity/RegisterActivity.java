package com.example.medvisor.activity;

import com.example.medvisor.R;
import com.example.medvisor.model.Patient;
import com.example.medvisor.rest.PatientApi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    static final String BASE_URL = "http://ahmed6038-001-site1.btempurl.com/";
    private static Retrofit retrofit = null;
    private final static String API_KEY = "";
    private EditText usernameTxt;
    private EditText emailTxt;
    private EditText passwordTxt;
    private DatePicker birthDate;
    private RadioButton male;
    private RadioButton female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameTxt= (EditText) findViewById(R.id.username);
        emailTxt   = (EditText) findViewById(R.id.email);
        passwordTxt= (EditText) findViewById(R.id.password);
        birthDate  = (DatePicker) findViewById(R.id.Birthdate);
        male       = (RadioButton) findViewById(R.id.male);
        female     = (RadioButton) findViewById(R.id.female);
    }
    public void Register (View view) {
        String username = usernameTxt.getText().toString();
        String email    = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        Date   Birthdate= (Date)new Date(birthDate.getYear(),birthDate.getMonth(),birthDate.getDayOfMonth());
        String Gender   = (male.isChecked()) ? "M" : "F";
        if(!TextUtils.isEmpty(username) &&!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)&& Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Patient user= new Patient(username,email,password,Birthdate,Gender);
            RegisterInToSystemy(user);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), ""+getString(R.string.data_error), Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    void RegisterInToSystemy(Patient user)
    {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        final PatientApi patientApi = retrofit.create(PatientApi.class);
        Call<Patient> call = patientApi.regeister(user);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if(response.code() == 200){
                    Toast toast = Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }else if(response.code() == 404){
                    Toast toast = Toast.makeText(getApplicationContext(), "Patient Not Founded", Toast.LENGTH_SHORT);
                    toast.show();
                }else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<Patient> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), "something goes wrong check internet", Toast.LENGTH_SHORT);
                toast.show();
                //throwable.printStackTrace();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });

    }

}
