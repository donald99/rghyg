package com.example.Bama.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.easemob.EMCallBack;
import com.easemob.chat.*;
import com.easemob.exceptions.EaseMobException;
import com.example.Bama.Bean.GroupCircleEntity;
import com.example.Bama.Bean.GroupCreateInfoEntity;
import com.example.Bama.R;
import com.example.Bama.background.Account;
import com.example.Bama.background.HCApplication;
import com.example.Bama.ui.ActivityBase;
import com.example.Bama.ui.ActivityCreateGroup;
import com.example.Bama.ui.ActivityGroupChat;
import com.example.Bama.ui.RequestUtil;
import com.example.Bama.util.ImageLoaderUtil;
import com.example.Bama.util.ToastUtil;

import java.util.List;

/**
 * 群圈简介界面*
 */
public class DescFragment extends Fragment implements View.OnClickListener {
	private ActivityBase activity;

	private Account account;

	private ImageView groupAvatar;
	private TextView group_people_count;
	private TextView group_desc;
	private TextView edit_group_desc;
	private TextView add_group;
	private TextView added_group;

    public static final String kGroupInfo = "kGroupId";
    private String groupInfo = "";

    private GroupCircleEntity.ContentEntity entity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        groupInfo = getArguments().getString(kGroupInfo);
        entity = HCApplication.getInstance().getGson().fromJsonWithNoException(groupInfo,GroupCircleEntity.ContentEntity.class);
		account = HCApplication.getInstance().getAccount();
		View rootView = inflater.inflate(R.layout.fragment_group_desc, container, false);
		initView(rootView);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (ActivityBase) activity;
	}

	private void initView(View rootView) {
		groupAvatar = (ImageView) rootView.findViewById(R.id.groupAvatar);
		group_people_count = (TextView) rootView.findViewById(R.id.group_people_count);
		group_desc = (TextView) rootView.findViewById(R.id.group_desc);
		edit_group_desc = (TextView) rootView.findViewById(R.id.edit_group_desc);
		add_group = (TextView) rootView.findViewById(R.id.add_group);
		added_group = (TextView) rootView.findViewById(R.id.added_group);

		groupAvatar.setOnClickListener(this);
		edit_group_desc.setOnClickListener(this);
		add_group.setOnClickListener(this);
		added_group.setOnClickListener(this);

        if ((entity.owner).equals(HCApplication.getInstance().getAccount().userId)){
            edit_group_desc.setVisibility(View.VISIBLE);
        }else{
            edit_group_desc.setVisibility(View.GONE);
        }

        HCApplication.getInstance().getImageLoader().displayImage(entity.picurl, groupAvatar, ImageLoaderUtil.Options_Common_memory_Pic);
        group_desc.setText(entity.description);

        isInGroup();
	}

    private void isInGroup() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					EMGroup group = EMGroupManager.getInstance().getGroupFromServer(entity.groupid);
					final List<String> userNames = group.getMembers();
                    if(activity!=null){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                group_people_count.setText(userNames.size() + "");
                            }
                        });
                    }
					/**判断自己是否加入群**/
					if (!TextUtils.isEmpty(account.userName) && userNames.contains(account.userName)) {
                        if(activity!=null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    add_group.setVisibility(View.GONE);
                                    added_group.setVisibility(View.VISIBLE);
                                    GroupChatFragment.isJoinGroup = true;
						        }
						    });
                        }
					} else {
                        if(activity!=null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    add_group.setVisibility(View.VISIBLE);
                                    added_group.setVisibility(View.GONE);
                                    GroupChatFragment.isJoinGroup = false;
                                }
						    });
                        }
					}
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.add_group:
			if (activity != null) {
				activity.showDialog("正在加入...");
			}

            sendAddText(HCApplication.getInstance().getAccount().userName + "加入群组");

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						EMGroupManager.getInstance().joinGroup(entity.groupid);//需异步处
                        final EMGroup group = EMGroupManager.getInstance().getGroupFromServer(entity.groupid);
                        if(activity!=null){
                            activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								add_group.setVisibility(View.GONE);
								added_group.setVisibility(View.VISIBLE);
                                if(activity instanceof ActivityGroupChat){
                                    ((ActivityGroupChat)activity).initHeaderColumn();
                                }
                                final List<String> accountIds = group.getMembers();
                                group_people_count.setText(accountIds.size() + "");
						    	}
						});
                        }
					} catch (EaseMobException e) {
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ToastUtil.makeShortText("加入失败");
							}
						});
						e.printStackTrace();
					}
                    if(activity!=null){
                        activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (activity != null) {
								activity.dismissDialog();
							}
					    	}
					});
                    }
				}
			}).start();
			break;
            case R.id.edit_group_desc:
                if(activity!=null && !TextUtils.isEmpty(groupInfo)){
                    ActivityCreateGroup.open(activity,groupInfo);
                }
                break;
		}
	}

    private void sendAddText(String content) {
        if(entity!=null){
            //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
            EMConversation conversation = EMChatManager.getInstance().getConversation(entity.groupid);
            //创建一条文本消息
            final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            setMessageAttribute(message);
            //如果是群聊，设置chattype,默认是单聊
            message.setChatType(EMMessage.ChatType.GroupChat);
            //设置消息body
            TextMessageBody txtBody = new TextMessageBody(content);
            message.addBody(txtBody);
            //设置接收人
            message.setReceipt(entity.groupid);
            //把消息加入到此会话对象中
            conversation.addMessage(message);
            //发送消息
            message.setUnread(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(int i, String s) {
                        }

                        @Override
                        public void onProgress(int i, String s) {
                        }
                    });
                }
            }).start();
        }

    }

    private void setMessageAttribute(EMMessage msg){
        Account account=HCApplication.getInstance().getAccount();
        if(msg!=null && account!=null){
            msg.setAttribute("nickname",account.name);
            msg.setAttribute("username",account.userName);
            msg.setAttribute("uid",account.userId);
            msg.setAttribute("headImg",account.avatar);
            msg.setAttribute("type","1");
        }
    }
}
