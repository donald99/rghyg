package com.example.Bama.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.example.Bama.R;

import java.io.InputStreamReader;

public class ActivityJubao extends ActivityBase {

	public static void open(Activity activity){
		Intent intent = new Intent(activity,ActivityJubao.class);
		activity.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_jubao);
		super.onCreate(savedInstanceState);
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
}
