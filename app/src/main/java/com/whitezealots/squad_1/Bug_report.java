package com.whitezealots.squad_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Bug_report extends AppCompatActivity {

    private static final String CHANNEL_ID = "TEST" ;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri path;
    private ImageView imageView;
    private Integer rand_id;
    private int PROGRESS_MAX = 100;
    private int PROGRESS_CURRENT = 0;
    private String mynum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report);

        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.bug_report_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bug_report.this,Aboutus  .class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }

        });


       imageView = findViewById(R.id.bug_image);
    findViewById(R.id.bug_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
    imageView.setImageURI(path);


        findViewById(R.id.bug_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText bug = findViewById(R.id.issue_text);
                Random random;
                random = new Random();
                rand_id = random.nextInt(10000);
                SharedPreferences number = getSharedPreferences("ActivityMobileNumber", Context.MODE_PRIVATE);
                mynum = number.getString("activity_executed","");

                databaseReference.child("data").child("bug").child(mynum).child(Integer.toString(rand_id)).setValue(bug.getText().toString().trim());
                uploadImage();

                Toast.makeText(Bug_report.this,"You feedback has been received thanks",Toast.LENGTH_SHORT).show();

            }
        });

        SharedPreferences pref = getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
        if(!pref.getBoolean("mode",false)){
            RelativeLayout layout = findViewById(R.id.bug_report_activity);
            layout.setBackgroundResource(R.drawable.bg_1);
            TextView tx;
            tx = findViewById(R.id.bug_ques_1);
            tx.setTextColor(Color.parseColor("#000000"));
            tx = findViewById(R.id.bug_ques_2);
            tx.setTextColor(Color.parseColor("#000000"));
            TextView editText;
            editText = findViewById(R.id.issue_text);
            editText.setTextColor(Color.parseColor("#000000"));
            editText.setLinkTextColor(Color.parseColor("#000000"));
            Button button;
            button = findViewById(R.id.bug_submit);
            button.setTextColor(Color.parseColor("#000000"));
        }
        else {
            RelativeLayout layout = findViewById(R.id.bug_report_activity);
            layout.setBackgroundResource(R.drawable.bg_dark);

            TextView tx;
            tx = findViewById(R.id.bug_ques_1);
            tx.setTextColor(Color.parseColor("#ffffff"));
            tx = findViewById(R.id.bug_ques_2);
            tx.setTextColor(Color.parseColor("#ffffff"));
            TextView editText;
            editText = findViewById(R.id.issue_text);
            editText.setTextColor(Color.parseColor("#ffffff"));
            Button button;
            button = findViewById(R.id.bug_submit);
            button.setTextColor(Color.parseColor("#ffffff"));
        }

    }

    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            path = data.getData();
            try {

                // Setting image on image view using Bitmap
                Glide.with(getApplicationContext()).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_dp)
                        .into(imageView);

            }

            catch (Exception e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {
        if (path != null) {

            final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            builder.setContentTitle("Picture Download")
                    .setContentText("Download in progress")
                    .setPriority(NotificationCompat.PRIORITY_LOW);



                    StorageReference mstorage = FirebaseStorage.getInstance().getReference("Bugs").child(mynum + "--" + rand_id);
                    UploadTask uploadTask = mstorage.putFile(path);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata();
                            Toast.makeText(Bug_report.this, "Uploaded the image", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Bug_report.this, "Unable to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });





        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Bug_report.this,Aboutus.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
