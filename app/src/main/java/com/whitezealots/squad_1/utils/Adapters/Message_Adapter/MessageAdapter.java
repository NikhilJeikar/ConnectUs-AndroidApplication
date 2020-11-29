package com.whitezealots.squad_1.utils.Adapters.Message_Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.whitezealots.squad_1.R;

import java.util.ArrayList;
import java.util.List;

class MessageViewHolder {
    public TextView messageBody,time;
}
/*
public class MessageAdapter extends BaseAdapter {
    List<message1> messages = new ArrayList<message1>();
    Context context;

    public MessageAdapter(Context context){
        this.context = context;
    }

    public void add(message1 message){
        notifyDataSetChanged();
        this.messages.add(message);
    }


    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        message1 message = messages.get(position);

        if(message.getBelongToCurrentUser()){
            convertView = messageInflater.inflate(R.layout.message_layout, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            holder.time = (TextView)convertView.findViewById(R.id.message_time);
            convertView.setTag(holder);
            holder.messageBody.setText(message.getMsg1());
            try{
            holder.time.setText("");
            }
            catch (Exception e){
                holder.time.setText("");
            }
        }
        else{

            final DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference("data");
            convertView = messageInflater.inflate(R.layout.message_layout_1, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body_1);
            holder.time = (TextView)convertView.findViewById(R.id.message_time_1);
            convertView.setTag(holder);
            databaseReference.child("Time_stamp").child(message.getUser_num()).child(message.getAn_num()).child(message.getI()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                    holder.time.setText(dataSnapshot.getValue(String.class));
                    }
                    catch (Exception e ){
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            holder.messageBody.setText(message.getMsg1() );

        }

        return convertView;
    }


}*/
