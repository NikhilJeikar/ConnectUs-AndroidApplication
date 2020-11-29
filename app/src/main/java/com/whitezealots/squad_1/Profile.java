package com.whitezealots.squad_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class Profile extends AppCompatActivity {
    private ImageButton back_btn, dp_btn;
    private Button bond;
    private EditText name ,bio;
    private DatabaseReference mdatabase;
    private StorageReference mstorage;
    public String user;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri path;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bio = (EditText)findViewById(R.id.bio_get);
        name = (EditText) findViewById(R.id.name_get_1);

        final SharedPreferences number = getSharedPreferences("ActivityMobileNumber", Context.MODE_PRIVATE);
        user = number.getString("activity_executed", "");

        mstorage = FirebaseStorage.getInstance().getReference();
        mdatabase = FirebaseDatabase.getInstance().getReference();


        dp_btn = (ImageButton)findViewById(R.id.profile_dp_btn);

        mdatabase.child("data").child("user-data").child(user).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                name.setText(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mdatabase.child("data").child("user-data").child(user).child("Bio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                bio.setText(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bond = findViewById(R.id.bond_view);
        bond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this,Bond.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });
        SharedPreferences pref = getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
        if(!pref.getBoolean("mode",false)){
            RelativeLayout layout = findViewById(R.id.profile_activity);
            layout.setBackgroundResource(R.drawable.bg_1);
            bio.setTextColor(Color.parseColor("#000000"));
            name.setTextColor(Color.parseColor("#000000"));
            bond.setTextColor(Color.parseColor("#000000"));
            TextView ts;
            ts = findViewById(R.id.name_text_view);
            ts.setTextColor(Color.parseColor("#000000"));
            ts = findViewById(R.id.bio);
            ts.setTextColor(Color.parseColor("#000000"));
        }
        else {
            RelativeLayout layout = findViewById(R.id.profile_activity);
            layout.setBackgroundResource(R.drawable.bg_dark);
            bio.setTextColor(Color.parseColor("#ffffff"));
            name.setTextColor(Color.parseColor("#ffffff"));
            bond.setTextColor(Color.parseColor("#ffffff"));
            TextView ts;
            ts = findViewById(R.id.name_text_view);
            ts.setTextColor(Color.parseColor("#ffffff"));
            ts = findViewById(R.id.bio);
            ts.setTextColor(Color.parseColor("#ffffff"));
        }


        back_btn = (ImageButton) findViewById(R.id.profile_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty()) {
                    mdatabase.child("data").child("user-data").child(user).child("Name").setValue(name.getText().toString());
                    if (!bio.getText().toString().isEmpty()) {
                        mdatabase.child("data").child("user-data").child(user).child("Bio").setValue(bio.getText().toString());
                        Intent intent = new Intent(Profile.this, Settings.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    } else {
                        mdatabase.child("data").child("user-data").child(user).child("Bio").setValue("Available");
                        Intent intent = new Intent(Profile.this, Settings.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

                    }
                } else {
                    name.setError("Enter valid Name please");
                    name.requestFocus();
                }

            }

        });

        mstorage.child("dp").child(user+".bmp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_dp)
                        .circleCrop().into(dp_btn);
            }
            });

        dp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();


            }
        });

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

                Glide.with(getApplicationContext()).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_dp)
                        .circleCrop().into(dp_btn);
            }

            catch (Exception e) {
                // Log the exception
                e.printStackTrace();
            }
        }
        uploadImage();
    }

    private void uploadImage()
    {
        if (path != null) {
            StorageReference mstorage = FirebaseStorage.getInstance().getReference("dp").child(user + ".bmp");
            UploadTask uploadTask = mstorage.putFile(path);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata();
                    Toast.makeText(Profile.this,"Dp Changed",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Profile.this,"Oops!!! Try again ",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profile.this, Settings.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
