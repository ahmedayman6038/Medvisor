package com.example.medvisor.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.medvisor.R;
import com.example.medvisor.model.Login;
import com.example.medvisor.model.Message;
import com.example.medvisor.model.Patient;
import com.example.medvisor.rest.PatientApi;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://ahmed6038-001-site1.btempurl.com/";
    private static Retrofit retrofit = null;
    private final static String API_KEY = "";

    private EditText emailTxt;
    private EditText passwordTxt;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailTxt = (EditText) findViewById(R.id.email);
        passwordTxt = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = emailTxt.getText().toString();
                String userPassword = passwordTxt.getText().toString();
                if(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword)){
                    if(Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                        Login user = new Login(userEmail, userPassword);
                        loginToSystem(user);
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Please fill all inputs", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void loginToSystem(Login user){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        final PatientApi patientApi = retrofit.create(PatientApi.class);
        Call<Patient> call = patientApi.login(user);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if(response.code() == 200){
                    Patient patient = response.body();
                    Toast toast = Toast.makeText(getApplicationContext(),Integer.toString(patient.getId()) , Toast.LENGTH_SHORT);
                    toast.show();
                }else if(response.code() == 404){
                    Toast toast = Toast.makeText(getApplicationContext(), "Patient Not Founded", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
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
