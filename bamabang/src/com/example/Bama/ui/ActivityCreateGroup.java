package com.example.Bama.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Bama.R;

public class ActivityCreateGroup extends ActivityBase implements View.OnClickListener{

    private View backBtn;
    private ImageView headerView;
    private EditText etInputGroupName,etInputGroupCategory;
    private TextView mCreateGroup;

    public static void open(Activity activity){
        Intent intent = new Intent(activity,ActivityCreateGroup.class);
        activity.startActivity(intent);
    }

    @Override
    protected void getViews() {

    }

    @Override
    protected void initViews() {
        backBtn = findViewById(R.id.back_btn);
        headerView = (ImageView) findViewById(R.id.groupImage);
        etInputGroupName = (EditText) findViewById(R.id.et_input_group_name);
        etInputGroupCategory= (EditText) findViewById(R.id.et_input_group_category);
        mCreateGroup = (TextView) findViewById(R.id.creategroup);
    }

    @Override
    protected void setListeners() {
        backBtn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_group);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.creategroup:
                Toast.makeText(this,"creategroup",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
