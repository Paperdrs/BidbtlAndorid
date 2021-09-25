package com.android.bidbatl.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Login implements Serializable {
   public Integer code;
    public String message;
    public String metadata;
    public LoginData data;

    public static class LoginData implements Serializable {
        @SerializedName("id")
        public String id;
        public String role;
        public String email;
        public String phone;
        public String registered_name;
        public String gst_number;
        public String token;
        public String name;
        public String photo;
    }
}

//{
//        "code": 200,
//        "message": "Success",
//        "metadata": null,
//        "data": {
//        "id": "1",
//        "role": "admin",
//        "email": null,
//        "phone": "9718976267",
//        "registered_name": null,
//        "gst_number": null,
//        "token": "1255a3b2d3b8934f0fede83cf380ff82"
//        }
//        }