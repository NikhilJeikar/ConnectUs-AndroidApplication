package com.whitezealots.squad_1.Services;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.whitezealots.squad_1.utils.Adapters.Contact_Adapter.Contact_Class;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Service2 extends Service {
    private DatabaseReference reference;
    private String my_num;
    public static final String DATE_FORMAT_1 = "kk:mm:ss";
    public static final String DATE_FORMAT_2 = "dd/M/yyyy";
    private final Handler handler = new Handler();
    private Runnable runnable , run;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



            reference = FirebaseDatabase.getInstance().getReference("data");

            SharedPreferences num = getSharedPreferences("Service_logic_1", Context.MODE_PRIVATE);
            Boolean logic = num.getBoolean("Service_logic", false);

            SharedPreferences number = getSharedPreferences("ActivityMobileNumber", Context.MODE_PRIVATE);
            my_num = number.getString("activity_executed","");


        run = new Runnable() {
            @Override
            public void run() {
                SharedPreferences num = getSharedPreferences("Service_logic_1", Context.MODE_PRIVATE);
                Boolean lo = num.getBoolean("Service_logic", false);
                PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
                KeyguardManager myKM = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
                if(isApplicationInForeground(getApplicationContext()) && lo &&!myKM.inKeyguardRestrictedInputMode() && powerManager.isInteractive() ){

                reference.child("Last_seen").child(my_num).child("Date").setValue(getCurrentDate());
                reference.child("Last_seen").child(my_num).child("Time").setValue(getCurrentTime());

                }
                handler.postDelayed(run,10000);
            }
        };
        run.run();



        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public static String getCurrentDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }


    public static String getCurrentTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    public static boolean isApplicationInForeground(final Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            final ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
