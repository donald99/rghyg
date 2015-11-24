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
public class GroupInfoManager {
	private static HashMap<String, GroupInfoModel> groupInfoMap = new HashMap<String, GroupInfoModel>();

	public static void getGroupInfo(Activity activity, final String groupId, final ModelRequestListener<GroupInfoModel> listener) {
		/**返回测试的数据***/
		GroupInfoModel groupInfoModel = new GroupInfoModel();
		groupInfoModel.id = groupId;
		groupInfoModel.description = "这是群的描述";
		groupInfoModel.name = "群名字";
		if (listener != null) {
			listener.onModelComplete(groupInfoModel);
			return;
		}
		/**测试end***/
		if (TextUtils.isEmpty(groupId)) {
			if (listener != null) {
				listener.onModelComplete(null);
			}
		}
		if (groupInfoMap.containsKey(groupId)) {
			if (listener != null) {
				listener.onModelComplete(groupInfoMap.get(groupId));
			}
		} else {
			/**去服务器拉取**/
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("id", groupId));
			Request.doRequest(activity, valuePairs, ServerConfig.URL_GROUP_INFO_GET, Request.GET, new Request.RequestListener() {
				@Override
				public void onException(Request.RequestException e) {
					if (listener != null) {
						listener.onException(e);
					}
				}

				@Override
				public void onComplete(String response) {
					GroupInfoModel groupInfoModel = HCApplication.getInstance().getGson().fromJsonWithNoException(response, GroupInfoModel.class);
					if (groupInfoModel != null) {
						groupInfoMap.put(groupId, groupInfoModel);
						if (listener != null) {
							listener.onModelComplete(groupInfoModel);
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

	public static void clearGroupInfo() {
		groupInfoMap.clear();
	}


	public static class GroupInfoModel {
		@SerializedName("id")
		public String id;

		@SerializedName("groupid")
		public String groupid;

		@SerializedName("name")
		public String name;

		@SerializedName("tagid")
		public String tagid;

		@SerializedName("description")
		public String description;

		@SerializedName("bulletin")
		public String bulletin;

		@SerializedName("owner")
		public String owner;

		@SerializedName("status")
		public String status;

		@SerializedName("created")
		public String created;
	}
}
