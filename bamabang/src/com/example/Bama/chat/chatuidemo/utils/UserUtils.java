package com.example.Bama.chat.chatuidemo.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import com.example.Bama.background.HCApplication;
import com.example.Bama.chat.chatuidemo.domain.User;
import com.example.Bama.ui.UserInfoManager;
import com.example.Bama.util.ImageLoaderUtil;
import com.example.Bama.util.Request;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserUtils {
	/**
	 * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
	 *
	 * @param username
	 * @return
	 */
	public static User getUserInfo(String username) {
		User user = HCApplication.getInstance().getContactList().get(username);
		if (user == null) {
			user = new User(username);
		}

		if (user != null) {
			//demo没有这些数据，临时填充
			user.setNick(username);
			//            user.setAvatar("http://downloads.easemob.com/downloads/57.png");
		}
		return user;
	}

	/**
	 * 设置用户头像
	 *
	 * @param username
	 */
	public static void setUserAvatar(Context context, String username, final ImageView imageView) {
		final ImageLoader imageLoader = HCApplication.getInstance().getImageLoader();
		/**自己设置头像**/
		UserInfoManager.getUserInfo((Activity) context, username, new UserInfoManager.ModelRequestListener<UserInfoManager.UserInfoModel>() {
			@Override
			public void onModelComplete(UserInfoManager.UserInfoModel model) {
				if (model != null) {
					if (!TextUtils.isEmpty(model.avatar)) {
						imageLoader.displayImage(model.avatar, imageView, ImageLoaderUtil.Options_Common_memory_Pic);
					} else {
						imageLoader.displayImage("", imageView, ImageLoaderUtil.Options_Common_memory_Pic);
					}
				}
			}

			@Override
			public void onException(Request.RequestException e) {

			}
		});
	}
}
