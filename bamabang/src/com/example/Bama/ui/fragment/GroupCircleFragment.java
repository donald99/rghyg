package com.example.Bama.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.easemob.chat.EMCursorResult;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.example.Bama.R;
import com.example.Bama.Bean.GroupCircleEntity;
import com.example.Bama.adapter.GroupCircleAdapter;
import com.example.Bama.ui.ActivityGroupChat;
import com.example.Bama.util.ToastUtil;
import com.example.Bama.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 单个群列表*
 */
public class GroupCircleFragment extends Fragment implements RefreshListView.OnRefreshListener, RefreshListView.OnLoadMoreListener {
	private final static String TAG = "GroupCircleFragment";
	private Activity activity;
	private ArrayList<GroupCircleEntity> groupCircleList = new ArrayList<GroupCircleEntity>();
	private RefreshListView mListView;
	private GroupCircleAdapter mAdapter;
	private String text;
	private int channel_id;
	private ImageView detail_loading;
	public final static int SET_NEWSLIST = 0;

	/**
	 * 环信分页*
	 */
	private static final int pageSize = 10;
	private String cursor = null;

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
		if (text.equals("环信真实群组")) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						EMCursorResult<EMGroupInfo> cursorResult = EMGroupManager.getInstance().getPublicGroupsFromServer(pageSize, cursor);
						if (cursorResult != null) {
							cursor = cursorResult.getCursor();
							/**有数据**/
							if (cursorResult.getData() != null && cursorResult.getData().size() > 0) {
								List<EMGroupInfo> datas = cursorResult.getData();
								groupCircleList.clear();
								for (EMGroupInfo groupInfo : datas) {
									GroupCircleEntity entity = new GroupCircleEntity();
									entity.groupId = groupInfo.getGroupId();

									entity.groupTitle = groupInfo.getGroupName();
									entity.groupImage = "";
									entity.peopleCount = "20";
									entity.lastMsg = "lastmsg";
									entity.masterId = "masterId";
									entity.masterName = "masterName";
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
			for (int i = 0; i < 10; i++) {
				GroupCircleEntity entity = new GroupCircleEntity();
				entity.groupId = i + "";

				entity.groupTitle = "群名称";
				entity.groupImage = "";
				if (i > 1) {
					entity.isHot = false;
				} else {
					entity.isHot = true;
				}
				entity.peopleCount = "20";
				entity.lastMsg = "lastmsg";
				entity.masterId = "masterId";
				entity.masterName = "masterName";

				groupCircleList.add(entity);
			}
			mListView.onRefreshComplete();
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoadMore() {
		/**制造数据**/
		if (text.equals("环信真实群组")) {
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
									GroupCircleEntity entity = new GroupCircleEntity();
									entity.groupId = groupInfo.getGroupId();

									entity.groupTitle = groupInfo.getGroupName();
									entity.groupImage = "";
									entity.peopleCount = "20";
									entity.lastMsg = "lastmsg";
									entity.masterId = "masterId";
									entity.masterName = "masterName";
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
			for (int i = 0; i < 10; i++) {
				GroupCircleEntity entity = new GroupCircleEntity();
				entity.groupId = i + "";

				entity.groupTitle = "群名称";
				entity.groupImage = "";
				if (i > 1) {
					entity.isHot = false;
				} else {
					entity.isHot = true;
				}
				entity.peopleCount = "20";
				entity.lastMsg = "lastmsg";
				entity.masterId = "masterId";
				entity.masterName = "masterName";

				groupCircleList.add(entity);
			}
			mListView.onLoadMoreComplete();
			mAdapter.notifyDataSetChanged();
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
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ActivityGroupChat.open(activity);
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "channel_id = " + channel_id);
	}
}
