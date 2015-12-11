package com.example.Bama.ui.Views;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.Bama.R;
import com.example.Bama.background.HCApplication;
import com.example.Bama.util.ModelRequestListener;
import com.example.Bama.util.UserInfoManager;
import com.example.Bama.util.ImageLoaderUtil;
import com.example.Bama.util.Request;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GroupMemberItemView extends FrameLayout {
	private ImageView avatar;
	private TextView nickName;

	public UserInfoManager.UserInfoModel userInfoModel;
	private ImageLoader imageLoader;

	public GroupMemberItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public GroupMemberItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public GroupMemberItemView(Context context) {
		super(context);
		init(context);
	}

	public void init(Context context) {
		imageLoader = HCApplication.getInstance().getImageLoader();
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.group_member_item_view, this, true);

		avatar = (ImageView) findViewById(R.id.avatar);
		nickName = (TextView) findViewById(R.id.nickName);
	}

	public void setData(String userId) {
		if (TextUtils.isEmpty(userId)) {
			return;
		}

		UserInfoManager.getUserInfo((Activity) getContext(), userId, new ModelRequestListener<UserInfoManager.UserInfoModel>() {
			@Override
			public void onModelComplete(UserInfoManager.UserInfoModel model) {
				userInfoModel = model;
                if(userInfoModel!=null && userInfoModel.content!=null &&userInfoModel.content.size()>0){
                    if (!TextUtils.isEmpty(userInfoModel.content.get(0).avatar)) {
                        imageLoader.displayImage(userInfoModel.content.get(0).avatar, avatar, ImageLoaderUtil.Options_Common_memory_Pic);
                    } else {
                        imageLoader.displayImage("", avatar, ImageLoaderUtil.Options_Common_memory_Pic);
                    }
                    if (!TextUtils.isEmpty(userInfoModel.content.get(0).username)) {
                        nickName.setText(userInfoModel.content.get(0).username);
                    } else {
                        nickName.setText("");
                    }
                }
			}

			@Override
			public void onException(Request.RequestException e) {

			}
		});
	}
}
