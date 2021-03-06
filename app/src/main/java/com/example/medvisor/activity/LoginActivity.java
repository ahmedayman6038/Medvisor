package com.example.medvisor.activity;

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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Login;
import com.example.medvisor.model.Patient;
import com.example.medvisor.rest.PatientApi;
import com.example.medvisor.rest.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private SharedPreferences sharedpreferences;
    private EditText emailTxt;
    private EditText passwordTxt;
    private ProgressBar loginProgress;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedpreferences   = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        emailTxt            = findViewById(R.id.email);
        passwordTxt         = findViewById(R.id.password);
        loginProgress       = findViewById(R.id.loginProgress);
        loginBtn            = findViewById(R.id.loginBtn);
    }

    private void loginToSystem(Login user){
        loginBtn.setVisibility(View.INVISIBLE);
        loginProgress.setVisibility(View.VISIBLE);
        retrofit = RetrofitClient.getClient();
        final PatientApi patientApi = retrofit.create(PatientApi.class);
        Call<Patient> call = patientApi.login(user,"normal");
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if(response.code() == 200){
                    Patient patient = response.body();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("UserID", Integer.toString(patient.getId()));
                    editor.putString("UserName", patient.getName());
                    editor.putString("UserEmail", patient.getEmail());
                    editor.putString("UserPassword", patient.getPassword());
                    editor.commit();
                    Intent homeIntent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                }else if(response.code() == 404){
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_user_notfound), Toast.LENGTH_SHORT);
                    toast.show();
                    loginBtn.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                    loginBtn.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<Patient> call, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Failure ",""+throwable.getLocalizedMessage());
                loginBtn.setVisibility(View.VISIBLE);
                loginProgress.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void Login(View view) {
        String userEmail = emailTxt.getText().toString();
        String userPassword = passwordTxt.getText().toString();
        if(!TextUtils.isEmpty(userEmail) &&
                !TextUtils.isEmpty(userPassword)){
            if(Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                Login user = new Login(userEmail, userPassword);
                loginToSystem(user);
            }else{
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_email), Toast.LENGTH_SHORT);
                toast.show();
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_empty_data), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void Register(View view) {
        Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onStop() {
        loginBtn.setVisibility(View.VISIBLE);
        loginProgress.setVisibility(View.INVISIBLE);
        super.onStop();
    }

    public void ResetPassword(View view) {
        Intent registerIntent = new Intent(LoginActivity.this,ResetPasswordActivity.class);
        startActivity(registerIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }
}
