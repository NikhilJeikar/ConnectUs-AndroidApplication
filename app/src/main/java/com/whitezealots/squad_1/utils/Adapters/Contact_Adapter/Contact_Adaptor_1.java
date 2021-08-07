package com.whitezealots.squad_1.utils.Adapters.Contact_Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
import com.whitezealots.squad_1.R;

import java.util.ArrayList;
import java.util.List;


public class Contact_Adaptor_1 extends BaseAdapter {
    List<Contact_Class> contacts = new ArrayList<>();
    Context context;
    StorageReference mstorage;

    public Contact_Adaptor_1(Context context) {
        this.context = context;
    }


    public void add(Contact_Class contact){
        notifyDataSetChanged();
        this.contacts.add(contact);
    }


    @Override
    public int getCount() {
        return contacts.size();
    }


    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DatabaseReference databaseReference;
        final ContactViewHolder holder = new ContactViewHolder();
        LayoutInflater contactInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Contact_Class contact = contacts.get(position);

        convertView = contactInflater.inflate(R.layout.contact_available_button,null);
        holder.relativeLayout = (RelativeLayout)convertView.findViewById(R.id.contact_b);
        holder.relativeLayout.setMinimumHeight(180);
        holder.Text = (TextView)convertView.findViewById(R.id.text_prev);
        holder.Name = (TextView)convertView.findViewById(R.id.contact_body);
        holder.Dp = (ImageView)convertView.findViewById(R.id.dp_list);
        convertView.setTag(holder);

        holder.Name.setText(contact.getName());
        databaseReference = FirebaseDatabase.getInstance().getReference("data");
        databaseReference.child("user-data").child(contact.getNumber()).child("Bio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    if(dataSnapshot.getValue(String.class) != null){
                    holder.Text.setText(dataSnapshot.getValue(String.class));}
                    else {
                        holder.Text.setText("Available");
                    }
                    }
                catch (Exception e){
                    holder.Text.setText("Available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mstorage = FirebaseStorage.getInstance().getReference();
        mstorage.child("dp").child(contact.getNumber() + ".bmp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).asBitmap().load(uri).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).error(R.drawable.default_dp)
                        .circleCrop().into(holder.Dp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.Dp.setBackground(context.getDrawable(R.drawable.default_dp));
            }
        });
        System.out.println(contact.getPath());
        return convertView;

    }


}
