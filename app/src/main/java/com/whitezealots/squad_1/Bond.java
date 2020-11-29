package com.whitezealots.squad_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whitezealots.squad_1.utils.Adapters.Contact;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Bond extends AppCompatActivity {
    private Integer v1 =0 ,v2 = 0 ,v3 = 0 ,v4 = 0 ,v5 = 0,v6 = 0,v7 = 0;
    private ArrayList<Contact> contactArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bond);


        SharedPreferences pref = getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
        if(!pref.getBoolean("mode",false)){
            RelativeLayout layout = findViewById(R.id.bond_activity);
            layout.setBackgroundResource(R.drawable.bg_1);
            TextView tx;
            tx = findViewById(R.id.bond1_tx_2);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bond2_tx_2);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bond3_tx_2);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bond4_tx_2);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bond5_tx_2);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bond6_tx_2);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bond7_tx_2);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bond1_tx_1);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bond2_tx_1);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bond3_tx_1);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bond4_tx_1);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bond5_tx_1);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bond6_tx_1);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bond7_tx_1);
            tx.setTextColor(Color.parseColor("#000000"));
        }

        else {
            RelativeLayout layout = findViewById(R.id.bond_activity);
            layout.setBackgroundResource(R.drawable.bg_dark);
            TextView tx;
            tx = findViewById(R.id.bond1_tx_2);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bond2_tx_2);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bond3_tx_2);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bond4_tx_2);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bond5_tx_2);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bond6_tx_2);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bond7_tx_2);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bond1_tx_1);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bond2_tx_1);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bond3_tx_1);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bond4_tx_1);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bond5_tx_1);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bond6_tx_1);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bond7_tx_1);
            tx.setTextColor(Color.parseColor("#ffffff"));

        }


        ImageButton backbutton;
        backbutton = findViewById(R.id.bond_back_btn);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bond.this,Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        SharedPreferences number = getSharedPreferences("ActivityMobileNumber", Context.MODE_PRIVATE);
        String mynum = number.getString("activity_executed","");

        SharedPreferences contact_pref = getSharedPreferences("Total_Contact_list",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = contact_pref.getString("task list" ,null);
        Type type = new TypeToken<ArrayList<Contact>>(){}.getType();
        contactArrayList = gson.fromJson(json,type);

        if(contactArrayList == null){
            contactArrayList = new ArrayList<>();
        }
        else {


        }

            DatabaseReference db = FirebaseDatabase.getInstance().getReference("data");
            db.child("Msg_count").child(mynum).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        process(snapshot.getValue(Integer.class));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }) ;



    }

    private void process(int i ){

        if(i>0 && i<=250){v1 = v1 + 1;}
        else if(i>250 && i<=500){v2 = v2 + 1;}
        else if(i>500 && i<=1000){v3 = v3+ 1;}
        else if(i>1000 && i<=1750){v4 = v4+ 1;}
        else if(i>1750 && i<=2500){v5 = v5 + 1;}
        else if(i>2500 && i<= 3250){v6 = v6 + 1;}
        else if(i>3250 && i<=4000){v7 = v7 + 1;}
        else {v1 = v1+1;}

        TextView tx;
        tx = findViewById(R.id.bond1_tx_2);
        tx.setText(Integer.toString(v1));
        tx = findViewById(R.id.bond2_tx_2);
        tx.setText(Integer.toString(v2));
        tx = findViewById(R.id.bond3_tx_2);
        tx.setText(Integer.toString(v3));
        tx = findViewById(R.id.bond4_tx_2);
        tx.setText(Integer.toString(v4));
        tx = findViewById(R.id.bond5_tx_2);
        tx.setText(Integer.toString(v5));
        tx = findViewById(R.id.bond6_tx_2);
        tx.setText(Integer.toString(v6));
        tx = findViewById(R.id.bond7_tx_2);
        tx.setText(Integer.toString(v7));
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Bond.this,Profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
