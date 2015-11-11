package com.example.Bama.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.example.Bama.R;

import java.util.logging.Handler;

public class ActivityGroupTipsOnOff extends ActivityBase implements View.OnClickListener{

    private FrameLayout on_tip,off_tip;

    private String groupId = "";

	public static void open(Activity activity,String groupId){
		Intent intent = new Intent(activity,ActivityGroupTipsOnOff.class);
        intent.putExtra("groupId",groupId);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        groupId = getIntent().getStringExtra("groupId");
		setContentView(R.layout.activitiy_group_onoff);
		super.onCreate(savedInstanceState);
	}

	@Override
    protected void getViews() {

    }

    @Override
    protected void initViews() {
        findViewById(R.id.back_btn).setOnClickListener(this);
        on_tip = (FrameLayout) findViewById(R.id.on_tip);
        off_tip = (FrameLayout) findViewById(R.id.off_tip);
    }

    @Override
    protected void setListeners() {
        on_tip.setOnClickListener(this);off_tip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_btn:
                finish();
                break;
            case R.id.on_tip:
                Toast.makeText(this,"开启提醒",Toast.LENGTH_LONG).show();
                /**
                 * 屏蔽群消息后，就不能接 收到此群的消息 （群创建者不能屏蔽群消息）（还是群里面的成员，但不再接收消息）
                 * @param groupId， 群id
                 * @throws EasemobException
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMGroupManager.getInstance().unblockGroupMessage(groupId);//需异步处理
                        } catch (EaseMobException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.off_tip:
                Toast.makeText(this,"关闭提醒",Toast.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMGroupManager.getInstance().blockGroupMessage(groupId);//需异步处理
                        } catch (EaseMobException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }
}
