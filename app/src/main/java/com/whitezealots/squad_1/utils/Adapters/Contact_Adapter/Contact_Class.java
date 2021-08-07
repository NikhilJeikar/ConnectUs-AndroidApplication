package com.whitezealots.squad_1.utils.Adapters.Contact_Adapter;

public class Contact_Class {

    private final String Name;
    private final String Number;
    private final String User;
    private final String  Path;

    public Contact_Class(String Name, String Number, String User, String Path) {
        this.Name = Name;
        this.Number = Number;
        this.User = User;
        this.Path = Path;
    }

    public String getName() {
        return Name;
    }

    public String getNumber() {
        return Number;
    }

    public String getUser() {
        return User;
    }

    public String getPath() {
        return Path;
    }

}
