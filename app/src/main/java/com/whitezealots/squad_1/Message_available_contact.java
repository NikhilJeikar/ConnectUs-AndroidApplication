package com.whitezealots.squad_1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
    private ArrayList<Contact> contactArrayList  ,list;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private String user_num;
    private Contact_Adaptor_1 contactAdaptor ;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_availabe_contact);
        list = new ArrayList<>();

        SharedPreferences pref = getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
        if(!pref.getBoolean("mode",false)){
            RelativeLayout layout = findViewById(R.id.message_available_contact_activity);
            layout.setBackgroundResource(R.drawable.bg_1);

        }
        else {
            RelativeLayout layout = findViewById(R.id.message_available_contact_activity);
            layout.setBackgroundResource(R.drawable.bg_dark);
        }

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

        swipeRefreshLayout = findViewById(R.id.message_available_contact_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                contactArrayList = new ArrayList<>();
                contactAdaptor = new Contact_Adaptor_1(getApplicationContext());
                set_contact();
                contact();
                set_contact();
                contactAdaptor.notifyDataSetChanged();
            }
        });

        contactAdaptor = new Contact_Adaptor_1(this);
        contactArrayList = new ArrayList<>();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                get_contact();
                contact();
            }
        };
        r.run();




    }


    private void contact(){
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
        {
            if(contactArrayList.isEmpty()){
                GetContactsIntoArrayList();

            }
            if(!contactArrayList.isEmpty()){
                sub();
                set_contact();
            }
        }
    }


    private void sub(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("data");
            for(int i = 0 ; i<contactArrayList.size();i++){
                if(contactArrayList.get(i).getContact_name() != null && contactArrayList.get(i).getContact_num() != null){
                    final String name = contactArrayList.get(i).getContact_name();
                    final String num  =contactArrayList.get(i).getContact_num();
                    databaseReference.child("User-Reg").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                if(Objects.equals(snapshot.getValue(String.class), num)){
                                    Contact_Class contact = new Contact_Class(name,num, user_num);
                                    Contact contact2 = new Contact(name,num);
                                    Boolean l = false;
                                    for(Contact contact1 :list){
                                        if(contact1.equals(contact2)){
                                            l = true;
                                            break;
                                        }
                                    }
                                    if(!l){
                                        contactAdaptor.add(contact);
                                        Contact contact1 = new Contact(name,num);
                                        list.add(contact1);
                                    }

                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

        ListView listView = findViewById(R.id.list);
            contactAdaptor.notifyDataSetChanged();
            listView.setAdapter(contactAdaptor);
            listView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String num = list.get(position).getContact_num();
                            String name = list.get(position).getContact_name();
                            Intent intent = new Intent(Message_available_contact.this,messaging.class);
                            intent.putExtra("user_num",num);
                            intent.putExtra("user_name",name);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                        }
                    }
            );


    }


    private void GetContactsIntoArrayList(){
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "SELECT DISTINCT " + ContactsContract.CommonDataKinds.Phone.NUMBER, null, "Upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");

        assert cursor != null;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            PhoneNumberUtils.formatNumber(phonenumber);
            phonenumber = num_pre_process(phonenumber);

            final Contact contact = new Contact(name.substring(0,1).toUpperCase()+ name.substring(1),phonenumber);
            contactArrayList.add(contact);


            }

        cursor.close();

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


    private void get_contact(){
        SharedPreferences contact_pref = getSharedPreferences("Total_Contact_list",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = contact_pref.getString("task list" ,null);
        Type type = new TypeToken<ArrayList<Contact>>(){}.getType();
        contactArrayList = gson.fromJson(json,type);

        if(contactArrayList == null){
            contactArrayList = new ArrayList<>();
        }
    }


    private void set_contact(){
        SharedPreferences contact_pref = getSharedPreferences("Total_Contact_list",MODE_PRIVATE);
        SharedPreferences.Editor editor = contact_pref.edit();
        Gson gson = new Gson();
        String lis = gson.toJson(contactArrayList);
        editor.putString("task list",lis);
        editor.apply();
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
