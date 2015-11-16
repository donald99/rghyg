package com.example.Bama.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.easemob.chat.*;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.example.Bama.R;
import com.example.Bama.Bean.GroupCircleEntity;
import com.example.Bama.adapter.GroupCircleAdapter;
import com.example.Bama.chat.chatuidemo.Constant;
import com.example.Bama.ui.ActivityGroupChat;
import com.example.Bama.ui.RequestUtil;
import com.example.Bama.ui.Views.ViewGroupListItem;
import com.example.Bama.util.ToastUtil;
import com.example.Bama.widget.RefreshListView;

import java.util.*;

/**
 * 单个群列表*
 */
public class GroupCircleFragment extends Fragment implements RefreshListView.OnRefreshListener, RefreshListView.OnLoadMoreListener {
	private final static String TAG = "GroupCircleFragment";
	private Activity activity;
	private List<GroupCircleEntity.ContentEntity> groupCircleList = new ArrayList<GroupCircleEntity.ContentEntity>();
	private RefreshListView mListView;
	private GroupCircleAdapter mAdapter;
	private String text;
	private int channel_id;
	private ImageView detail_loading;
	public final static int SET_NEWSLIST = 0;

	/**
	 * 环信分页*
	 */
	private int page = 1;
	private static final int pageSize = 10;
	private String cursor = null;

