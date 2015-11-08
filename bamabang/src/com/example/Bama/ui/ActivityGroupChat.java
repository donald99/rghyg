package com.example.Bama.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.*;
import com.example.Bama.Bean.GroupMemberEntity;
import com.example.Bama.R;
import com.example.Bama.ui.Views.ViewMemberHeaderItem;
import com.example.Bama.ui.fragment.*;
import com.example.Bama.util.DisplayUtil;
import com.example.Bama.widget.ColumnHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

public class ActivityGroupChat extends ActivityBase implements View.OnClickListener {
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	private LinearLayout mRadioGroup_content;
	public ImageView shade_left;
	public ImageView shade_right;

	private ArrayList<GroupMemberEntity> memberList = new ArrayList<GroupMemberEntity>();

	private FrameLayout descFL;
	private TextView descText;
	private FrameLayout groupChatFL;
	private TextView groupChatText;
	private FrameLayout rankFL;
	private TextView rankText;

	private Fragment mFragmentCurrent;

	private DescFragment fragmentDesc;
	private GroupChatFragment fragmentGroupChat;
	private RankFragment fragmentRank;

	private List<FrameLayout> frameLayouts = new ArrayList<FrameLayout>();

	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityGroupChat.class);
		activity.startActivity(intent);
	}

	@Override
	protected void getViews() {

	}

	@Override
	protected void initViews() {
		mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
		mColumnHorizontalScrollView.setParam(this, DisplayUtil.getWindowsWidth(this), mRadioGroup_content, shade_left, shade_right);

		descFL = (FrameLayout) findViewById(R.id.descFl);
		descText = (TextView) findViewById(R.id.descText);

		groupChatFL = (FrameLayout) findViewById(R.id.groupchatFL);
		groupChatText = (TextView) findViewById(R.id.groupchatText);

		rankFL = (FrameLayout) findViewById(R.id.rankFL);
		rankText = (TextView) findViewById(R.id.rankText);

		frameLayouts.add(descFL);
		frameLayouts.add(groupChatFL);
		frameLayouts.add(rankFL);
	}

	@Override
	protected void setListeners() {
		findViewById(R.id.back_btn).setOnClickListener(this);
		descFL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateImageViewsStatus(0);
				if (fragmentDesc == null) {
					fragmentDesc = new DescFragment();
				}
				switchContent(mFragmentCurrent, fragmentDesc);
			}
		});

		groupChatFL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateImageViewsStatus(1);
				if (fragmentGroupChat == null) {
					fragmentGroupChat = new GroupChatFragment();
				}
				switchContent(mFragmentCurrent, fragmentGroupChat);
			}
		});

		rankFL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateImageViewsStatus(2);
				if (fragmentRank == null) {
					fragmentRank = new RankFragment();
				}
				switchContent(mFragmentCurrent, fragmentRank);
			}
		});

		setDefaultFragment();
	}

	private void updateImageViewsStatus(int index) {
		for (int i = 0; i < frameLayouts.size(); i++) {
			FrameLayout view = frameLayouts.get(i);
			if (i == index) {
				setSelected(view, true);
			} else {
				setSelected(view, false);
			}
		}
	}

	private void setSelected(FrameLayout view, boolean bool) {
		if (bool) {
			view.setBackgroundColor(getResources().getColor(R.color.title_bg));
			((TextView) view.getChildAt(0)).setTextColor(getResources().getColor(R.color.common_white));
		} else {
			view.setBackgroundColor(getResources().getColor(R.color.common_white));
			((TextView) view.getChildAt(0)).setTextColor(getResources().getColor(R.color.title_font));
		}
	}

	public void switchContent(Fragment from, Fragment to) {
		if (from != to) {
			mFragmentCurrent = to;
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			if (!to.isAdded()) {
				if (from != null && from.isAdded()) {
					transaction.hide(from);
				}
				// 隐藏当前的fragment，add下一个到Activity中
				transaction.add(R.id.fragementLayout, to).commitAllowingStateLoss();
			} else {
				// 隐藏当前的fragment，显示下一个
				transaction.hide(from).show(to).commitAllowingStateLoss();
			}
		}
	}

	private void setDefaultFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		fragmentDesc = new DescFragment();
		transaction.add(R.id.fragementLayout, fragmentDesc);
		transaction.commit();
		mFragmentCurrent = fragmentDesc;
		updateImageViewsStatus(0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_group_chat);
		super.onCreate(savedInstanceState);

		for (int i = 0; i < 10; i++) {
			GroupMemberEntity entity = new GroupMemberEntity();
			entity.name = "name";
			entity.accountId = "123123";
			entity.avatar = "http://img.name2012.com/uploads/allimg/2015-06/30-023131_451.jpg";
			entity.isMaster = false;
			memberList.add(entity);
		}
		initHeaderColumn(memberList);
	}

	private void initHeaderColumn(List<GroupMemberEntity> models) {
		mRadioGroup_content.removeAllViews();
		for (final GroupMemberEntity model : models) {
			ViewMemberHeaderItem item = new ViewMemberHeaderItem(ActivityGroupChat.this);
			item.setData(model);
			mRadioGroup_content.addView(item);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_btn:
			finish();
			break;
		case R.id.creategroup:
			Toast.makeText(this, "creategroup", Toast.LENGTH_LONG).show();
			break;
		}
	}
}
