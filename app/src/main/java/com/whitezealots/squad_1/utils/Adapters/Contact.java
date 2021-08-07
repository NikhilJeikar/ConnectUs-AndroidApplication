package com.whitezealots.squad_1.utils.Adapters;

public class Contact {
    private final String Name;
    private final String Number;
    private final String  Image;

    public Contact(String Name, String Number, String Image) {
        this.Name = Name;
        this.Number = Number;
        this.Image = Image;
    }

    public String getName() {
        return Name;
    }

    public String getNumber() {
        return Number;
    }

    public String getImage() {
        return Image;
    }
}
