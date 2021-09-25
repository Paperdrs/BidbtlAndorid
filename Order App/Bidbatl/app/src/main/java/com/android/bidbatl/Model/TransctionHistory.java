package com.android.bidbatl.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransctionHistory {
    public int code;
    public String message;
    @SerializedName("data")
    public List<TransctionHistory.MyTransctions> transctionsList;
    public static class MyTransctions{
        public String id;
        public String user_id;
        public String number;
        public float total_amount;
        public String payment_status;
        public String status;

        @SerializedName("cashback_points")
        public CashbackPoints cashbackPoints;
        @SerializedName("order")
        public Order order;
        @SerializedName("paid_points")
        public PaidPoints paidPoints;
        @SerializedName("payment")
        public Payment payment;
    }

    public static class CashbackPoints{
        public String timestamp;
        public String invoice_no;
        public String transaction_type;
        public String amount;
    }
    public static class Order{
        public String timestamp;
        public String invoice_no;
        public String transaction_type;
        public String amount;
    }
    public static class PaidPoints{
        public String timestamp;
        public String invoice_no;
        public String transaction_type;
        public String amount;
    }
    public static class Payment{
        public String timestamp;
        public String invoice_no;
        public String transaction_type;
        public String amount;
    }

}