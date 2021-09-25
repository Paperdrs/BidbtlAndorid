package com.android.bidbatl.Utility;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
    Context context;

    public LocalStorage(Context context) {
        this.context = context;
    }
    public void setId(Context context, String id) {
        SharedPreferences sh = context.getSharedPreferences("userId", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();
        edit.putString("userId", id);
        edit.apply();
    }

    public String getId() {
        SharedPreferences sh = context.getSharedPreferences("userId", Context.MODE_PRIVATE);
        return sh.getString("userId", "");
    }

    public void setName(Context context, String name) {
        SharedPreferences sh = context.getSharedPreferences("name", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();
        edit.putString("name", name);
        edit.apply();
    }

    public String getName() {
        SharedPreferences sh = context.getSharedPreferences("name", Context.MODE_PRIVATE);
        return sh.getString("name", "");
    }
    public void setPhone(Context context, String phone) {
        SharedPreferences sh = context.getSharedPreferences("phone", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();
        edit.putString("phone", phone);
        edit.apply();
    }

    public String getPhone() {
        SharedPreferences sh = context.getSharedPreferences("phone", Context.MODE_PRIVATE);
        return sh.getString("phone", "");
    }
}
