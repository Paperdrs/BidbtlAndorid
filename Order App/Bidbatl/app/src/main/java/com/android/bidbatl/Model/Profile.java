package com.android.bidbatl.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Profile implements Serializable {
    public int code;

    @SerializedName("data")
    public UserProfile userProfile;
    public static class UserProfile implements Serializable{
        public String id;
        public String role;
        public String name;
        public String registered_name;
        public String email;
        public String phone;
        public String gst_number;
        public String photo;
        public String address;
        public String point;
        public String latitude;
        public String logitude;


    }
}
