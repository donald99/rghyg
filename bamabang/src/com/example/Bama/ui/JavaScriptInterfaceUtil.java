package com.example.Bama.ui;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import android.content.Context;
import android.webkit.WebViewClient;

public class JavaScriptInterfaceUtil {

    private Context mContxt;

    public JavaScriptInterfaceUtil(Context mContxt) {
        this.mContxt = mContxt;
    }

    public void initWebView(WebView mWebView,String url){
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this, "mamihong");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(url);
    }

    @JavascriptInterface //sdk17版本以上加上注解
    public void openGroupChat(String groupId) {
        ActivityGroupChat.open((ActivityBase)mContxt, groupId);
    }
}
