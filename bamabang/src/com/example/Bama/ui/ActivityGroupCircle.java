package com.example.Bama.ui;

import android.content.Intent;
import android.os.Bundle;
import com.example.Bama.R;

public class ActivityGroupCircle extends ActivityBase {

    public static void openWithTab(ActivityBase activityBase,int tab){
        Intent intent = new Intent(activityBase,ActivityGroupCircle.class);
        intent.putExtra("tab",tab);
        activityBase.startActivity(intent);
    }

    @Override
    protected void getViews() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actity_groupcircle);
    }
}
