package com.example.Bama.ui.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.Bama.Bean.GroupCircleEntity;
import com.example.Bama.Bean.GroupMemberEntity;
import com.example.Bama.Bean.GroupMemberRankEntity;
import com.example.Bama.R;
import com.example.Bama.background.HCApplicaton;
import com.example.Bama.util.ImageLoaderUtil;

public class ViewGroupMemberRankItem extends LinearLayout {
    private Context context;
    private TextView tvTitle,tvName,tvRankValue;
    private ImageView ivAvatar;

    private GroupMemberRankEntity entity;

    public ViewGroupMemberRankItem(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ViewGroupMemberRankItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.item_group_member_rank, this, true);

        tvTitle = (TextView) findViewById(R.id.rankTitle);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        tvName = (TextView) findViewById(R.id.membername);
        tvRankValue = (TextView) findViewById(R.id.rankValue);
    }


    public void setModelItem(GroupMemberRankEntity entity) {
        this.entity = entity;
        setUI();
    }

    private void setUI() {
        if (entity == null) {
            return;
        }

        tvTitle.setText(entity.rankValue);
        HCApplicaton.getInstance().getImageLoader().displayImage(entity.avatar,ivAvatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
        tvName.setText(entity.accountName);
        tvRankValue.setText(entity.activityValue+"活跃值");
    }
}
