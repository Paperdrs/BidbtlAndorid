package com.android.Interface;

import com.android.bidbatl.Model.ProductListProvider;

public interface UpdateCartItem {
    void didUpdateCartItemFromMainActivity(ProductListProvider.ProductListData productListData, String qnty, int index);
    void getProductBycategoryId(String id);
    void refreshProductList(int index);
}
