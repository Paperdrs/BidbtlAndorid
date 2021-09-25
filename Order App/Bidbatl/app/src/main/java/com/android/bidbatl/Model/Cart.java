package com.android.bidbatl.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    public int code;
    public String message;
    public CartData data;

    public static class CartData implements Serializable {
        @SerializedName("cart_item")
        public List<CartList>cartLists;
        public String count;
        public String subtotal;
        public String delivery_charges;
        public String grandtotal;
        public String gst;
        @SerializedName("default_address")
        public List<Address.AddressList> addressLists;

    }
    public static class CartList implements Serializable{
        public String cart_id;
        public String user_id;
        public String product_id;
        public String cart_item_quantity;
        public String id;
        public String category_id;
        public String name;
        public Float weight;
        public Float mrp;
        public String cfc_quantity;
        public String cfc_stock;
        public String category_name;
        public String product_image;
        public String unit;
        public String price;
        public String ordering_unit;
        public String status;
    }
}
