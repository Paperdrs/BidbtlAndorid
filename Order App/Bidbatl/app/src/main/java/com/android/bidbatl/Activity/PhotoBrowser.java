package com.android.bidbatl.Activity;

import android.media.browse.MediaBrowser;
import android.os.Bundle;

import com.zeuskartik.mediaslider.MediaSliderActivity;

import java.util.ArrayList;

public class PhotoBrowser extends MediaSliderActivity {
    ArrayList<String> mStringList= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String url = getIntent().getStringExtra("galleryURL");

        mStringList.add(url);
        super.onCreate(savedInstanceState);
        loadMediaSliderView(mStringList,"image",true,true,false,"Image-Slider","#000000",null,0);
    }
}
