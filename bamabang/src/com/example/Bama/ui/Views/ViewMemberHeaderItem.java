package com.example.Bama.ui.Views;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.Bama.Bean.GroupMemberEntity;
import com.example.Bama.R;
import com.example.Bama.background.HCApplication;
import com.example.Bama.ui.ActivityBase;
import com.example.Bama.ui.ChatInfoManager;
import com.example.Bama.util.ImageLoaderUtil;
import com.example.Bama.util.Request;

public class ViewMemberHeaderItem extends LinearLayout {
	private ImageView imageView;
	private ImageView isMaster;
	private GroupMemberEntity model;
	private ActivityBase activity;
	private TextView name;

	private ChatInfoManager.UserInfoModel userInfoModel;

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

	public void setData(GroupMemberEntity modelCategory) {
		this.model = modelCategory;
		name.setText(modelCategory.name);
		if (modelCategory.isMaster) {
			isMaster.setVisibility(View.VISIBLE);
		} else {
			isMaster.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(model.accountId)){
			ChatInfoManager.getUserInfo((Activity)getContext(),model.accountId,new ChatInfoManager.ModelRequestListener<ChatInfoManager.UserInfoModel>(){
				@Override
				public void onModelComplete(ChatInfoManager.UserInfoModel model) {
					userInfoModel = model;
					name.setText(userInfoModel.name);
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
