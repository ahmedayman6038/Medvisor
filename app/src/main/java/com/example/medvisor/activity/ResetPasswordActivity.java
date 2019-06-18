package com.example.medvisor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Patient;
import com.example.medvisor.rest.PatientApi;
import com.example.medvisor.rest.RetrofitClient;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResetPasswordActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private SharedPreferences sharedpreferences;
    private EditText passwordTxt;
    private EditText cPasswordTxt;
    private EditText emailTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        sharedpreferences   = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        emailTxt            = findViewById(R.id.email);
        passwordTxt         = findViewById(R.id.password);
        cPasswordTxt         = findViewById(R.id.confirmpassword);
    }



    public void Login(View view) {
        Intent loginIntent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
        startActivity(loginIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    public void Reset(View view) {
        String email        = emailTxt.getText().toString();
        String password     = passwordTxt.getText().toString();
        String cPassword     = cPasswordTxt.getText().toString();
        if(!TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(cPassword) &&
                !TextUtils.isEmpty(password)) {
            if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                if(password.equals(cPassword)){
                    if(password.length() >= 6){
                        Patient user = new Patient("system",email,password,null,null);
                        ResetPassword(user);
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_short_password), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_notmatch_password), Toast.LENGTH_SHORT);
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

    public void ResetPassword(Patient patient){
        retrofit = RetrofitClient.getClient();
        final PatientApi patientApi = retrofit.create(PatientApi.class);
        Call<Patient> call = patientApi.resetPassword(patient);
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
                    Intent homeIntent = new Intent(ResetPasswordActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                }else if(response.code() == 404){
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_notfound_email), Toast.LENGTH_SHORT);
                    toast.show();
                }else if(response.code() == 400){
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_taken_password), Toast.LENGTH_SHORT);
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

}
