package com.whitezealots.squad_1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Settings extends AppCompatActivity {
    private ImageButton back_btn;
    private Button logout,profile,about_us,privacy;
    private String mynum;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        SharedPreferences pref = getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
        if(!pref.getBoolean("mode",false)){
            RelativeLayout layout = findViewById(R.id.settings_activity);
            layout.setBackgroundResource(R.drawable.bg_1);
            Button button;
            button = findViewById(R.id.profile);
            button.setTextColor(Color.parseColor("#000000"));
            button = findViewById(R.id.about_us);
            button.setTextColor(Color.parseColor("#000000"));
            button = findViewById(R.id.general);
            button.setTextColor(Color.parseColor("#000000"));
            button = findViewById(R.id.logout);
            button.setTextColor(Color.parseColor("#000000"));
        }
        else {
            RelativeLayout layout = findViewById(R.id.settings_activity);
            layout.setBackgroundResource(R.drawable.bg_dark);
            Button button;
            button = findViewById(R.id.profile);
            button.setTextColor(Color.parseColor("#ffffff"));
            button = findViewById(R.id.about_us);
            button.setTextColor(Color.parseColor("#ffffff"));
            button = findViewById(R.id.general);
            button.setTextColor(Color.parseColor("#ffffff"));
            button = findViewById(R.id.logout);
            button.setTextColor(Color.parseColor("#ffffff"));
        }


        back_btn = (ImageButton)findViewById(R.id.setting_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this,message_list.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            alert();
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this,Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });

        about_us = (Button) findViewById(R.id.about_us);
        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this,Aboutus.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });

        privacy = (Button) findViewById(R.id.general);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, App_Setting.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });



    }


    private void alert(){
        AlertDialog.Builder dailog = new AlertDialog.Builder(this);
        dailog.setMessage("Do you want to Logout ?");
        dailog.setTitle("Logout");
        dailog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences number = getSharedPreferences("ActivityMobileNumber", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = number.edit();
                mynum = number.getString("activity_executed","");


                editor.clear();
                editor.commit();

                SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = pref.edit();
                edt.putBoolean("activity_executed", false);
                edt.commit();

                SharedPreferences contact_pref = getSharedPreferences("Contact_list",MODE_PRIVATE);
                SharedPreferences.Editor editor1 = contact_pref.edit();
                editor1.clear();
                editor1.commit();

                SharedPreferences logic_1 = getSharedPreferences("Service_logic_1", Context.MODE_PRIVATE);
                SharedPreferences.Editor logic_1_edit = logic_1.edit();
                logic_1_edit.putBoolean("Service_logic",false);
                logic_1_edit.apply();

                Intent intent = new Intent(Settings.this,Initial_login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finishAndRemoveTask();
                startActivity(intent);

            }
        });
        dailog.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = dailog.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Settings.this,message_list.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
