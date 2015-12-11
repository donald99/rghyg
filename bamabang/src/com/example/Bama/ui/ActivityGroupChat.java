package com.example.Bama.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.Bama.Bean.GroupCircleEntity;
import com.example.Bama.Bean.GroupCreateInfoEntity;
import com.example.Bama.Bean.GroupMemberEntity;
import com.example.Bama.R;
import com.example.Bama.background.Account;
import com.example.Bama.background.HCApplication;
import com.example.Bama.chat.chatuidemo.video.util.Utils;
import com.example.Bama.ui.Views.ViewMemberHeaderItem;
import com.example.Bama.ui.fragment.*;
import com.example.Bama.util.DisplayUtil;
import com.example.Bama.util.Request;
import com.example.Bama.util.ToastUtil;
import com.example.Bama.util.UserInfoManager;
import com.example.Bama.widget.ColumnHorizontalScrollView;
import com.example.Bama.widget.XYBottomDialog;
import com.example.Bama.widget.XYGroupCustomerDialog;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityGroupChat extends ActivityBase implements View.OnClickListener {
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	private LinearLayout mRadioGroup_content;
	public ImageView shade_left;
	public ImageView shade_right;

	private List<UserInfoManager.UserInfoModel> memberList = new ArrayList<UserInfoManager.UserInfoModel>();

	private ImageView more;
	private FrameLayout descFL;
	private TextView title,descText;
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
	public static final String kGroupInfo = "group_Info";
    public GroupCircleEntity.ContentEntity entity;

	public static void open(Activity activity, String groupInfo) {
		Intent intent = new Intent(activity, ActivityGroupChat.class);
		intent.putExtra(kGroupInfo, groupInfo);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (getIntent() != null) {
            entity = HCApplication.getInstance().getGson().fromJsonWithNoException(getIntent().getStringExtra(kGroupInfo), GroupCircleEntity.ContentEntity.class);
		}
		account = HCApplication.getInstance().getAccount();
		setContentView(R.layout.activity_group_chat);
		super.onCreate(savedInstanceState);
        if(account==null || TextUtils.isEmpty(account.userName)){
            ToastUtil.makeLongText("请先登录");
            finish();
            return;
        }
		/**初始化群成员头像**/
		initHeaderColumn();
	}

	@Override
	protected void getViews() {
		title = (TextView) findViewById(R.id.title);
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

        if(entity!=null){
            title.setText(entity.name);
        }
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
					bundle.putString(DescFragment.kGroupInfo, HCApplication.getInstance().getGson().toJson(entity));
					fragmentDesc.setArguments(bundle);
				}
				switchContent(mFragmentCurrent, fragmentDesc);
                DisplayUtil.hideSolftInput(ActivityGroupChat.this,descFL);
			}
		});

		groupChatFL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
                if(TextUtils.isEmpty(account.userName)){
                    ToastUtil.makeLongText("请先登录");
                    finish();
                    return;
                }else{
                    if(!TextUtils.isEmpty(account.userName)){
                        account.toLoginChatServer(ActivityGroupChat.this,account.userName,account.password);
                    }
                    updateImageViewsStatus(1);
                    if (fragmentGroupChat == null) {
                        fragmentGroupChat = new GroupChatFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(GroupChatFragment.kGroupId, entity.groupid);
                        fragmentGroupChat.setArguments(bundle);
                    }
                    switchContent(mFragmentCurrent, fragmentGroupChat);
                }
                DisplayUtil.hideSolftInput(ActivityGroupChat.this,groupChatFL);
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
                DisplayUtil.hideSolftInput(ActivityGroupChat.this,rankFL);
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
        bundle.putString(DescFragment.kGroupInfo, HCApplication.getInstance().getGson().toJson(entity));
		fragmentDesc.setArguments(bundle);
		transaction.add(R.id.fragementLayout, fragmentDesc);
		transaction.commit();
		mFragmentCurrent = fragmentDesc;
		updateImageViewsStatus(0);
	}

	/**
	 * 初始化群成员*
	 */
	public void initHeaderColumn() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					EMGroup group = EMGroupManager.getInstance().getGroupFromServer(entity.groupid);
					String ownerId = group.getOwner();
					List<String> accountIds = group.getMembers();
					/**判断自己是否加入群**/
					if (!TextUtils.isEmpty(account.userName) && accountIds.contains(account.userName)) {
						isJoinGroup = true;
					} else {
						isJoinGroup = false;
					}
					memberList.clear();

                    Message msg = Message.obtain();
                    msg.obj = accountIds;
                    myHandler.sendMessage(msg);

				} catch (EaseMobException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

    View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            XYGroupCustomerDialog.showDialog(ActivityGroupChat.this,view, new XYGroupCustomerDialog.XYGroupCustomerDialogListener() {
                @Override
                public void onAtTaClicked() {

                    try {
                        EMGroup group = EMGroupManager.getInstance().getGroupFromServer(entity.groupid);
                        List<String> accountIds = group.getMembers();
                        if (!TextUtils.isEmpty(account.userName) && accountIds.contains(account.userName)) {
                        }else{
                            ToastUtil.makeLongText("非群组成员");
                            return ;
                        }
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }

                    if (listener!=null){
                        UserInfoManager.UserInfoModel member = (UserInfoManager.UserInfoModel) view.getTag();
                        listener.onAttalistener(member.uid);
                    }
                }

                @Override
                public void onReportClicked() {
                    UserInfoManager.UserInfoModel member = (UserInfoManager.UserInfoModel) view.getTag();
                    ActivityJubao.open(ActivityGroupChat.this,member.uid,false);
                }

                @Override
                public void onDeleteClicked() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //把username从群聊里删除
                            try {
                                EMGroupManager.getInstance().removeUserFromGroup(entity.groupid, ((UserInfoManager.UserInfoModel) view.getTag()).uid);//需异步处理
                            } catch (EaseMobException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

                @Override
                public void onCancelClicked() {
                }
            });
        }
    };

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
						ActivityGroupTipsOnOff.open(ActivityGroupChat.this,entity.groupid);
					}

					@Override
					public void reportGroup() {
						ActivityJubao.open(ActivityGroupChat.this,entity.groupid,true);
					}

					@Override
					public void exitGroup() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EMGroupManager.getInstance().exitFromGroup(entity.groupid);//需异步处理
                                } catch (EaseMobException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        finish();
                        Toast.makeText(ActivityGroupChat.this, "退出该群", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void cancel() {
					}
				});
			}
			break;
		}
	}

    private void getUserInfo(List<String> userNameArray){
        List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
        JSONArray array = new JSONArray();
        try {
            if (userNameArray!=null){
                for(int i =0;i<userNameArray.size() ;i++){
                    JSONObject object = new JSONObject();
                    object.put("username",userNameArray.get(i));
                    array.put(object);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtil.getUserInfo(ActivityGroupChat.this, array.toString(), new JavaScriptInterfaceUtil.GetJsonLinister() {
            @Override
            public void onSuccess(String response) {
                UserInfoManager.UserInfoModel userInfoModel = HCApplication.getInstance().getGson().fromJsonWithNoException(response, UserInfoManager.UserInfoModel.class);
                memberList = userInfoModel.content;
                mRadioGroup_content.removeAllViews();
                for (int i = 0; i < memberList.size(); i++) {
                    UserInfoManager.UserInfoModel member = memberList.get(i);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if (i != memberList.size() - 1) {
                        params.rightMargin = DisplayUtil.dip2px(ActivityGroupChat.this, 13);
                    }
                    ViewMemberHeaderItem item = new ViewMemberHeaderItem(ActivityGroupChat.this);
                    item.setData(member);
                    item.setOnClickListener(ClickListener);
                    item.setTag(member);
                    mRadioGroup_content.addView(item, params);
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

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<String> accountIds = (List<String>) msg.obj;
            getUserInfo(accountIds);
        }
    };

    public ATTAListener listener;
    public void setATTAListener(ATTAListener  l){
        this.listener = l;
    }
    public interface ATTAListener{
        public void onAttalistener(String atusername);
    }
}
