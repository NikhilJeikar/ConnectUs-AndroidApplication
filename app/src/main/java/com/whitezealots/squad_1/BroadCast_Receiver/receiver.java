package com.whitezealots.squad_1.BroadCast_Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.whitezealots.squad_1.Services.Service1;

public class receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
            Intent background = new Intent(context, Service1.class);
            context.startService(background);
        }

    }
}
