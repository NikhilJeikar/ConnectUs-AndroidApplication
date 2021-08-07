package com.whitezealots.squad_1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whitezealots.squad_1.utils.Adapters.Contact;
import com.whitezealots.squad_1.utils.Adapters.Contact_Adapter.Contact_Adaptor_1;
import com.whitezealots.squad_1.utils.Adapters.Contact_Adapter.Contact_Class;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class Message_available_contact extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private String user_num;
    private Contact_Adaptor_1 contactAdaptor ;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_availabe_contact);

        SharedPreferences number = getSharedPreferences("ActivityMobileNumber", Context.MODE_PRIVATE);
        user_num = number.getString("activity_executed","");

        ImageButton back_btn = findViewById(R.id.msg_ava_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Message_available_contact.this,message_list.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
            }
        });

        contactAdaptor = new Contact_Adaptor_1(getApplicationContext());
        ListView view = findViewById(R.id.list);
        view.setAdapter(contactAdaptor);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                contact();
            }
        };
        r.run();

        swipeRefreshLayout = findViewById(R.id.message_available_contact_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                contactAdaptor = new Contact_Adaptor_1(getApplicationContext());
                contact();
            }
        });
    }


    private void contact(){
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
        {
            GetContactsIntoArrayList();
        }
    }


    private void GetContactsIntoArrayList(){
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if((cursor != null ? cursor.getCount() : 0) > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String Name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String  Image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0){
                    Cursor Acursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?",new String[]{id},null);
                    while (Acursor.moveToNext()){
                        String Number = Acursor.getString(Acursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        PhoneNumberUtils.formatNumber(Number, "IN");
                        Contact_Class contact = new Contact_Class(Name,num_pre_process(Number),user_num,Image);
                        contactAdaptor.add(contact);
                        System.out.println(num_pre_process(Number));
                    }
                    Acursor.close();
                }
            }
            cursor.close();
        }
    }

    public String num_pre_process(String phonenumber){
        phonenumber = phonenumber.replaceAll("[^+0-9]", "");
        phonenumber = phonenumber.replace("-","");
        phonenumber = phonenumber.replace("(","");
        phonenumber = phonenumber.replace(")","");
        phonenumber = phonenumber.replace(" ","");
        phonenumber = phonenumber.replace("+91","");

        return phonenumber;

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Message_available_contact.this,message_list.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
    }
}
