package com.whitezealots.squad_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
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

import java.io.File;
import java.io.FileNotFoundException;

public class Aboutus extends AppCompatActivity {
    private TextView update_date;
    private DatabaseReference db;
    private String date ;
    private DownloadManager downloadManager;
    private long refid;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        SharedPreferences pref = getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
        if(!pref.getBoolean("mode",false)){
            RelativeLayout layout = findViewById(R.id.about_us_activity);
            layout.setBackgroundResource(R.drawable.bg_1);
            Button button;
            button=findViewById(R.id.bug_button);
            button.setTextColor(Color.parseColor("#000000"));
            TextView tx;
            tx = findViewById(R.id.created);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.created_1);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.Architect);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.Arch_name_1);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.Arch_name_2);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.Arch_name_3);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.update_date_s);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.update_date);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.your_version);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.ver_ins);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.contact_us);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.mail_id);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.tc);
            tx.setTextColor(Color.parseColor("#000000"));
        }
        else {
            RelativeLayout layout = findViewById(R.id.about_us_activity);
            layout.setBackgroundResource(R.drawable.bg_dark);
            Button button;
            button=findViewById(R.id.bug_button);
            button.setTextColor(Color.parseColor("#ffffff"));
            TextView tx;
            tx = findViewById(R.id.created);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.created_1);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.Architect);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.Arch_name_1);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.Arch_name_2);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.Arch_name_3);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.update_date_s);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.update_date);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.your_version);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.ver_ins);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.contact_us);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.mail_id);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.tc);
            tx.setTextColor(Color.parseColor("#ffffff"));
        }

        File file = new File(Environment.DIRECTORY_DOWNLOADS , "Haggle.apk");
        if(file.isFile()){
            file.delete();
        }

        findViewById(R.id.bug_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Aboutus.this, Bug_report.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
        findViewById(R.id.msg_abt_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Aboutus.this, Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        db = FirebaseDatabase.getInstance().getReference("data");
        db.child("Patch").child("Latest_update").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                date = dataSnapshot.getValue(String.class);
                date = date + " ";
                update_date = (TextView)findViewById(R.id.update_date);
                update_date.setText(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.layout_about_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStoragePermissionGranted()  && i == 0) {
                    i = 1;
                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri download_uri;
                    download_uri = Uri.parse("https://drive.google.com/uc?export=download&id=1Py2HPH2iHNJmcxvMI8_OFQ-1w2oCmwfC");
                    DownloadManager.Request request = new DownloadManager.Request(download_uri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                    request.setAllowedOverRoaming(true);
                    request.setAllowedOverMetered(true);
                    request.setTitle("Haggle");
                    request.setDescription("Downloading the APK");
                    request.setVisibleInDownloadsUi(true);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Haggle.apk");
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    request.setMimeType("*/");
                    refid = downloadManager.enqueue(request);
                    Status();

                    try {
                        downloadManager.openDownloadedFile(refid);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(Aboutus.this, "Download have started", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.tc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://drive.google.com/file/d/13IGkFfR_9N9mNxDDIz8uFFcCHUoML2qu/view?usp=sharing";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    private  void Status(){
        Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(refid));
        if (cursor != null && cursor.moveToNext()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            cursor.close();

            if (status == DownloadManager.STATUS_FAILED) {
                // do something when failed
            }
            else if (status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_PAUSED) {
                // do something pending or paused
            }
            else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                File file = new File(Environment.DIRECTORY_DOWNLOADS+ "/Haggle.apk");
                installAPK(file);
            }
            else if (status == DownloadManager.STATUS_RUNNING) {
                // do something when running
            }
        }
    }


    private void installAPK(File apkFile) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        startActivity(intent);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){


            // permission granted

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Aboutus.this, Settings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
