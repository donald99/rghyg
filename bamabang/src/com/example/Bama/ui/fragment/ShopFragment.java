package com.example.Bama.ui.fragment;

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
 * 购物界面*
 */
public class ShopFragment extends Fragment {
	private ActivityBase activity;
	private WebView webView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_shop, null);
        webView = (WebView) rootView.findViewById(R.id.webView);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = (ActivityBase) getActivity();
        JavaScriptInterfaceUtil util = new JavaScriptInterfaceUtil(activity);
        util.initWebView(webView, Base.shopUrl);
	}


}
