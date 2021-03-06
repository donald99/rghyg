package com.example.Bama.ui;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import android.content.Context;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.example.Bama.R;
import com.example.Bama.background.Account;
import com.example.Bama.background.HCApplication;
import com.example.Bama.chat.chatuidemo.Constant;
import com.example.Bama.chat.chatuidemo.db.UserDao;
import com.example.Bama.chat.chatuidemo.domain.User;
import com.example.Bama.chat.chatuidemo.utils.CommonUtils;
import com.example.Bama.chat.chatuidemo.video.util.Utils;
import com.example.Bama.util.StringUtil;
import com.example.Bama.util.ToastUtil;
import com.example.Bama.util.UserInfoManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                load(view, url);
                return true;
            }
        });
        load(mWebView, url);
	}

    private void load(WebView view ,String url){
        boolean isContainOperationParam = url.contains(Base.OperationParam);
        if(isContainOperationParam){
            String jsonString = StringUtil.getUrlInfo(url);

            opertion( view , url,jsonString);

        }else{
            view.loadUrl(url);
        }
    }

    public void openGroupChat(String groupId) {
		ActivityGroupChat.open((ActivityBase) mContxt, groupId);
	}

	public void openGroupCircleWithTab(int tabindex) {
		ActivityGroupCircle.openWithTab((ActivityBase) mContxt, tabindex);
	}
	/**
	 * 用户登录成功后的js回调*
	 */
	public void loginSuccess(String json) {
		UserInfoManager.UserInfoModel userInfoModel = HCApplication.getInstance().getGson().fromJsonWithNoException(json, UserInfoManager.UserInfoModel.class);
		if (userInfoModel != null && userInfoModel.content!=null && userInfoModel.content.size()>0) {
			Account account = HCApplication.getInstance().getAccount();
			account.password = userInfoModel.content.get(0).password;
			/**这个username是环信的userid**/
			account.userId = userInfoModel.content.get(0).uid;
			account.userName = userInfoModel.content.get(0).username;
			account.avatar = userInfoModel.content.get(0).avatar;
			account.saveMeInfoToPreference();
            account.toLoginChatServer(mContxt,account.userId,account.password);
		}
	}

	/**
	 * 用户注册成功后的js回调*
	 */
	public void registerSuccess(String json) {
		UserInfoManager.UserInfoModel userInfoModel = HCApplication.getInstance().getGson().fromJsonWithNoException(json, UserInfoManager.UserInfoModel.class);
		if (userInfoModel != null && userInfoModel.content!=null && userInfoModel.content.size()>0) {
			Account account = HCApplication.getInstance().getAccount();
			account.password = userInfoModel.content.get(0).password;
			/**这个username是环信的userid**/
			account.userId = userInfoModel.content.get(0).uid;
			account.userName = userInfoModel.content.get(0).username;
			account.avatar = userInfoModel.content.get(0).avatar;
			account.saveMeInfoToPreference();
		}
	}

    /**
     * 用户退出成功后的js回调*
     */
    public void clearAccount() {
        Account account = HCApplication.getInstance().getAccount();
        account.clearMeInfo();
    }

    /**
     * 用户退出成功后的js回调*
     */
    public void close(Activity activity) {
        if (activity!=null){
            activity.finish();
        }
    }

    private void opertion(WebView view ,String url ,String jsonString) {

        try {
            JSONObject object = new JSONObject(jsonString);
            if(object.has("jump")){
                //  http://www.baidu.com?ClientOperation={"jump":"group”,”jumpindex”:0}
                //  跳转app原生页面
                //  jump字段，string类型，有该字段时，group跳转到群组页面 没有该字段不跳转
                //  jumpindex字段，int类型，0，1，2，3，4，5… 有该字段，跳转到对应索引，没有默认索引为0
                String mjump = object.optString("jump");
                int mbreak = object.optInt("break");
                int mjumpindex = object.optInt("jumpindex");
                openGroupCircleWithTab(mjumpindex);
            }

            if(object.has("login")){
                //  http://www.baidu.com?ClientOperation={"login":1,"uid":"123456"}
                //  登录状态
                //  login字段，int类型，有该字段时，1表示登录成功，0表示退出  没有该字段则忽略
                //  uid字段，string类型，用户id
                int mlogin = object.optInt("login");
                if(mlogin==0){
                    clearAccount();
                }else{
                    String uid = object.optString("uid");

                    JSONArray array = new JSONArray();
                    JSONObject object1 = new JSONObject();
                    object1.put("username",uid);
                    array.put(object1);

                    RequestUtil.getUserInfo(mContxt,array.toString(),new GetJsonLinister(){
                        @Override
                        public void onSuccess(String json) {
                            loginSuccess(json);
                        }

                        @Override
                        public void onFail() {

                        }
                    });
                }
            }

            if(object.has("menuindex")){
            //http://www.baidu.com?ClientOperation={"menuindex”:0}
            //menuindex，int类型，参数值：0，1，2，3，4分别对应底部索引。
            // 没有该参数默认显示当前索引。

                int menuIndex = object.optInt("menuindex");
                //显示底部菜单，并且 选中这个menuIndex索引


            }

            if(object.has("menu")){
                //http://www.baidu.com?ClientOperation={"menu”:1}
                //menu字段，int类型，有该字段时，1表示不显示底部菜单，
                // 其他值表示显示菜单没有该字段默认当前菜单显示状态
                int menu = object.optInt("menu");
                if (menu == 1){

                }else{

                }
            }

            if(object.has("break")){
                //http://www.baidu.com?ClientOperation={"break":1}
                //break字段，int类型，有该字段是，1表示不执行跳转操作，只是和app传递消息使用其他值或没有该字段表示执行跳转操作
                int mbreak = object.optInt("break");
                if (mbreak==1){

                }else{
                    view.loadUrl(url);
                }
            }else{
                view.loadUrl(url);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface GetJsonLinister{
        public void onSuccess(String json);
        public void onFail();
    }
}
