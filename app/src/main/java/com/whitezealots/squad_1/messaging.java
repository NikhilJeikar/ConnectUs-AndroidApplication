package com.whitezealots.squad_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whitezealots.squad_1.Complex_Message_struct.Message_pre_process_Stage_1;
import com.whitezealots.squad_1.utils.Adapters.Contact;
import com.whitezealots.squad_1.utils.Adapters.Message_Adapter.message1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class messaging extends AppCompatActivity {
    private ImageView dp ;
    private StorageReference mstorage;
    private DatabaseReference db;
    private ArrayList<Contact> contactArrayList;
    private ArrayList<message1> conv;
    private ImageButton backbutton;
    private ListView list_view;
    private TextView user_name;
    private String an_num,an_name,mynum,val;
    private EditText messages;
    private Message_pre_process_Stage_1 messageAdapter;
    private Integer i=0,j=0,t_msg = 0;
    private long tmp;
    private Integer stat = 0;
    public static final String DATE_FORMAT_1 = "kk:mm:ss";
    public static final String DATE_FORMAT_2 = "dd/M/yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_messaging);

        NotificationManager notificationManager =(NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancelAll();

        SharedPreferences pref = getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
        if(!pref.getBoolean("mode",false)){
            RelativeLayout layout = findViewById(R.id.messaging_activity);
            layout.setBackgroundResource(R.drawable.bg_1);
            EditText editText = findViewById(R.id.mes);
            editText.setTextColor(Color.parseColor("#000000"));
            ImageButton imageButton = findViewById(R.id.msg_send_btn);
            imageButton.setBackgroundResource(R.drawable.send_button);
        }
        else {
            RelativeLayout layout = findViewById(R.id.messaging_activity);
            layout.setBackgroundResource(R.drawable.bg_dark);
            EditText editText = findViewById(R.id.mes);
            editText.setTextColor(Color.parseColor("#ffffff"));
            editText.setHintTextColor(Color.parseColor("#ffffff"));
            ImageButton imageButton = findViewById(R.id.msg_send_btn);
            imageButton.setBackgroundResource(R.drawable.send_button_dark);
        }

        conv = new ArrayList<>();

        contactArrayList = new ArrayList<Contact>();
        get_contact();


        db = FirebaseDatabase.getInstance().getReference("data");
        messageAdapter = new Message_pre_process_Stage_1(messaging.this);

        list_view = (ListView)findViewById(R.id.messages_view);
        list_view.setAdapter(messageAdapter);
        messages = (EditText)findViewById(R.id.mes);

        backbutton = (ImageButton)findViewById(R.id.msg_back_btn);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(messaging.this, message_list.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

            }
        });

        SharedPreferences number = getSharedPreferences("ActivityMobileNumber", Context.MODE_PRIVATE);
        mynum = number.getString("activity_executed","");

        Intent intent = getIntent();
        an_num =intent.getStringExtra("user_num");
        an_name =intent.getStringExtra("user_name");
        get_msg();

        SharedPreferences pref_1 = getSharedPreferences("Noti", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref_1.edit();
        edt.putString("Noti",an_num);
        edt.apply();

        Contact contact = new Contact(an_name,an_num,null);
        ArrayList<Contact> t = new ArrayList<Contact>();
        for (int z = 0;z<contactArrayList.size();z++){
            if(!contactArrayList.get(z).getNumber().equals(an_num)){
                t.add(contactArrayList.get(z));
            }
        }
        t.add(contact);
        contactArrayList = t;

        user_name  =(TextView)findViewById(R.id.name);
        user_name.setText(an_name);

        dp = (ImageView)findViewById(R.id.msg_dp_btn);
        mstorage = FirebaseStorage.getInstance().getReference();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy kk:mm:ss");

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) findViewById(R.id.name)).setText(an_num);
            }
        });

        mstorage.child("dp").child(an_num+".bmp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_dp)
                        .circleCrop().into(dp);

            }
        });
        Status();
        final ImageButton imageButton;
        imageButton = findViewById(R.id.info_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(messaging.this, v);
                popup.getMenuInflater().inflate(R.menu.info_menu,popup.getMenu());
                onPrepareOptionsMenu(popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.save){
                            try {
                                save();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        if(item.getItemId() == R.id.report_user){
                            alert();

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                c_assign();
                getstat();
                geti();
                set_contact();
            }
        }).start();
    sent_msg();
    receive_msg();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.data_save);

        item.setTitle( val);
        return super.onPrepareOptionsMenu(menu);
    }


    private void getstat(){
        db = FirebaseDatabase.getInstance().getReference("data");
        db.child("Msg_Stat").child(an_num).child(mynum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    stat = dataSnapshot.getValue(Integer.class);
                    if(stat == null){
                        stat = 0;
                    }
                }
                catch (Exception e){
                    db.child("Msg_Stat").child(an_num).child(mynum).setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void geti(){
        db = FirebaseDatabase.getInstance().getReference("data");
        db.child("chat-data").child(an_num).child(mynum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    tmp = dataSnapshot.getChildrenCount();
                    i = (int)tmp;
                }
                catch (Exception e){
                    i = 0;
                    db.child("chat-data").child(an_num).child(mynum).setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void c_assign(){
        db = FirebaseDatabase.getInstance().getReference("data");
        db.child("Msg_count").child(mynum).child(an_num).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    t_msg = dataSnapshot.getValue(Integer.class);
                    if(t_msg == null){
                        t_msg = 0;
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


    private void reset(){
        DatabaseReference db5 = FirebaseDatabase.getInstance().getReference("data");
        db5.child("chat-data").child(an_num).child(mynum).setValue(0);
    }


    private void sent_msg(){
        db = FirebaseDatabase.getInstance().getReference("data");
        findViewById(R.id.msg_send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = messages.getText().toString();
                getstat();
                geti();
                if(stat == 1){
                    db.child("Msg_Stat").child(an_num).child(mynum).setValue(0);
                    db.child("Noti_Stat").child(an_num).child(mynum).setValue(0);
                    db.child("Time_stamp").child(an_num).child(mynum).setValue(0);
                    reset();
                    i = 0;
                }
                if(temp.trim().length() >0 && temp.trim() != null){
                    db.child("Msg_Stat").child(an_num).child(mynum).setValue(0);
                    db.child("Msg_Current_stat").child(mynum).child(an_num).setValue(0);
                    db.child("Noti_Stat").child(an_num).child(mynum).setValue(0);
                    i = i +1;
                    t_msg  = t_msg +1;
                    db.child("chat-data").child(an_num).child(mynum).child(i.toString()).child("Msg").setValue(temp.trim());
                    db.child("chat-data").child(an_num).child(mynum).child(i.toString()).child("Time").setValue(getCurrentTimeformes());
                    String msg = temp.trim();
                    String time = getCurrentTimeformes();
                    message1 message1 = new message1(msg,time,true);
                    messageAdapter.add(message1);
                    conv.add(message1);
                    set_msg();
                    messages.setText("");
                    list_view.post(new Runnable() {
                        @Override
                        public void run() {
                            list_view.setSelection(messageAdapter.getCount() - 1);
                            db.child("Msg_count").child(mynum).child(an_num).setValue(t_msg);
                        }
                    });
                }
            }
        });

    }


    private void  receive_msg(){
        db = FirebaseDatabase.getInstance().getReference("data");
        db.child("Msg_Stat").child(mynum).child(an_num).setValue(1);
        db.child("Noti_Stat").child(mynum).child(an_num).setValue(1);
        db.child("chat-data").child(mynum).child(an_num).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String msg =snapshot.child("Msg").getValue(String.class);
                    String time = snapshot.child("Time").getValue(String.class);
                    if(msg != null && time !=null){
                    message1 message1 = new message1(msg,time,false);
                    if (!message1.toString().trim().isEmpty()){
                        messageAdapter.add(message1);
                        conv.add(message1);
                        try{
                            db.child("chat-data").child(mynum).child(an_num).child(snapshot.getKey()).removeValue();
                        }
                        catch (Exception e){

                        }
                        set_msg();
                        db.child("Msg_Current_stat").child(an_num).child(mynum).setValue(1);
                        list_view.post(new Runnable() {
                            @Override
                            public void run() {
                                list_view.setSelection(messageAdapter.getCount() - 1);
                                db.child("Msg_Stat").child(mynum).child(an_num).setValue(1);
                            }
                        });
                    }
                        db.child("chat-data").child(mynum).child(an_num).child(snapshot.getKey()).removeValue();

                }
                }
                db.child("chat-data").child(mynum).child(an_num).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(((int)dataSnapshot.getChildrenCount()) >1){
                            db.child("chat-data").child(mynum).child(an_num).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void set_contact(){
        SharedPreferences contact_pref = getSharedPreferences("Contact_list",MODE_PRIVATE);
        SharedPreferences.Editor editor = contact_pref.edit();
        Gson gson = new Gson();
        String lis = gson.toJson(contactArrayList);
        editor.putString("task list",lis);
        editor.apply();
    }


    private void get_contact(){
        SharedPreferences contact_pref = getSharedPreferences("Contact_list",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = contact_pref.getString("task list" ,null);
        Type type = new TypeToken<ArrayList<Contact>>(){}.getType();
        contactArrayList = gson.fromJson(json,type);
        if(contactArrayList == null){
            contactArrayList = new ArrayList<Contact>();
        }
    }


    private void set_msg(){
        String string = mynum + "_" +an_num;
        SharedPreferences message_pref = getSharedPreferences(string, MODE_PRIVATE);
        SharedPreferences.Editor editor = message_pref.edit();
        Gson gson = new Gson();
        String lis = gson.toJson(conv);
        editor.clear();
        editor.putString("text list",lis);
        editor.apply();

    }


    private void get_msg(){
        String string = mynum + "_" +an_num;
        SharedPreferences message_pref = getSharedPreferences(string , MODE_PRIVATE);
        Gson gson = new Gson();
        String json = message_pref.getString("text list",null);
        Type type = new TypeToken<ArrayList<message1>>(){}.getType();
        conv = gson.fromJson(json , type);
        if(conv == null){
            conv = new ArrayList<message1>();
        }
        list_view = (ListView)findViewById(R.id.messages_view);
        list_view.setAdapter(messageAdapter);
        for(int z = 0;z<conv.size();z++){
            messageAdapter.add(conv.get(z));
        }
        list_view.post(new Runnable() {
            @Override
            public void run() {
                list_view.setSelection(messageAdapter.getCount() - 1);
            }
        });
        String size = conv.toString();
        byte[] bytes = size
                .getBytes();
        if(bytes.length == 1400000){
            SharedPreferences.Editor editor = message_pref.edit();
            editor.clear();
            editor.apply();
        }

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


    public static String getCurrentTimeformes() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }


    public void Difference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;

        Handler handler = new Handler();

        if(elapsedDays != 0){
            user_name.setText("Active " + Integer.toString((int)elapsedDays)+ "days ago");
        }else {
            if(elapsedHours != 0){
                user_name.setText("Active " + Integer.toString((int)elapsedHours) + " hours ago");
            }
            else {
                if(elapsedMinutes != 0){
                    user_name.setText("Active " + Integer.toString((int)elapsedMinutes)+ " mins ago");
                }
                else {
                    user_name.setText("Active now");
                }
            }

        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                user_name.setText(an_name);
            }
        },3000);
    }


    private void save() throws IOException {
        if(isStoragePermissionGranted() && isread()){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            File file = new File(Environment.getExternalStorageDirectory(),an_num + ".txt");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                Writer writer = new OutputStreamWriter(fileOutputStream , "UTF16");

                for(int z=0;z<conv.size();z++){
                    String temp;
                    if(conv.get(z).getBelongToCurrentUser()){
                       temp = conv.get(z).getTime() + "  " + mynum + "  " +conv.get(z).getMsg1()  + "\n";
                    }
                    else {
                        temp =  conv.get(z).getTime() + "  " + an_num + "  " +conv.get(z).getMsg1() + "\n";
                    }
                    writer.write(temp);
                }
                writer.close();
                fileOutputStream.close();
                db = FirebaseDatabase.getInstance().getReference("data");
                db.child("Chat-save").child(mynum).child(an_num).child("Date").setValue(getCurrentDate());
                db.child("Chat-save").child(mynum).child(an_num).child("Logic").setValue("1");
                Toast.makeText(getApplicationContext(), "Saved your text", Toast.LENGTH_LONG).show();
            }
            catch (IOException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Unable to save the text", Toast.LENGTH_LONG).show();
            }

        }


        }

    }


    private void Status(){
        db = FirebaseDatabase.getInstance().getReference("data");
            db.child("Chat-save").child(an_num).child(mynum).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                    if(dataSnapshot.child("Logic").getValue(String.class).equals("1")){
                        val = "Saved by " + an_name + " on " + dataSnapshot.child("Date").getValue(String.class);
                    }}
                    catch (Exception e){
                        val = "Never Saved ";
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }


    public boolean isread(){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            return false;
        }
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }

        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


    private void alert(){
        AlertDialog.Builder dailog = new AlertDialog.Builder(this);
        dailog.setMessage("Do you want to report " + an_name +" ?");
        dailog.setTitle("Report");
        dailog.setPositiveButton("Report", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db = FirebaseDatabase.getInstance().getReference("data");
                db.child("Reported-User").child(an_num).child(mynum).setValue("0");
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){



        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(messaging.this, message_list.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
