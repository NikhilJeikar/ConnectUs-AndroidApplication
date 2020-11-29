package com.whitezealots.squad_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whitezealots.squad_1.Services.Service1;
import com.whitezealots.squad_1.Services.Service2;
import com.whitezealots.squad_1.utils.Adapters.Contact;
import com.whitezealots.squad_1.utils.Adapters.Contact_Adapter.Contact_Adaptor;
import com.whitezealots.squad_1.utils.Adapters.Contact_Adapter.Contact_Class;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class message_list extends AppCompatActivity {
    private Contact_Adaptor contactAdaptor ;
    private String mynum;
    private ArrayList<Contact> contactArrayList ;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Integer pos_ ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        get_contact();


        SharedPreferences pref = getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
        if(!pref.getBoolean("mode",false)){
            RelativeLayout layout = findViewById(R.id.message_list_activity);
            layout.setBackgroundResource(R.drawable.bg_1);
        }
        else {
            RelativeLayout layout = findViewById(R.id.message_list_activity);
            layout.setBackgroundResource(R.drawable.bg_dark);
        }


        SharedPreferences pref_1 = getSharedPreferences("Noti", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref_1.edit();
        edt.putString("Noti","121");
        edt.commit();

        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);

        assert pm != null;
        if(ServiceStat(Service1.class) && pm.isInteractive() ){
            startService(new Intent(getApplicationContext(),Service1.class));
        }
        if(ServiceStat(Service2.class) && pm.isInteractive()){
            startService(new Intent(getApplicationContext(),Service2.class));
        }

        NotificationManager notificationManager =(NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancelAll();

        SharedPreferences number = getSharedPreferences("ActivityMobileNumber", Context.MODE_PRIVATE);
        mynum = number.getString("activity_executed","");

        contactAdaptor = new Contact_Adaptor(getApplicationContext());

        View add_people_view = findViewById(R.id.add_people);
        add_people_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(message_list.this, Message_available_contact.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
            }
        });


        View Setting_view = findViewById(R.id.setting);
        Setting_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(message_list.this,Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }

        });

        swipeRefreshLayout = findViewById(R.id.msg_list_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                get_contact();
                contactAdaptor.clear();
                contactAdaptor.notifyDataSetChanged();
                main_run();
            }
        });
        Runnable r= new Runnable() {
            @Override
            public void run() {
                main_run();
            }
        };
        r.run();


    }


    private void main_run(){
        Collections.reverse(contactArrayList);

        if(!contactArrayList.isEmpty()) {

            for (int i = 0; i<contactArrayList.size();i++){
                Contact_Class contact = new Contact_Class(contactArrayList.get(i).getContact_name(),contactArrayList.get(i).getContact_num(),mynum);
                contactAdaptor.add(contact);
            }


            ListView listView = findViewById(R.id.list);
            contactAdaptor.notifyDataSetChanged();
            listView.setAdapter(contactAdaptor);
            registerForContextMenu(listView);

            listView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String num = contactArrayList.get(position).getContact_num();
                            String name = contactArrayList.get(position).getContact_name();
                            Intent intent = new Intent(message_list.this,messaging.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("user_num",num);
                            intent.putExtra("user_name",name);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                        }
                    }
            );


        }
    }


    @Override
    public void onCreateContextMenu(android.view.ContextMenu menu, View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        pos_ = info.position;
        inflater.inflate(R.menu.msg_list_menu, menu);
        SharedPreferences pref = getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.delete){
            Integer i = pos_;
            SharedPreferences contact_pref = getSharedPreferences(mynum + "_" +contactArrayList.get(pos_).getContact_num(),MODE_PRIVATE);
            SharedPreferences.Editor editor = contact_pref.edit();
            editor.clear();
            editor.commit();
            contactAdaptor.delete(pos_);
            contactAdaptor.notifyDataSetChanged();
            contactArrayList.remove(contactArrayList.get(i));
            Collections.reverse(contactArrayList);
            set_contact();
        }else if(item.getItemId() == R.id.hide){
            Integer i = pos_;
            contactAdaptor.delete(pos_);
            contactAdaptor.notifyDataSetChanged();
            contactArrayList.remove(contactArrayList.get(i));
            Collections.reverse(contactArrayList);
            set_contact();
        }
        else{
            return false;
        }
        return true;
    }


    private void get_contact(){
        SharedPreferences contact_pref = getSharedPreferences("Contact_list",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = contact_pref.getString("task list" ,null);
        Type type = new TypeToken<ArrayList<Contact>>(){}.getType();
        contactArrayList = gson.fromJson(json,type);
        if(contactArrayList == null){
            contactArrayList = new ArrayList<>();
        }
    }


    private void set_contact(){
        SharedPreferences contact_pref = getSharedPreferences("Contact_list",MODE_PRIVATE);
        SharedPreferences.Editor editor = contact_pref.edit();
        Gson gson = new Gson();
        String lis = gson.toJson(contactArrayList);
        editor.putString("task list",lis);
        editor.commit();
    }


    private boolean ServiceStat(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        System.exit(0);
        finishAndRemoveTask();
    }
}
