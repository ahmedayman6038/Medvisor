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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Patient;
import com.example.medvisor.rest.PatientApi;
import com.example.medvisor.rest.RetrofitClient;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PatientProfileActivity extends AppCompatActivity {
    private ImageView backImg;
    private SharedPreferences sharedpreferences;
    private int patientID;
    private Retrofit retrofit;
    private Patient patient;
    private EditText usernameTxt;
    private EditText emailTxt;
    private EditText weightTxt;
    private EditText heightTxt;
    private DatePicker birthDate;
    private RadioButton male;
    private RadioButton female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        backImg        = findViewById(R.id.backImg);
        usernameTxt         = findViewById(R.id.username);
        emailTxt            = findViewById(R.id.useremail);
        weightTxt         = findViewById(R.id.userweight);
        heightTxt         = findViewById(R.id.userheight);
        birthDate           = findViewById(R.id.birthdate);
        male                = findViewById(R.id.male);
        female              = findViewById(R.id.female);

        String currentLang = Locale.getDefault().getLanguage();
        if(currentLang.equals("ar")){
            backImg.setBackgroundResource(R.drawable.ic_navigate_next_black_24dp);
        }else {
            backImg.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
        }
        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String UserID = sharedpreferences.getString("UserID","");
        if(!UserID.equals("")){
            patientID = Integer.parseInt(UserID);
            GetUserData(patientID);
        }
    }

    public void UpdateProfile(View view) {
        String username     = usernameTxt.getText().toString();
        String email        = emailTxt.getText().toString();
        Date   birthdate    = getDateFromDatePicker(birthDate);
        Integer weight      = Integer.parseInt(weightTxt.getText().toString());
        Integer height      = Integer.parseInt(heightTxt.getText().toString());
        String Gender       = (male.isChecked()) ? "M" : "F";
        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email)) {
            if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                patient.setName(username);
                patient.setEmail(email);
                patient.setBirthDate(birthdate);
                patient.setGender(Gender);
                patient.setHeight(height);
                patient.setWeight(weight);
                UpdatePatient(patient);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_email), Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), ""+getString(R.string.error_empty_data), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void BackHome(View view) {
        Intent homeIntent = new Intent(PatientProfileActivity.this,HomeActivity.class);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    private void GetUserData(int userId){
        retrofit = RetrofitClient.getClient();
        final PatientApi patientApi = retrofit.create(PatientApi.class);
        Call<Patient> call = patientApi.GetPatient(userId);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if(response.code() == 200){
                    patient = response.body();
                    usernameTxt.setText(patient.getName());
                    emailTxt.setText(patient.getEmail());
                    weightTxt.setText(String.valueOf(patient.getWeight()));
                    heightTxt.setText(String.valueOf(patient.getHeight()));
                    if(patient.getGender().equals("M")){
                        male.setChecked(true);
                    }else{
                        female.setChecked(true);
                    }
                    Date birth = patient.getBirthDate();
                    String day = (String) DateFormat.format("dd",   birth);
                    String month = (String) DateFormat.format("MM",   birth);
                    String year = (String) DateFormat.format("yyyy",   birth);
                    birthDate.updateDate(Integer.parseInt(year),Integer.parseInt(month) - 1,Integer.parseInt(day));
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<Patient> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }

    private void UpdatePatient(Patient patient){
        retrofit = RetrofitClient.getClient();
        final PatientApi patientApi = retrofit.create(PatientApi.class);
        Call<Patient> call = patientApi.UpdatePatient(patient.getId(),patient);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if(response.code() == 200){
                    Intent homeIntent = new Intent(PatientProfileActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    finish();
                }else if(response.code() == 400){
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_taken_email), Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<Patient> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
            }
        });
    }

    private Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }
}
