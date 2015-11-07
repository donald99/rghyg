package com.example.Bama.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.example.Bama.R;
import com.example.Bama.Bean.GroupCircleEntity;
import com.example.Bama.adapter.GroupCircleAdapter;
import com.example.Bama.ui.ActivityGroupChat;

import java.util.ArrayList;

public class GroupCircleFragment extends Fragment {
	private final static String TAG = "GroupCircleFragment";
    private Activity activity;
    private ArrayList<GroupCircleEntity> groupCircleList = new ArrayList<GroupCircleEntity>();
    private ListView mListView;
    private GroupCircleAdapter mAdapter;
    private String text;
    private int channel_id;
    private ImageView detail_loading;
	public final static int SET_NEWSLIST = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle args = getArguments();
		text = args != null ? args.getString("text") : "";
		channel_id = args != null ? args.getInt("id", 0) : 0;
		initData();
		super.onCreate(savedInstanceState);
	}

    private void initData() {
        Toast.makeText(activity,"initData",Toast.LENGTH_LONG).show();

        for (int i = 0; i < 10; i++) {
            GroupCircleEntity entity = new GroupCircleEntity();
            entity.groupId = i+"";

            entity.groupTitle = "群名称";
            entity.groupImage = "";
            if (i>1){
                entity.isHot = false;
            }else{
                entity.isHot = true;
            }
            entity.peopleCount = "20";
            entity.lastMsg = "lastmsg";
            entity.masterId = "masterId";
            entity.masterName = "masterName";

            groupCircleList.add(entity);
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
			if(groupCircleList !=null && groupCircleList.size() !=0){
				handler.obtainMessage(SET_NEWSLIST).sendToTarget();
			}else{
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
		}else{

		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.group_list_fragment, null);
		mListView = (ListView) view.findViewById(R.id.mListView);
		TextView item_textview = (TextView)view.findViewById(R.id.item_textview);
		detail_loading = (ImageView)view.findViewById(R.id.detail_loading);

		item_textview.setText(text);
		return view;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SET_NEWSLIST:
				detail_loading.setVisibility(View.GONE);
				if(mAdapter == null){
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
