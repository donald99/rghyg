package com.example.Bama.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.example.Bama.Bean.GroupMemberEntity;
import com.example.Bama.R;
import com.example.Bama.background.Account;
import com.example.Bama.background.HCApplication;
import com.example.Bama.ui.Views.ViewMemberHeaderItem;
import com.example.Bama.ui.fragment.*;
import com.example.Bama.util.DisplayUtil;
import com.example.Bama.widget.ColumnHorizontalScrollView;
import com.example.Bama.widget.XYBottomDialog;
import com.example.Bama.widget.XYGroupCustomerDialog;

import java.util.ArrayList;
import java.util.List;

public class ActivityGroupChat extends ActivityBase implements View.OnClickListener {
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	private LinearLayout mRadioGroup_content;
	public ImageView shade_left;
	public ImageView shade_right;

	private ArrayList<GroupMemberEntity> memberList = new ArrayList<GroupMemberEntity>();

	private ImageView more;
	private FrameLayout descFL;
	private TextView descText;
	private FrameLayout groupChatFL;
	private TextView groupChatText;
	private FrameLayout rankFL;
	private TextView rankText;

	private Fragment mFragmentCurrent;

	public DescFragment fragmentDesc;
	public GroupChatFragment fragmentGroupChat;
	public RankFragment fragmentRank;

	private List<FrameLayout> frameLayouts = new ArrayList<FrameLayout>();

	/**
	 * 是否加入群的标志位*
	 */
	public boolean isJoinGroup = false;
	public Account account;

	/**
	 * intent data*
	 */
	public static final String kGroupId = "group_id";
	private String groupId;

	public static void open(Activity activity, String groupId) {
		Intent intent = new Intent(activity, ActivityGroupChat.class);
		intent.putExtra(kGroupId, groupId);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (getIntent() != null) {
			groupId = getIntent().getStringExtra(kGroupId);
		}
		account = HCApplication.getInstance().getAccount();
		setContentView(R.layout.activity_group_chat);
		super.onCreate(savedInstanceState);
		/**初始化群成员头像**/
		initHeaderColumn();
	}

	@Override
	protected void getViews() {
		more = (ImageView) findViewById(R.id.more);
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
	}

	@Override
	protected void initViews() {
		frameLayouts.add(descFL);
		frameLayouts.add(groupChatFL);
		frameLayouts.add(rankFL);
	}

	@Override
	protected void setListeners() {
		findViewById(R.id.back_btn).setOnClickListener(this);
		more.setOnClickListener(this);
		descFL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateImageViewsStatus(0);
				if (fragmentDesc == null) {
					fragmentDesc = new DescFragment();
					Bundle bundle = new Bundle();
					bundle.putString(DescFragment.kGroupId, groupId);
					fragmentDesc.setArguments(bundle);
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
					Bundle bundle = new Bundle();
					bundle.putString(GroupChatFragment.kGroupId, groupId);
					fragmentGroupChat.setArguments(bundle);
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
		Bundle bundle = new Bundle();
		bundle.putString(DescFragment.kGroupId, groupId);
		fragmentDesc.setArguments(bundle);
		transaction.add(R.id.fragementLayout, fragmentDesc);
		transaction.commit();
		mFragmentCurrent = fragmentDesc;
		updateImageViewsStatus(0);
	}

	/**
	 * 初始化群成员*
	 */
	private void initHeaderColumn() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					EMGroup group = EMGroupManager.getInstance().getGroupFromServer(groupId);
					String ownerId = group.getOwner();
					List<String> accountIds = group.getMembers();
					/**判断自己是否加入群**/
					if (!TextUtils.isEmpty(account.userId) && accountIds.contains(account.userId)) {
						isJoinGroup = true;
					} else {
						isJoinGroup = false;
					}
					memberList.clear();
					for (String accountId : accountIds) {
						GroupMemberEntity entity = new GroupMemberEntity();
						/**通过自己服务器的accountid去注册环信**/
						entity.name = accountId;
						entity.accountId = accountId;
						entity.avatar = "http://img.name2012.com/uploads/allimg/2015-06/30-023131_451.jpg";
						if (!TextUtils.isEmpty(ownerId) && ownerId.equals(accountId)) {
							entity.isMaster = true;
						} else {
							entity.isMaster = false;
						}
						memberList.add(entity);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mRadioGroup_content.removeAllViews();
								for (int i = 0; i < memberList.size(); i++) {
									LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
									if (i != memberList.size() - 1) {
										params.rightMargin = DisplayUtil.dip2px(ActivityGroupChat.this, 13);
									}
									ViewMemberHeaderItem item = new ViewMemberHeaderItem(ActivityGroupChat.this);
									item.setData(memberList.get(i));
									mRadioGroup_content.addView(item, params);
								}
							}
						});
					}
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onBackPressed() {
		if (XYBottomDialog.isShowing(this)) {
			XYBottomDialog.onBackPressed(this);
			return;
		}
		if (XYGroupCustomerDialog.isShowing(this)) {
			XYGroupCustomerDialog.onBackPressed(this);
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_btn:
			finish();
			break;
		case R.id.more:
			if (XYBottomDialog.hasDlg(this)) {
				XYBottomDialog.getDlgView(this).show();
			} else {
				XYBottomDialog.showDialog(this, new XYBottomDialog.XYShareDialogListener() {
					@Override
					public void groupMessageTip() {
						ActivityGroupTipsOnOff.open(ActivityGroupChat.this);
					}

					@Override
					public void reportGroup() {
						ActivityJubao.open(ActivityGroupChat.this);
					}

					@Override
					public void exitGroup() {
						Toast.makeText(ActivityGroupChat.this, "exitGroup", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void cancel() {
					}
				});
			}
			break;
		case R.id.creategroup:
			Toast.makeText(this, "creategroup", Toast.LENGTH_LONG).show();
			break;
		}
	}
}
