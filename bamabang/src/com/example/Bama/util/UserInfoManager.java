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

/**
 * 用户信息缓存类*
 */
public class UserInfoManager {
	private static HashMap<String, UserInfoModel> userInfoMap = new HashMap<String, UserInfoModel>();

	public static void getUserInfo(Activity activity, final String userId, final ModelRequestListener<UserInfoModel> listener) {
		/**返回测试的数据***/
		UserInfoModel userInfoModel = new UserInfoModel();
		if (listener != null) {
			listener.onModelComplete(userInfoModel);
			return;
		}
		/**测试end***/
		if (TextUtils.isEmpty(userId)) {
			if (listener != null) {
				listener.onModelComplete(null);
			}
		}
		if (userInfoMap.containsKey(userId)) {
			if (listener != null) {
				listener.onModelComplete(userInfoMap.get(userId));
			}
		} else {
			/**去服务器拉取**/
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            try {
                object.put("id",userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(object);
            RequestUtil.getUserInfo(activity, array.toString(), new JavaScriptInterfaceUtil.GetJsonLinister() {
                @Override
                public void onSuccess(String response) {
                    UserInfoModel userInfoModel = HCApplication.getInstance().getGson().fromJsonWithNoException(response, UserInfoModel.class);
                    if (userInfoModel != null) {
                        userInfoMap.put(userId, userInfoModel);
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

        @SerializedName("name")
        public String name;

        @SerializedName("avatar")
        public String avatar;
        /**
         * 环信的用户id*
         */
        @SerializedName("username")
        public String username;

        @SerializedName("password")
        public String password;

		@SerializedName("privilege")
		public List<String> privilege;

        @SerializedName("content")
        public List<UserInfoModel> content;
	}
}
