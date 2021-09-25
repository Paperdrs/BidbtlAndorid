package com.android.bidbatl.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetails {
    public int code;
    public String message;
    public OrderData data;

    public static class OrderData{
        @SerializedName("order_item")
        public List<OrderDetailList> orderDetailLists;
        public String count;
        public String subtotal;
        public String delivery_charges;
        public String grandtotal;
    }

    public static class OrderDetailList{
        public String id;
        public String user_id;
        public String vehicle_id;
        public String number;
        public String total_amount;
        public String payment_status;
        public String status;
        public String created_at;
        public String updated_at;
        public String name;
        public String quantity;
        public String unit_price;
        public String weight;
        public String product_image;
    }
}
