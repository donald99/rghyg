package com.example.Bama.ui.Views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Bama.Bean.GroupCircleEntity;
import com.example.Bama.R;
import com.example.Bama.chat.chatuidemo.utils.SmileUtils;

public class ViewGroupListItem extends LinearLayout {
	private Context context;
	private TextView mTitle, groupPeople, lastMsgText;
	private ImageView ivAvatar, hotImage;

	public GroupCircleEntity.ContentEntity entity;

	public ViewGroupListItem(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public ViewGroupListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	private void initView() {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		layoutInflater.inflate(R.layout.item_grouplist, this, true);

		ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
		mTitle = (TextView) findViewById(R.id.grouptitle);
		hotImage = (ImageView) findViewById(R.id.hotimage);
		groupPeople = (TextView) findViewById(R.id.grouppeople);
		lastMsgText = (TextView) findViewById(R.id.lastmsgtext);
	}

	public void setGroupListItem(GroupCircleEntity.ContentEntity entity) {
		this.entity = entity;
		if (entity == null) {
			return;
		}
		if (!TextUtils.isEmpty(entity.name)) {
			mTitle.setText(entity.name);
		} else {
			mTitle.setText("");
		}

		if (entity.status) {
			hotImage.setVisibility(View.VISIBLE);
		} else {
			hotImage.setVisibility(View.GONE);
		}
		groupPeople.setText(":" + entity.peopleCount);
		if (!TextUtils.isEmpty(entity.lastMsg)) {
			lastMsgText.setText(SmileUtils.getSmiledText(getContext(), entity.lastMsg), TextView.BufferType.SPANNABLE);
		} else {
			lastMsgText.setText("欢迎来到" + entity.name);
		}
	}
}
