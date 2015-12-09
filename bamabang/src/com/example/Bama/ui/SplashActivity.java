package com.example.Bama.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;
import com.example.Bama.R;

public class SplashActivity extends ActivityBase{
    @Override
    protected void getViews() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {

    }

    private final int SPLASH_DISPLAY_LENGHT = 3000; //延迟三秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_mysplash);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, ActivityMain.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGHT);
    }

    protected String getVersion() {
        String versionStr = "";
        PackageManager packManger = getPackageManager();
        try {
            PackageInfo info = packManger.getPackageInfo(getPackageName(), 0);
            versionStr = info.versionName;//获得版本号
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            versionStr = "";
            e.printStackTrace();
        }
        return versionStr;

    }
}
