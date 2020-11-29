package com.whitezealots.squad_1.utils.Adapters;

import android.graphics.Bitmap;

public class Contact_save {
    private String opp_name , msg_stat;
    private Bitmap dp;

    public Contact_save(String opp_name, String msg_stat, Bitmap dp) {
        this.opp_name = opp_name;
        this.msg_stat = msg_stat;
        this.dp = dp;
    }

    public String getOpp_name() {
        return opp_name;
    }

    public String getMsg_stat() {
        return msg_stat;
    }

    public Bitmap getDp() {
        return dp;
    }
}
