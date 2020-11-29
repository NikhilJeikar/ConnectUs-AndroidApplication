package com.whitezealots.squad_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class App_Setting extends AppCompatActivity {
    private Switch sw1,sw2;
    private FingerprintManager fingerprintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        fingerprintManager =
                (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        ImageButton back_btn;
        back_btn = (ImageButton)findViewById(R.id.privacy_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App_Setting.this, Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        sw1 = (Switch)findViewById(R.id.switch1);
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    try {
                        if (!fingerprintManager.isHardwareDetected()) {
                            Toast.makeText(App_Setting.this,"Your device doesn't support fingerprint authentication",Toast.LENGTH_LONG).show();
                            sw1.setChecked(false);
                        }
                        else if (ActivityCompat.checkSelfPermission(App_Setting.this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(App_Setting.this,"Please enable the fingerprint permission",Toast.LENGTH_LONG).show();
                            sw1.setChecked(false);
                        }
                        else if (!fingerprintManager.hasEnrolledFingerprints()) {
                            Toast.makeText(App_Setting.this,"No fingerprint configured. Please register at least one fingerprint in your device's Settings",Toast.LENGTH_LONG).show();
                            sw1.setChecked(false);
                        }
                        else {
                            sw1.setChecked(true);
                            SharedPreferences pref = getSharedPreferences("BioAuth", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edt = pref.edit();
                            edt.putBoolean("auth_stat", true);
                            edt.commit();
                        }

                    }
                    catch (Exception e){}
                }
                else {
                    SharedPreferences pref = getSharedPreferences("BioAuth", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edt = pref.edit();
                    edt.putBoolean("auth_stat", false);
                    edt.commit();
                }

            }
        });

        SharedPreferences pref2 = getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
        if(!pref2.getBoolean("mode",false)){
            RelativeLayout layout = findViewById(R.id.activity_privacy);
            layout.setBackgroundResource(R.drawable.bg_1);

            TextView tx;
            tx = findViewById(R.id.text_1);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.text_2);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.sub_text_1);
            tx.setTextColor(Color.parseColor("#000000"));

        }
        else {
            RelativeLayout layout = findViewById(R.id.activity_privacy);
            layout.setBackgroundResource(R.drawable.bg_dark);
            TextView tx;
            tx = findViewById(R.id.text_1);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.text_2);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.sub_text_1);
            tx.setTextColor(Color.parseColor("#ffffff"));
        }
        SharedPreferences pref1 = getSharedPreferences("BioAuth",Context.MODE_PRIVATE);
        if(!pref1.getBoolean("auth_stat", false)){
            sw1.setChecked(false);
        }
        else {
            sw1.setChecked(true);
        }

        sw2 = findViewById(R.id.switch2);
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences pref = getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edt = pref.edit();
                    edt.putBoolean("mode", true);
                    edt.commit();
                    RelativeLayout layout  = findViewById(R.id.activity_privacy);
                    layout.setBackgroundResource(R.drawable.bg_dark);
                    TextView tx;
                    tx = findViewById(R.id.text_1);
                    tx.setTextColor(Color.parseColor("#ffffff"));
                    tx = findViewById(R.id.text_2);
                    tx.setTextColor(Color.parseColor("#ffffff"));
                    tx = findViewById(R.id.sub_text_1);
                    tx.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    SharedPreferences pref = getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edt = pref.edit();
                    edt.putBoolean("mode", false);
                    edt.commit();
                    RelativeLayout layout  = findViewById(R.id.activity_privacy);
                    layout.setBackgroundResource(R.drawable.bg_1);
                    TextView tx;
                    tx = findViewById(R.id.text_1);
                    tx.setTextColor(Color.parseColor("#000000"));
                    tx = findViewById(R.id.text_2);
                    tx.setTextColor(Color.parseColor("#000000"));
                    tx = findViewById(R.id.sub_text_1);
                    tx.setTextColor(Color.parseColor("#000000"));
                }
            }
        });
        SharedPreferences pref = getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
        if(!pref.getBoolean("mode",false)){
            sw2.setChecked(false);
        }
        else {
            sw2.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(App_Setting.this, Settings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
