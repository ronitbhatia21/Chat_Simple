package com.example.chat_simple_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class LoginMobileNumber extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText mobile;
    Button send;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mobile_number);

        countryCodePicker=findViewById(R.id.countryCodeHolder);
        mobile=findViewById(R.id.log_mobile);
        send=findViewById(R.id.send_otp);
        progressBar= findViewById(R.id.progress_circular);

        progressBar.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(mobile);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!countryCodePicker.isValidFullNumber()){
                    mobile.setError("Mobile number is not valid");
                }else{
                    Intent intent= new Intent(LoginMobileNumber.this,LoginOtpActivity.class);
                    intent.putExtra("mobile",countryCodePicker.getFullNumberWithPlus());
                    startActivity(intent);
                }
            }
        });


    }
}