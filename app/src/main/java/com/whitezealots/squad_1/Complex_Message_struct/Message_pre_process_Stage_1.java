package com.whitezealots.squad_1.Complex_Message_struct;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whitezealots.squad_1.R;
import com.whitezealots.squad_1.message2;
import com.whitezealots.squad_1.utils.Adapters.Message_Adapter.message1;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

class A{
    TextView messageBody,time;
}

public class Message_pre_process_Stage_1 extends BaseAdapter {
    private ArrayList<message1> message1ArrayList = new ArrayList<message1>();
    Context context;

    public Message_pre_process_Stage_1(Context context) {
        this.context = context;
    }


    public void add(message1 message) {
        notifyDataSetChanged();
        this.message1ArrayList.add(message);
    }



    @Override
    public int getCount() {
        return message1ArrayList.size();
    }


    @Override
    public Object getItem(int position) {
        return message1ArrayList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        A holder = new A();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        message1 message  = message1ArrayList.get(position);
        if (message.getBelongToCurrentUser()) {

            convertView = messageInflater.inflate(R.layout.message_layout, null);

            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            holder.time = (TextView) convertView.findViewById(R.id.message_time);

            convertView.setTag(holder);
            holder.messageBody.setText(message.getMsg1());
        } else {
            convertView = messageInflater.inflate(R.layout.message_layout_1, null);

            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body_1);
            holder.time = (TextView) convertView.findViewById(R.id.message_time_1);

            convertView.setTag(holder);

            holder.time.setText(message.getTime());
            holder.messageBody.setText(message.getMsg1());
        }
        return convertView;
    }



}
