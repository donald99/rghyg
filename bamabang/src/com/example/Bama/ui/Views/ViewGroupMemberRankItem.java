package com.example.Bama.ui.Views;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.Bama.Bean.GroupMemberRankEntity;
import com.example.Bama.R;
import com.example.Bama.background.HCApplication;
import com.example.Bama.util.ImageLoaderUtil;

public class ViewGroupMemberRankItem extends LinearLayout {
    private Context context;
    private TextView tvTitle,tvName,tvRankValue;
    private ImageView ivAvatar;

    private GroupMemberRankEntity.ContentEntity.RankEntity entity;

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


    public void setModelItem(GroupMemberRankEntity.ContentEntity.RankEntity entity) {
        this.entity = entity;
        setUI();
    }

    private void setUI() {
        if (entity == null) {
            return;
        }

        tvTitle.setText(entity.ranking+"");
        if(entity.color.contains("#")){
            tvTitle.setTextColor(Color.parseColor(entity.color));
        }else{
            tvTitle.setTextColor(Color.parseColor("#"+entity.color));
        }
        HCApplication.getInstance().getImageLoader().displayImage(entity.avatar,ivAvatar, ImageLoaderUtil.Options_Memory_Rect_Avatar);
        tvName.setText(entity.name);
        tvRankValue.setText(entity.coin+"活跃值");
    }
}
