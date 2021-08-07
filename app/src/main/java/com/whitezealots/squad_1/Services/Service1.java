package com.whitezealots.squad_1.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whitezealots.squad_1.Aboutus;
import com.whitezealots.squad_1.Initial_login;
import com.whitezealots.squad_1.R;
import com.whitezealots.squad_1.messaging;
import com.whitezealots.squad_1.utils.Adapters.Contact;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class Service1 extends Service
{
    private DatabaseReference reference;
    private String my_num;
    private String msg_send_name;
    private Integer value;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private Boolean block = false;
    private ArrayList<Contact> contactArrayList,contact_current_ArrayList;
    public static final String DATE_FORMAT_1 = "hh:mm:ss";
    public static final String DATE_FORMAT_2 = "dd/M/yyyy";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences num = getSharedPreferences("Service_logic_1", Context.MODE_PRIVATE);
        Boolean logic = num.getBoolean("Service_logic", false);

        contactArrayList = new ArrayList<Contact>();
        reference = FirebaseDatabase.getInstance().getReference("data");
        reference.child("Patch").child("Latest_update").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.getValue(String.class).matches("1.0.1")){
                    Update_notification();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(logic){
            get_contact();
            get_used_contact();

            SharedPreferences number = getSharedPreferences("ActivityMobileNumber", Context.MODE_PRIVATE);
            my_num = number.getString("activity_executed","");

            String my_version = "1.0.0";
            reference.child("User-Version").child(my_num).setValue(my_version);
            reference.child("Reported-User").child(my_num).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if((int)dataSnapshot.getChildrenCount()>3){
                        reference.child("Blocked-user").child(my_num).setValue(getCurrentDate());
                        block = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if(block){
                reference.child("Reported-User").child(my_num).removeValue();
                SharedPreferences number1 = getSharedPreferences("ActivityMobileNumber", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = number1.edit();
                String mynum = number1.getString("activity_executed","");


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

                Intent intent1 = new Intent(this,Initial_login.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }

            reference.child("Noti_Stat").child(my_num).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SharedPreferences pref_1 = getSharedPreferences("Noti", Context.MODE_PRIVATE);
                        value = snapshot.getValue(Integer.class);
                        if (value.equals(0) && !pref_1.getString("Noti" ,"121").equals(snapshot.getKey())) {
                            reference.child("Noti_Stat").child(my_num).child(Objects.requireNonNull(snapshot.getKey())).setValue(1);
                            notificationDialog(snapshot.getKey());
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            reference.child("Issue").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean log = true;
                    String value ;
                    value = dataSnapshot.child("notice").getValue(String.class);
                    for (DataSnapshot snapshot : dataSnapshot.child("members").getChildren()) {
                        if (snapshot.getValue(String.class).equals(my_num)) {
                            log = true;
                            break;
                        }
                        else {
                            log = false;
                        }
                    }
                    if (!log){ exploit(value);
                        reference.child("Issue").child("members").child(my_num).setValue(my_num);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("WrongConstant")
    private void exploit(String iss) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "Haggle";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Issues");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.WHITE);
            notificationChannel.setVibrationPattern(new long[]{50, 100, 50, 100});
            notificationChannel.enableVibration(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.noti)
                .setColorized(true)
                .setBadgeIconType(R.drawable.noti)
                .setTicker("Team Haggle")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(iss);
        notificationManager.notify(101, notificationBuilder.build());


    }


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void notificationDialog(String num) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "Haggle";

        Random random;
        random = new Random();
        int rand_id = random.nextInt(10000);
        reference = FirebaseDatabase.getInstance().getReference("data");


        for(int i =0;i<contactArrayList.size();i++){
            if(contactArrayList.get(i).getNumber().equals(num)){
                msg_send_name = contactArrayList.get(i).getName();
                break;
            }
        }
        if(msg_send_name == null || msg_send_name.isEmpty()){
            msg_send_name = num;
        }
        Contact contact = new Contact(msg_send_name ,num,"");
        ArrayList<Contact> t = new ArrayList<Contact>();
        for (int z = 0;z<contact_current_ArrayList.size();z++){
            if(!contact_current_ArrayList.get(z).getNumber().equals(num)){
                t.add(contact_current_ArrayList.get(z));
            }
        }
        t.add(contact);
        contact_current_ArrayList = t;
        set_used_contact();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.WHITE);
            notificationChannel.setVibrationPattern(new long[]{50, 100, 50, 100});
            notificationChannel.enableVibration(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(getApplicationContext(), messaging.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("user_num",num);
        intent.putExtra("user_name",msg_send_name);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.noti)
                .setContentIntent(pendingIntent)
                .setColorized(true)
                .setBadgeIconType(R.drawable.noti)
                .setTicker("CC")
                .setContentTitle(msg_send_name)
                .setContentText("Sent a Message")
                .setContentInfo("Information");
        notificationManager.notify(rand_id, notificationBuilder.build());

    }


    @SuppressLint("WrongConstant")
    private void Update_notification(){
        Intent intent = new Intent(getApplicationContext(), Aboutus.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "Haggle";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.WHITE);
            notificationChannel.setVibrationPattern(new long[]{50, 100, 50, 100});
            notificationChannel.enableVibration(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.noti)
                .setContentIntent(pendingIntent)
                .setColorized(true)
                .setBadgeIconType(R.drawable.noti)
                .setTicker("Haggle")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Update Available");
        notificationManager.notify(10, notificationBuilder.build());

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


    public static String getCurrentDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }


    private void get_used_contact(){
        SharedPreferences contact_pref = getSharedPreferences("Contact_list",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = contact_pref.getString("task list" ,null);
        Type type = new TypeToken<ArrayList<Contact>>(){}.getType();
        contact_current_ArrayList = gson.fromJson(json,type);
        if(contact_current_ArrayList == null){
            contact_current_ArrayList = new ArrayList<>();
        }
    }


    private void set_used_contact(){
        SharedPreferences contact_pref = getSharedPreferences("Contact_list",MODE_PRIVATE);
        SharedPreferences.Editor editor = contact_pref.edit();
        Gson gson = new Gson();
        String lis = gson.toJson(contact_current_ArrayList);
        editor.putString("task list",lis);
        editor.commit();
    }


    @Override
    public void onCreate() {
    }


    @Override
    public void onDestroy() {
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(this,Service1.class);
        startService(intent);
        startService(intent);
    }

}
