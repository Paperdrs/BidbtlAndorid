package com.android.Interface;

import com.android.bidbatl.Model.Address;

public interface AddressInterface {
    void deleteAddress(String id);
    void setDefault(String id,String type);
    void getSelectedAddress(Address.AddressList addressList);
}
