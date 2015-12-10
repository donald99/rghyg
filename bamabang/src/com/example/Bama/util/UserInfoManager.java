package com.example.Bama.util;

import android.app.Activity;
import android.text.TextUtils;
import com.example.Bama.background.HCApplication;
import com.example.Bama.background.config.ServerConfig;
import com.example.Bama.ui.JavaScriptInterfaceUtil;
import com.example.Bama.ui.RequestUtil;
import com.meilishuo.gson.annotations.SerializedName;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 用户信息缓存类*
 */
public class UserInfoManager {
	private static HashMap<String, UserInfoModel> userInfoMap = new HashMap<String, UserInfoModel>();

	public static void getUserInfo(Activity activity, final String userName, final ModelRequestListener<UserInfoModel> listener) {
		/**返回测试的数据***/
		UserInfoModel userInfoModel = new UserInfoModel();
		if (listener != null) {
			listener.onModelComplete(userInfoModel);
			return;
		}
		/**测试end***/
		if (TextUtils.isEmpty(userName)) {
			if (listener != null) {
				listener.onModelComplete(null);
			}
		}
		if (userInfoMap.containsKey(userName)) {
			if (listener != null) {
				listener.onModelComplete(userInfoMap.get(userName));
			}
		} else {
			/**去服务器拉取**/
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            try {
                object.put("username",userName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(object);
            RequestUtil.getUserInfo(activity, array.toString(), new JavaScriptInterfaceUtil.GetJsonLinister() {
                @Override
                public void onSuccess(String response) {
                    UserInfoModel userInfoModel = HCApplication.getInstance().getGson().fromJsonWithNoException(response, UserInfoModel.class);
                    if (userInfoModel != null) {
                        userInfoMap.put(userName, userInfoModel);
                        if (listener != null) {
                            listener.onModelComplete(userInfoModel);
                        }
                    } else {
                        if (listener != null) {
                            listener.onModelComplete(null);
                        }
                    }
                }

                @Override
                public void onFail() {
                    if (listener != null) {
//                        listener.onException();
                    }
                }
            });
		}
	}

	public static void clearUserInfo() {
		userInfoMap.clear();
	}


	public static class UserInfoModel {
        @SerializedName("status")
        public String status;

        @SerializedName("code")
        public String code;

        @SerializedName("message")
        public String message;

        @SerializedName("id")
        public String id;

        @SerializedName("uid")
        public String uid;
        /**
         * 环信的用户id*
         */
        @SerializedName("username")
        public String username;

        @SerializedName("name")
        public String name;

        @SerializedName("password")
        public String password;

        @SerializedName("gender")
        public String gender;

        @SerializedName("created")
        public String created;

        @SerializedName("baby")
        public String baby;

        @SerializedName("avatar")
        public String avatar;

		@SerializedName("privilege")
		public List<String> privilege;

        @SerializedName("content")
        public List<UserInfoModel> content;
	}
}