	/**
	 * 联系人会话列表*
	 */
	private HashMap<String, EMConversation> conversationMap = new HashMap<String, EMConversation>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.group_list_fragment, null);
		mListView = (RefreshListView) view.findViewById(R.id.mListView);

		mListView.setCanRefresh(true);
		mListView.setCanLoadMore(false);
		mListView.setOnRefreshListener(this);
		mListView.setOnLoadListener(this);

		TextView item_textview = (TextView) view.findViewById(R.id.item_textview);
		detail_loading = (ImageView) view.findViewById(R.id.detail_loading);
		item_textview.setText(text);

		Bundle args = getArguments();
		text = args != null ? args.getString("text") : "";
		channel_id = args != null ? args.getInt("id", 0) : 0;

		return view;
	}

	@Override
	public void onRefresh() {
		/**制造数据**/
		if (text.equals("同城")) {
			new Thread(new Runnable() {
				@Override
				public boolean equals(Object o) {
					return super.equals(o);
				}

				@Override
				public void run() {
					try {
						/**加载所有的回话**/
						conversationMap.clear();
						List<EMConversation> conversations = loadConversationsWithRecentChat();
						for (EMConversation conversation : conversations) {
							conversationMap.put(conversation.getUserName(), conversation);
						}
						EMCursorResult<EMGroupInfo> cursorResult = EMGroupManager.getInstance().getPublicGroupsFromServer(pageSize, cursor);
						if (cursorResult != null) {
							cursor = cursorResult.getCursor();
							/**有数据**/
							if (cursorResult.getData() != null && cursorResult.getData().size() > 0) {
								List<EMGroupInfo> datas = cursorResult.getData();
								groupCircleList.clear();
								for (EMGroupInfo groupInfo : datas) {
									GroupCircleEntity.ContentEntity entity = new GroupCircleEntity.ContentEntity();
									entity.groupid = groupInfo.getGroupId();
									entity.name = groupInfo.getGroupName();
									/**计算人数**/
									EMGroup group = EMGroupManager.getInstance().getGroupFromServer(groupInfo.getGroupId());
									entity.peopleCount = group.getMembers().size();
									EMConversation emConversation = conversationMap.get(groupInfo.getGroupId());
									if (emConversation == null || emConversation.getMsgCount() == 0) {
										entity.lastMsg = "";
									} else {
										entity.lastMsg = getMessageDigest(emConversation.getLastMessage(), getActivity());
									}
									entity.ownerid = "masterId";
									entity.owner = "masterName";
									groupCircleList.add(entity);
								}
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										mListView.onRefreshComplete();
										mAdapter.notifyDataSetChanged();
										if (TextUtils.isEmpty(cursor)) {
											mListView.setCanLoadMore(false);
										} else {
											mListView.setCanLoadMore(true);
										}
									}
								});
							} else {
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										mListView.onRefreshComplete();
										mListView.setCanLoadMore(false);
									}
								});
							}
						}
					} catch (EaseMobException e) {
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mListView.onRefreshComplete();
								ToastUtil.makeShortText("获取群组失败");
							}
						});
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			groupCircleList.clear();
			RequestUtil.queryTagGroupList(activity, channel_id + "", new QueryTagGroupListCallback() {
				@Override
				public void onSuccess(List<GroupCircleEntity.ContentEntity> entitys) {
					if (entitys != null) {
						groupCircleList.addAll(entitys);
						mListView.onLoadMoreComplete();
						mAdapter.notifyDataSetChanged();
					}
				}

				@Override
				public void onFail() {
					//TODO:onFail 再次请求逻辑

				}
			});
		}
	}

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 *
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 位置消息
			if (message.direct == EMMessage.Direct.RECEIVE) {
				// 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
				// digest = EasyUtils.getAppResourceString(context,
				// "location_recv");
				digest = getStrng(context, R.string.location_recv);
				digest = String.format(digest, message.getFrom());
				return digest;
			} else {
				// digest = EasyUtils.getAppResourceString(context,
				// "location_prefix");
				digest = getStrng(context, R.string.location_prefix);
			}
			break;
		case IMAGE: // 图片消息
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
			digest = getStrng(context, R.string.picture) + imageBody.getFileName();
			break;
		case VOICE:// 语音消息
			digest = getStrng(context, R.string.voice);
			break;
		case VIDEO: // 视频消息
			digest = getStrng(context, R.string.video);
			break;
		case TXT: // 文本消息
			if (!message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = txtBody.getMessage();
			} else {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = getStrng(context, R.string.voice_call) + txtBody.getMessage();
			}
			break;
		case FILE: // 普通文件消息
			digest = getStrng(context, R.string.file);
			break;
		default:
			EMLog.e(TAG, "unknow type");
			return "";
		}

		return digest;
	}

	String getStrng(Context context, int resId) {
		return context.getResources().getString(resId);
	}

	/**
	 * 获取所有会话
	 *
	 * @param
	 * @return
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
		 * 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变
		 * 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					//if(conversation.getType() != EMConversationType.ChatRoom){
					sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
					//}
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 *
	 * @param
	 */
	private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
			@Override
			public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

				if (con1.first == con2.first) {
					return 0;
				} else if (con2.first > con1.first) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	@Override
	public void onLoadMore() {
		page++;

		/**制造数据**/
		if (text.equals("同城")) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						EMCursorResult<EMGroupInfo> cursorResult = EMGroupManager.getInstance().getPublicGroupsFromServer(pageSize, cursor);
						if (cursorResult != null) {
							cursor = cursorResult.getCursor();
							if (cursorResult.getData() != null && cursorResult.getData().size() > 0) {
								List<EMGroupInfo> datas = cursorResult.getData();
								for (EMGroupInfo groupInfo : datas) {
									GroupCircleEntity.ContentEntity entity = new GroupCircleEntity.ContentEntity();
									entity.groupid = groupInfo.getGroupId();

									entity.name = groupInfo.getGroupName();
									//									entity.peopleCount = "20";
									//									entity.lastMsg = "lastmsg";
									entity.ownerid = "owner";
									entity.owner = "owner";
									groupCircleList.add(entity);
								}
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										mListView.onLoadMoreComplete();
										mAdapter.notifyDataSetChanged();
										if (TextUtils.isEmpty(cursor)) {
											mListView.setCanLoadMore(false);
										} else {
											mListView.setCanLoadMore(true);
										}
									}
								});
							} else {
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										mListView.onLoadMoreComplete();
										mListView.setCanLoadMore(false);
									}
								});
							}
						}
					} catch (EaseMobException e) {
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mListView.onLoadMoreComplete();
								ToastUtil.makeShortText("获取群组失败");
							}
						});
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			RequestUtil.queryTagGroupList(activity, channel_id + "", new QueryTagGroupListCallback() {
				@Override
				public void onSuccess(List<GroupCircleEntity.ContentEntity> entitys) {
					if (entitys != null) {
						groupCircleList.addAll(entitys);
						mListView.onLoadMoreComplete();
						mAdapter.notifyDataSetChanged();
					}
				}

				@Override
				public void onFail() {
					page--;
					mListView.onLoadMoreComplete();
					//TODO:onFail 再次请求逻辑
				}
			});
		}
	}

	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser) {
			if (groupCircleList != null && groupCircleList.size() != 0) {
				handler.obtainMessage(SET_NEWSLIST).sendToTarget();
			} else {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						handler.obtainMessage(SET_NEWSLIST).sendToTarget();
					}
				}).start();
			}
		} else {

		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SET_NEWSLIST:
				detail_loading.setVisibility(View.GONE);
				if (mAdapter == null) {
					mAdapter = new GroupCircleAdapter(activity, groupCircleList);
				}
				mListView.setAdapter(mAdapter);
				mListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if (view instanceof ViewGroupListItem) {
							ViewGroupListItem item = (ViewGroupListItem) view;
							if (item.entity != null && !TextUtils.isEmpty(item.entity.groupid)) {
								ActivityGroupChat.open(activity, item.entity.groupid);
							} else {
								ToastUtil.makeShortText("群id为空");
							}
						}
					}
				});
				onRefresh();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mAdapter = null;
	}

	public interface QueryTagGroupListCallback {
		public void onSuccess(List<GroupCircleEntity.ContentEntity> entitys);

		public void onFail();
	}
}
