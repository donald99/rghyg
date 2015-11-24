package com.example.Bama.ui;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import android.content.Context;
import android.webkit.WebViewClient;
import com.example.Bama.background.Account;
import com.example.Bama.background.HCApplication;
import com.example.Bama.util.UserInfoManager;
import org.json.JSONException;
import org.json.JSONObject;

public class JavaScriptInterfaceUtil {

	private Context mContxt;

	public JavaScriptInterfaceUtil(Context mContxt) {
		this.mContxt = mContxt;
	}

	public void initWebView(WebView mWebView, String url) {
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
		ActivityGroupChat.open((ActivityBase) mContxt, groupId);
	}

	/**
	 * 用户登录成功后的js回调*
	 */
	@JavascriptInterface
	public void loginSuccess(String json) {
		UserInfoManager.UserInfoModel userInfoModel = HCApplication.getInstance().getGson().fromJsonWithNoException(json, UserInfoManager.UserInfoModel.class);
		if (userInfoModel != null) {
			Account account = HCApplication.getInstance().getAccount();
			account.phoneNumber = userInfoModel.phoneNumber;
			account.password = userInfoModel.password;
			/**这个username是环信的userid**/
			account.userId = userInfoModel.username;
			account.userName = userInfoModel.name;
			account.avatar = userInfoModel.avatar;
			account.saveMeInfoToPreference();
		}
	}

	/**
	 * 用户注册成功后的js回调*
	 */
	@JavascriptInterface
	public void registerSuccess(String json) {
		UserInfoManager.UserInfoModel userInfoModel = HCApplication.getInstance().getGson().fromJsonWithNoException(json, UserInfoManager.UserInfoModel.class);
		if (userInfoModel != null) {
			Account account = HCApplication.getInstance().getAccount();
			account.phoneNumber = userInfoModel.phoneNumber;
			account.password = userInfoModel.password;
			/**这个username是环信的userid**/
			account.userId = userInfoModel.username;
			account.userName = userInfoModel.name;
			account.avatar = userInfoModel.avatar;
			account.saveMeInfoToPreference();
		}
	}
}
