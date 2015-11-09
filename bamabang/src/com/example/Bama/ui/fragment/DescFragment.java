package com.example.Bama.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.example.Bama.R;

/**
 * 群圈简介界面*
 */
public class DescFragment extends Fragment implements View.OnClickListener {

	private Activity activity;
    private String groupId = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_group_desc, container, false);
		initView(rootView);
        return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
        groupId = activity.getIntent().getStringExtra("groupId");

	}

    private void initView(View rootView){
        rootView.findViewById(R.id.add_group).setOnClickListener(this);
    }

	@Override
	public void onClick(View view) {

        switch (view.getId()){

            case R.id.add_group:{
                Toast.makeText(activity,"add_group",Toast.LENGTH_LONG).show();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //如果群开群是自由加入的，即group.isMembersOnly()为false，直接join
                        try {
                            EMGroupManager.getInstance().joinGroup(groupId);//需异步处
                        } catch (EaseMobException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
	}
}}
