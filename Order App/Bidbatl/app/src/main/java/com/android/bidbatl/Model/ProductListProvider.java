package com.android.bidbatl.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductListProvider {
    public  int code;
    int image;
    public String message;
    @SerializedName("metadata")
    public MetaData metaData;
    @SerializedName("data")
   public List<ProductListData>productListProviders = new ArrayList<>();

    public static class ProductListData{
        public String id;
        public String category_id;
        public String name;
        public Float weight;
        public Float mrp;
        public String cfc_quantity;
        public String cfc_volume;
        public String category_name;
        public String mother_pack_name;
        public String mother_pack_id;
        public String cfc_stock;
        public String product_image;
        public String photo;
        public String unit;
        public String price;
        public String product_cart_count;
        public String ordering_unit;
        public String cfc_mrp;
        public String current_price;

    }
    public static class MetaData{
        public String cart_count;
    }

}
