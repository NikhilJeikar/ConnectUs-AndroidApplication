package com.whitezealots.squad_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Otp_verification extends AppCompatActivity {

    private FirebaseAuth mauth;
    private String v_id;
    private EditText otp;
    private String mobile;
    private Boolean login_logic;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        mauth = FirebaseAuth.getInstance();
        otp = findViewById(R.id.Otp);

        Intent intent0 = getIntent();
        mobile = intent0.getStringExtra("mobile");
        sendVerificationCode(mobile);

        findViewById(R.id.login_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Otp_verification.this, Initial_login.class);
                finishAndRemoveTask();
                startActivity(intent1);
            }
        });

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otp.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    Toast.makeText(Otp_verification.this,"Otp is invalid",Toast.LENGTH_SHORT).show();
                    otp.setError("Enter valid code");
                    otp.requestFocus();
                    return;
                }
                try{
                verifyVerificationCode(code);
                }
                catch (Exception e){
                    if(e instanceof FirebaseTooManyRequestsException){
                        Toast.makeText(Otp_verification.this,"Quota exceeded",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mauth)
                .setPhoneNumber("+91"+mobile)
                .setActivity(this)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otp.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Otp_verification.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            v_id = s;
        }
    };

    private void verifyVerificationCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(v_id, code);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(Otp_verification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("data").child("User-Reg").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        String value = snapshot.getValue(String.class);
                                        if(value.equals(mobile)){
                                            login_logic = false;
                                            break;

                                        }
                                        else {
                                            login_logic = true;

                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            try{
                            if(login_logic){
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"You have already Loggedin another device",Toast.LENGTH_LONG).show();
                            }
                            }
                            catch (Exception e){
                                }
                            onrun();
                        } else {

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Toast.makeText(Otp_verification.this,message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void onrun(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("data").child("User-Reg").child(mobile).setValue(mobile);
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("activity_executed", true);
        edt.apply();

        SharedPreferences num = getSharedPreferences("ActivityMobileNumber", Context.MODE_PRIVATE);
        SharedPreferences.Editor num_edit = num.edit();
        num_edit.putString("activity_executed",mobile);
        num_edit.apply();

        final SharedPreferences logic_1 = getSharedPreferences("Service_logic_1", Context.MODE_PRIVATE);
        SharedPreferences.Editor logic_1_edit = logic_1.edit();
        logic_1_edit.putBoolean("Service_logic",true);
        logic_1_edit.apply();


        Intent intent = new Intent(Otp_verification.this, Initial_profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(Otp_verification.this, Initial_login.class);
        finishAndRemoveTask();
        startActivity(intent1);
    }
}
