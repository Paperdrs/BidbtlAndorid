package com.android.bidbatl.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BidBatlPoints {
    public int code;
    public String message;
    @SerializedName("data")
    public List<PointList> pointList;

    public static class PointList{
        public String created_at;
        public String activity;
        public String order_id;
        public String point;
        public  String  order_number;
        public String type;
        public String total_point;
    }
}
