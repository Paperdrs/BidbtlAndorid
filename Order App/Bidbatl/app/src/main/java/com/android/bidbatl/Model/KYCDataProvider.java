package com.android.bidbatl.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KYCDataProvider {
    public int code;
    public String message;
    @SerializedName("data")
    public List<KYCList>kycLists;

    public static class KYCList{
        public String id;
        public String user_id;
        public String type;
        public String id_number;
        public String name;
        public String photo;
        public String status;
    }
}
