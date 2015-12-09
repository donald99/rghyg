package com.example.Bama.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.Bama.R;
import com.example.Bama.ui.ActivityBase;
import com.example.Bama.ui.Base;
import com.example.Bama.ui.JavaScriptInterfaceUtil;

/**
 * 发现界面*
 */
public class FindFragment extends Fragment {
	private ActivityBase activity;
	private WebView webView;

    public static String url = "";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_find, null);
        webView = (WebView) rootView.findViewById(R.id.webView);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = (ActivityBase) getActivity();
        webLoadUrl(url);
	}

    private void webLoadUrl(String url) {
        JavaScriptInterfaceUtil util = new JavaScriptInterfaceUtil(activity);
        util.initWebView(webView, url);
    }

    @Override
    public void onResume() {
        super.onResume();
        webLoadUrl(url);
    }
}
