package com.example.Bama.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Bama.Bean.GroupMemberRankEntity;
import com.example.Bama.R;
import com.example.Bama.adapter.GroupMemberRankAdapter;

import java.util.ArrayList;

public class RankFragment extends Fragment implements View.OnClickListener{
    private Activity activity;
    private FrameLayout weekRankFl,monthRankFL;
    private TextView weekRankText,monthRankText;
    private ListView mListView;
    private GroupMemberRankAdapter mAdapter;
    private ArrayList<GroupMemberRankEntity> groupMemberRankList = new ArrayList<GroupMemberRankEntity>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rank, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;


    }

    private void initView(View view){
        weekRankFl = (FrameLayout) view.findViewById(R.id.weekRankFl);
        monthRankFL = (FrameLayout) view.findViewById(R.id.monthRankFL);
        weekRankText = (TextView) view.findViewById(R.id.weekRankText);
        monthRankText = (TextView) view.findViewById(R.id.monthRankText);

        weekRankFl.setOnClickListener(this);
        weekRankText.setOnClickListener(this);
        monthRankFL.setOnClickListener(this);
        monthRankText.setOnClickListener(this);


        mListView = (ListView) view.findViewById(R.id.listview);

        for (int i = 0; i < 10; i++) {
            GroupMemberRankEntity entity = new GroupMemberRankEntity();
            entity.accountId = i+"";
            entity.accountName = "rankrank";
            entity.avatar = "http://img.name2012.com/uploads/allimg/2015-06/30-023131_451.jpg";
            entity.rankValue = i+"";
            entity.activityValue = i+"";
            groupMemberRankList.add(entity);
        }


        mAdapter = new GroupMemberRankAdapter(activity, groupMemberRankList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.weekRankText:
            case  R.id. weekRankFl:
                Toast.makeText(activity,"周排行榜",Toast.LENGTH_LONG).show();
                weekRankText.setTextColor(getResources().getColor(R.color.btn_logout_normal));
                monthRankText.setTextColor(getResources().getColor(R.color.gray_normal));

                break;
            case R.id.monthRankText:
            case R.id.monthRankFL:
                Toast.makeText(activity,"月排行榜",Toast.LENGTH_LONG).show();
                weekRankText.setTextColor(getResources().getColor(R.color.gray_normal));
                monthRankText.setTextColor(getResources().getColor(R.color.btn_logout_normal));
                break;
        }
    }
}