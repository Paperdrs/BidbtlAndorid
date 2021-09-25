package com.android.Interface;

import com.android.bidbatl.Model.MotherPackModel;

public class MotherPackEvent {
    MotherPackModel.CategoryList categoryList;

    public MotherPackEvent(MotherPackModel.CategoryList categoryList) {
        this.categoryList = categoryList;
    }
}
