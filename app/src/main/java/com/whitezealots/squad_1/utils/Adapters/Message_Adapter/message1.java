package com.whitezealots.squad_1.utils.Adapters.Message_Adapter;

public class message1 {
    private String msg1;
    private String time;
    private Boolean belongToCurrentUser;

    public message1(String msg1, String time, Boolean belongToCurrentUser) {
        this.msg1 = msg1;
        this.time = time;
        this.belongToCurrentUser = belongToCurrentUser;
    }


    public Boolean getBelongToCurrentUser() {
        return belongToCurrentUser;
    }

    public String getMsg1() {
        return msg1;
    }

    public String getTime() {
        return time;
    }
}
