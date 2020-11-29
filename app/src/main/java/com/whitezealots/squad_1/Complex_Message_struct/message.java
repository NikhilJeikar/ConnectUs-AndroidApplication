package com.whitezealots.squad_1.Complex_Message_struct;

public class message {
    String text_msg , time ;
    Boolean belong;

    public message(String text_msg, String time, Boolean belong) {
        this.text_msg = text_msg;
        this.time = time;
        this.belong = belong;
    }

    public String getText_msg() {
        return text_msg;
    }

    public String getTime() {
        return time;
    }

    public Boolean getBelong() {
        return belong;
    }
}
