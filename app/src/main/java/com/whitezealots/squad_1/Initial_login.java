package com.whitezealots.squad_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.google.firebase.auth.PhoneAuthProvider;

import java.lang.reflect.Array;
import java.util.concurrent.TimeUnit;

public class Initial_login extends AppCompatActivity {
    private EditText login_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_login);
        login_num = (EditText)findViewById(R.id.login_num);

        SharedPreferences num = getSharedPreferences("Service_logic_1", Context.MODE_PRIVATE);
        SharedPreferences.Editor num_edit = num.edit();
        num_edit.putBoolean("Service_logic",false);
        num_edit.apply();


        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = login_num.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 10){
                    Toast.makeText(Initial_login.this,"Enter a valid phone number",Toast.LENGTH_SHORT).show();
                    login_num.setError("Enter a valid mobile");
                    login_num.requestFocus();
                    return;
                }

                Intent intent = new Intent(Initial_login.this, Otp_verification.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.tc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://drive.google.com/file/d/13IGkFfR_9N9mNxDDIz8uFFcCHUoML2qu/view?usp=sharing";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
        System.exit(0);
    }
}