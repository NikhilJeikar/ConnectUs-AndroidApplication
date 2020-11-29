package com.whitezealots.squad_1.utils.Adapters.Contact_Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.whitezealots.squad_1.R;
import com.whitezealots.squad_1.utils.Adapters.Contact_save;

import java.util.ArrayList;
import java.util.List;

class ContactViewHolder{
    RelativeLayout relativeLayout;
    TextView Text,Name;
    ImageView Dp,Frndshp_score,msg;

}

public class Contact_Adaptor extends BaseAdapter {
    List<Contact_Class> contacts = new ArrayList<Contact_Class>();
    ArrayList<Contact_save> offline_messaging_list = new ArrayList<Contact_save>();
    Context context;
    StorageReference mstorage;
    DatabaseReference databaseReference;


    public Contact_Adaptor(Context context) {
        this.context = context;
    }


    public void add(Contact_Class contact){
        notifyDataSetChanged();
        this.contacts.add(contact);
    }


    public void clear(){
        contacts = new ArrayList<Contact_Class>();
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
        final String mynum , anum;

        final ContactViewHolder holder = new ContactViewHolder();
        LayoutInflater contactInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Contact_Class contact_class = contacts.get(position);
        convertView = contactInflater.inflate(R.layout.contact_available_button,null);
        holder.relativeLayout = (RelativeLayout)convertView.findViewById(R.id.contact_b);
        holder.relativeLayout.setMinimumHeight(180);
        holder.Text = (TextView)convertView.findViewById(R.id.text_prev);
        holder.Name = (TextView)convertView.findViewById(R.id.contact_body);
        holder.msg = (ImageView)convertView.findViewById(R.id.msg_stat_symbol);
        holder.Dp = (ImageView)convertView.findViewById(R.id.dp_list);
        holder.Frndshp_score = (ImageView)convertView.findViewById(R.id.frndshp_score);

        convertView.setTag(holder);

        SharedPreferences pref = context.getSharedPreferences("DarkTheme", Context.MODE_PRIVATE);
        if(!pref.getBoolean("mode",false)){
            holder.Text.setTextColor(Color.parseColor("#000000"));
            holder.Name.setTextColor(Color.parseColor("#000000"));
        }
        else {
            holder.Text.setTextColor(Color.parseColor("#ffffff"));
            holder.Name.setTextColor(Color.parseColor("#ffffff"));
        }

        mstorage = FirebaseStorage.getInstance().getReference();
        mstorage.child("dp").child(contact_class.getNum() + ".bmp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).asBitmap().load(uri).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_dp)
                        .circleCrop().into(holder.Dp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.Dp.setBackground(context.getDrawable(R.drawable.default_dp));
            }
        });

        holder.Name.setText(contact_class.getName());
        databaseReference = FirebaseDatabase.getInstance().getReference("data");
        mynum =contact_class.getMynum();
        anum = contact_class.getNum();

        databaseReference.child("chat-data").child(mynum).child(anum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Msg_Stat").child(contact_class.getMynum()).child(contact_class.getNum()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                if(dataSnapshot.getValue(Integer.class) == 0){
                    holder.msg.setVisibility(View.VISIBLE);
                }
                else if(dataSnapshot.getValue(Integer.class) >0){
                    holder.msg.setVisibility(View.INVISIBLE);
                }}catch (Exception e){
                    holder.msg.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Msg_Stat").child(contact_class.getNum()).child(contact_class.getMynum()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    if(dataSnapshot.getValue(Integer.class) == 0){
                        holder.Text.setText("Sent");
                    }else {
                        holder.Text.setText("Seen");
                    }}
                catch (Exception e){
                    holder.Text.setText("Available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Msg_count").child(contact_class.getMynum()).child(contact_class.getNum()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    holder.Frndshp_score.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(process(dataSnapshot.getValue(Integer.class)))));
                    holder.msg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(process(dataSnapshot.getValue(Integer.class)))));
                }
                catch (Exception e){
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Contact_save contactSave = new Contact_save(holder.Name.getText().toString() , holder.Text.getText().toString() , holder.Dp.getDrawingCache());
        offline_messaging_list.add(contactSave);
        return convertView;

    }


    private String process(int i ){
        if(i>0 && i<=250){return "#48a6e8";}
        else if(i>250 && i<=500){return  "#4855e8";}
        else if(i>500 && i<=1000){return "#7b48e8";}
        else if(i>1000 && i<=1750){return "#b848e8";}
        else if(i>1750 && i<=2500){return "#e848db";}
        else if(i>2500 && i<= 3250){return "#e84878";}
        else if(i>3250 && i<=4000){return "#e84848";}
        else {return "#48a6e8";}
    }


    public  void delete(int id){
        contacts.remove(id);

    }

}
