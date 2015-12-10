package com.example.Bama.ui.Views;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.Bama.R;
import com.example.Bama.background.HCApplication;
import com.example.Bama.ui.ActivityBase;
import com.example.Bama.util.ModelRequestListener;
import com.example.Bama.util.UserInfoManager;
import com.example.Bama.util.ImageLoaderUtil;
import com.example.Bama.util.Request;

public class ViewMemberHeaderItem extends LinearLayout {
	private ImageView imageView;
	private ImageView isMaster;
	private ActivityBase activity;
	private TextView name;

	private UserInfoManager.UserInfoModel userInfoModel;

	public ViewMemberHeaderItem(Context context) {
		super(context);
		initView(context);
	}

	public ViewMemberHeaderItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		activity = (ActivityBase) context;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.view_group_member, this);
		imageView = (ImageView) findViewById(R.id.image);
		name = (TextView) findViewById(R.id.name);
		isMaster = (ImageView) findViewById(R.id.isMaster);
	}

	public void setData(UserInfoManager.UserInfoModel modelCategory) {
		this.userInfoModel = modelCategory;
		name.setText(modelCategory.username);
//		if (modelCategory.) {
//			isMaster.setVisibility(View.VISIBLE);
//		} else {
//			isMaster.setVisibility(View.GONE);
//		}

        if (!TextUtils.isEmpty(userInfoModel.avatar)) {
            HCApplication.getInstance().getImageLoader().displayImage(userInfoModel.avatar, imageView, ImageLoaderUtil.Options_Memory_Rect_Avatar);
        } else {
            HCApplication.getInstance().getImageLoader().displayImage("", imageView, ImageLoaderUtil.Options_Memory_Rect_Avatar);
        }

		if(!TextUtils.isEmpty(userInfoModel.username)){
			UserInfoManager.getUserInfo((Activity) getContext(), userInfoModel.username, new ModelRequestListener<UserInfoManager.UserInfoModel>() {
				@Override
				public void onModelComplete(UserInfoManager.UserInfoModel model) {
					userInfoModel = model;
					name.setText(userInfoModel.username);
					if (!TextUtils.isEmpty(userInfoModel.avatar)) {
						HCApplication.getInstance().getImageLoader().displayImage(model.avatar, imageView, ImageLoaderUtil.Options_Memory_Rect_Avatar);
					} else {
						HCApplication.getInstance().getImageLoader().displayImage("", imageView, ImageLoaderUtil.Options_Memory_Rect_Avatar);
					}
				}

				@Override
				public void onException(Request.RequestException e) {
				}
			});
		}
	}
}
