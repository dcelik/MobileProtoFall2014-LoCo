package com.example.flymperopoulos.loco;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by dcelik on 10/10/14.
 */
public class User {
    String username, phonenumber;
    Double latitude, longitude;
    ArrayList<String> flag;

    public User(){}

    //Public Constructor to create a user
    public User(String name, String num, Double lat, Double lon){
        this.username = name;
        this.phonenumber = num;
        this.latitude = lat;
        this.longitude = lon;
        this.flag = new ArrayList<String>();
    }

    /**
     * Get Fields
     */
    public String getName(){
        return this.username;
    }

    public String getPhoneNumber() { return this.phonenumber;}

    public Double getLatitude() {return this.latitude;}

    public Double getLongitude() {return this.longitude;}

    public ArrayList<String> getFlag() {return this.flag;}

    /**
     * Set Fields
     */
    public void setName(String value){
        this.username = toTitleCase(value);
    }

    public void setPhoneNumber(String value){
        this.phonenumber = toTitleCase(value);
    }

    public void setLatitude(Double value){
        this.latitude = value;
    }

    public void setLongitude(Double value){
        this.longitude = value;
    }

    public void setFlag(ArrayList<String> list) {
        this.flag = list;
    }

    public void addToFlag(String s){
        this.flag.add(s);
    }

    /**
     * Title Case Method
     */
    public String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}