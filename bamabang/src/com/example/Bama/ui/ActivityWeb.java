package com.example.Bama.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import com.example.Bama.R;

public class ActivityWeb extends ActivityBase {
    private WebView webView;
    private String url;

    public static void open(ActivityBase activityBase,String url){
        Intent intent = new Intent(activityBase,ActivityWeb.class);
        intent.putExtra("url",url);
        activityBase.startActivity(intent);
    }

    @Override
    protected void getViews() {

    }

    @Override
    protected void initViews() {
        webView = (WebView) findViewById(R.id.webView);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web);
        super.onCreate(savedInstanceState);

        if(getIntent()!=null){
            url = getIntent().getStringExtra("url");
        }

        if(!TextUtils.isEmpty(url)){
            JavaScriptInterfaceUtil util = new JavaScriptInterfaceUtil(this);
            util.initWebView(webView, url);
        }
    }
}
