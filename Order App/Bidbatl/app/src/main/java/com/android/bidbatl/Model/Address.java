package com.android.bidbatl.Model;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Address implements Serializable {
    int code;
    @SerializedName("data")
   public List<AddressList> addressLists;

    public static class AddressList implements Serializable{
        public String id;
        public String user_id;
        public String latitude;
        public String logitude;
        public String address;
        public String area;
        public String city;
        public String zip_code;
        public String type;
        public String state;
        public int is_default;
        public int is_deleted;
        public String name;
        public String phone;
    }
}
