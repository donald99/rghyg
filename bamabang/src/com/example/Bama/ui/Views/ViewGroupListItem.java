package com.example.Bama.ui.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Bama.Bean.GroupCircleEntity;
import com.example.Bama.R;

public class ViewGroupListItem extends LinearLayout {
    private Context context;
    private TextView mTitle,groupPeople,lastMsgText;
    private ImageView ivAvatar, hotImage;

    public ViewGroupListItem(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ViewGroupListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.item_grouplist, this, true);

        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        mTitle = (TextView) findViewById(R.id.grouptitle);
        hotImage = (ImageView) findViewById(R.id.hotimage);
        groupPeople = (TextView) findViewById(R.id.grouppeople);
        lastMsgText = (TextView) findViewById(R.id.lastmsgtext);
    }

   private GroupCircleEntity entity;
    public void setGroupListItem(GroupCircleEntity entity) {
        this.entity = entity;
        setUI();
    }

    private void setUI() {
        if (entity == null) {
            return;
        }

        mTitle.setText(entity.groupTitle);
        if(entity.isHot){
            hotImage.setVisibility(View.VISIBLE);
        }else{
            hotImage.setVisibility(View.GONE);
        }
        groupPeople.setText(":"+entity.peopleCount);
        lastMsgText.setText("欢迎加入宝宝群圈"+entity.lastMsg);
    }
}
