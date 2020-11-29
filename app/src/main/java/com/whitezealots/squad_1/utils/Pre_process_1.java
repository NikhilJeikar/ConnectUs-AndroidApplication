package com.whitezealots.squad_1.utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.whitezealots.squad_1.Initial_login;
import com.whitezealots.squad_1.R;
import com.whitezealots.squad_1.Services.Service1;
import com.whitezealots.squad_1.bio_auth.Bio_metric_auth;
import com.whitezealots.squad_1.message_list;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Pre_process_1 extends AppCompatActivity {

    private String mynum;
    private DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("data");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_process_1);
        startService(new Intent(getApplicationContext(), Service1.class));

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences pref1 = getSharedPreferences("BioAuth",Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            SharedPreferences number = getSharedPreferences("ActivityMobileNumber", Context.MODE_PRIVATE);
            mynum = number.getString("activity_executed","");
            if(!pref1.getBoolean("auth_stat", false)){
                main_run();

                Intent intent = new Intent(this, message_list.class);
                startActivity(intent);
                finish();
            }
            else {
                main_run();

                Intent intent = new Intent(this, Bio_metric_auth    .class);
                startActivity(intent);
                finish();
            }

        } else {
            Intent intent = new Intent(this, Initial_login.class);
            startActivity(intent);
            finish();
        }


    }


    public static String getCurrentDate_server() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }


    private void main_run(){
        db1.child("Meta-User-data").child("Total_visit").child(mynum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer integer;
                try {
                    integer = dataSnapshot.getValue(Integer.class);
                    if(integer == null){
                        db1.child("Meta-User-data").child("Total_visit").child(mynum).setValue(0);
                    }
                    else {
                        db1.child("Meta-User-data").child("Total_visit").child(mynum).setValue(integer + 1);
                    }
                }
                catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        db1.child("Meta-User-data").child("Visit").child(mynum).child(getCurrentDate_server()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer integer;
                try {
                    integer = dataSnapshot.getValue(Integer.class);
                    if(integer == null){
                        db1.child("Meta-User-data").child("Visit").child(mynum).child(getCurrentDate_server()).setValue(1);
                    }
                    else {
                        db1.child("Meta-User-data").child("Visit").child(mynum).child(getCurrentDate_server()).setValue(integer + 1);
                    }
                }
                catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
