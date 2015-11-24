package com.example.Bama.util;

/**
 * Created by xiaoyu on 15-11-13.
 */

import android.app.Activity;
import android.text.TextUtils;
import com.example.Bama.background.HCApplication;
import com.example.Bama.background.config.ServerConfig;
import com.meilishuo.gson.annotations.SerializedName;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
		userInfoModel.id = userId;
		userInfoModel.username = userId;
		userInfoModel.name = "劲爆美女";
		userInfoModel.avatar = "http://p1.so.qhimg.com/t01a8a1f419472cedbb.jpg";
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
			valuePairs.add(new BasicNameValuePair("id", userId));
			Request.doRequest(activity, valuePairs, ServerConfig.URL_USER_GET, Request.GET, new Request.RequestListener() {
				@Override
				public void onException(Request.RequestException e) {
					if (listener != null) {
						listener.onException(e);
					}
				}

				@Override
				public void onComplete(String response) {
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
			});
		}
	}

	public static void clearUserInfo() {
		userInfoMap.clear();
	}


	public static class UserInfoModel {
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

		/**需要新增加的字段**/
		@SerializedName("phoneNumber")
		public String phoneNumber;
	}
}
