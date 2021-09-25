package com.android.bidbatl.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrder {
    public int code;
    public String message;
    @SerializedName("data")
    public List<MyOrderList> myOrderLists;

    public static class MyOrderList{
        public String id;
        public String user_id;
        @SerializedName("number")
        public String orderNumber;
        public float total_amount;
        public String payment_status;
        public String status;
        public String created_at;
        public String updated_at;
        public String bill_url;
        public String invoice_url;
    }
}
