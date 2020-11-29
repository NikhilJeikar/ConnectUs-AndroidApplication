package com.whitezealots.squad_1;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.entity.StringEntityHC4;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class message2 {
    private String msg1;
    private String user_num;
    private String an_num;
    private String time;
    private String i;
    private Boolean belongToUser;

    public message2(String msg1, String user_num, String an_num, String i, Boolean belongToUser) {
        this.msg1 = msg1;
        this.user_num = user_num;
        this.an_num = an_num;
        this.i = i;
        this.belongToUser = belongToUser;
    }


    private static String getCurrentTimeformes() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }


    public String getMsg1() {
        return msg1;
    }


    public Boolean getBelongToUser() {
        return belongToUser;
    }


    public String getTime() {
        if(!belongToUser){
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("data");
            databaseReference.child("Time_stamp").child(user_num).child(an_num).child(i).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    time = dataSnapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else {
            time = getCurrentTimeformes();
        }

        return time;
    }
}
