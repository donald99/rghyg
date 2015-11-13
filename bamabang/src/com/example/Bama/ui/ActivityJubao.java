package com.example.Bama.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.Bama.Bean.GroupMemberRankEntity;
import com.example.Bama.R;
import com.example.Bama.adapter.GroupMemberRankAdapter;
import com.example.Bama.background.HCApplication;
import com.example.Bama.background.config.AppInfo;
import com.example.Bama.background.config.ServerConfig;
import com.example.Bama.util.Request;
import com.example.Bama.util.ToastUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ActivityJubao extends ActivityBase {

    private String group_id = "";
	public static void open(Activity activity){
		Intent intent = new Intent(activity,ActivityJubao.class);
		activity.startActivity(intent);
	}

    private TextView jubaoTextView;
    private EditText etJubaoInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_jubao);
		super.onCreate(savedInstanceState);
        group_id = getIntent().getStringExtra("group_id");
	}

    @Override
    protected void getViews() {

    }

    @Override
    protected void initViews() {
        jubaoTextView = (TextView) findViewById(R.id.tv_jubao);
        etJubaoInfo = (EditText) findViewById(R.id.et_jubao_info);

    }

    @Override
    protected void setListeners() {
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        jubaoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jubaoInfo = etJubaoInfo.getText().toString().trim();
                if(TextUtils.isEmpty(jubaoInfo)){
                    ToastUtil.makeLongText("输入举报信息");
                    return;
                }
                //FIXME:UID  Context context,String fortype,String froid, String jubaoInfo
                RequestUtil.jubaoGroup(ActivityJubao.this,"group",group_id,jubaoInfo);
            }
        });
    }


}
