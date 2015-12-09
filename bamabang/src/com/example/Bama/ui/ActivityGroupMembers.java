package com.example.Bama.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.example.Bama.R;
import com.example.Bama.ui.Views.GroupMemberItemView;
import com.example.Bama.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;

public class ActivityGroupMembers extends ActivityBase implements View.OnClickListener {
	private ImageView back_btn;
	private RefreshListView mListView;

	private GroupMemberAdapter adapter;
	private List<String> userIds = new ArrayList<String>();

	public static final String kGroupId = "group_id";
	private String groupId = null;

	public static final String kUserId = "user_id";

	public static void open(Activity activity, String groupId, int requestCode) {
		Intent intent = new Intent(activity, ActivityGroupMembers.class);
		intent.putExtra(kGroupId, groupId);
		activity.startActivityForResult(intent, requestCode);
	}

	public static void open(Fragment fragment, String groupId, int requestCode) {
		Intent intent = new Intent(fragment.getActivity(), ActivityGroupMembers.class);
		intent.putExtra(kGroupId, groupId);
		fragment.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		groupId = getIntent().getStringExtra(kGroupId);
		setContentView(R.layout.activity_group_members);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		back_btn = (ImageView) findViewById(R.id.back_btn);
		mListView = (RefreshListView) findViewById(R.id.mListView);
	}

	@Override
	protected void initViews() {
		adapter = new GroupMemberAdapter();
		mListView.setCanRefresh(false);
		mListView.setCanLoadMore(false);

		showDialog();
		/**得到用户ids**/
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					EMGroup group = EMGroupManager.getInstance().getGroupFromServer(groupId);
					List<String> accountIds = group.getMembers();
					userIds.clear();
					userIds.addAll(accountIds);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mListView.setAdapter(adapter);
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
				dismissDialog();
			}
		}).start();

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(view instanceof GroupMemberItemView){
					String userId = ((GroupMemberItemView)view).userInfoModel.id;
					Intent intent = new Intent();
					intent.putExtra(kUserId, userId);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}

	@Override
	protected void setListeners() {
		back_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

	}

	private class GroupMemberAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userIds.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GroupMemberItemView itemView = null;
			if (convertView == null) {
				itemView = new GroupMemberItemView(ActivityGroupMembers.this);
			} else {
				itemView = (GroupMemberItemView) convertView;
			}
			itemView.setData(userIds.get(position));
			return itemView;
		}
	}
}
