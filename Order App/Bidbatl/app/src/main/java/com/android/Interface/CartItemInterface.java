package com.android.Interface;

public interface CartItemInterface {
    void didUpadetCartItem(String productid,String qty,int index,String price,String stock);
    void deleteCartItem(String id);
    void refreshProductList(int index);
}
