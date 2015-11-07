package com.example.Bama.ui;


import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.example.Bama.R;

public class ActivityGroupTipsOnOff extends ActivityBase implements View.OnClickListener{

    private FrameLayout on_tip,off_tip;

    @Override
    protected void getViews() {

    }

    @Override
    protected void initViews() {
        findViewById(R.id.back_btn).setOnClickListener(this);
        on_tip = (FrameLayout) findViewById(R.id.on_tip);
        off_tip = (FrameLayout) findViewById(R.id.off_tip);
    }

    @Override
    protected void setListeners() {
        on_tip.setOnClickListener(this);off_tip.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activitiy_group_onoff);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_btn:
                finish();
                break;
            case R.id.on_tip:
                Toast.makeText(this,"on_tip",Toast.LENGTH_LONG).show();
                break;
            case R.id.off_tip:
                Toast.makeText(this,"on_tip",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
