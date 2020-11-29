package com.whitezealots.squad_1.utils.Adapters.Contact_Adapter;

public class Contact_Class {

    private String name,num,mynum;

    public Contact_Class(String name, String num, String mynum) {
        this.name = name;
        this.num = num;
        this.mynum = mynum;
    }

    public String getMynum() {
        return mynum;
    }

    public String getName() {
        return name;
    }

    public String getNum() {
        return num;
    }
}
