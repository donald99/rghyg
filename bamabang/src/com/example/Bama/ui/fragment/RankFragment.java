package com.example.Bama.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.example.Bama.background.HCApplication;
import com.example.Bama.background.config.ServerConfig;
import com.example.Bama.util.Request;
import com.example.Bama.util.ToastUtil;
import com.meilishuo.gson.annotations.SerializedName;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class RankFragment extends Fragment implements View.OnClickListener{
    private Activity activity;
    private FrameLayout weekRankFl,monthRankFL;
    private TextView weekRankText,monthRankText;
    private ListView mListView;
    private GroupMemberRankAdapter mAdapter;

    public List<GroupMemberRankEntity.ContentEntity.RankEntity> all = new ArrayList<GroupMemberRankEntity.ContentEntity.RankEntity>();
    public List<GroupMemberRankEntity.ContentEntity.RankEntity> week = new ArrayList<GroupMemberRankEntity.ContentEntity.RankEntity>();
    public List<GroupMemberRankEntity.ContentEntity.RankEntity> current = new ArrayList<GroupMemberRankEntity.ContentEntity.RankEntity>();

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


        queryRankList();
    }

    private void queryRankList() {

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        Request.doRequest(activity,pairs, ServerConfig.URL_GET_RANK_LISR,Request.GET,new Request.RequestListener(){

            @Override
            public void onException(Request.RequestException e) {
            }

            @Override
            public void onComplete(String response) {

                if(!TextUtils.isEmpty(response)){
                    GroupMemberRankEntity entity = HCApplication.getInstance().getGson().fromJsonWithNoException(response,GroupMemberRankEntity.class);

                    if (entity!=null && entity.content != null){
                        all = entity.content.all;
                        week = entity.content.week;
                        current = week;
                        mAdapter = new GroupMemberRankAdapter(activity, current);
                        mListView.setAdapter(mAdapter);
                    }
                }
            }
        });
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

        mAdapter = new GroupMemberRankAdapter(activity, current);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.weekRankText:
            case  R.id. weekRankFl:
                weekRankText.setTextColor(getResources().getColor(R.color.rank_press_font));
                monthRankText.setTextColor(getResources().getColor(R.color.rank_unpress_font));

                current = week;
                mAdapter.notifyDataSetChanged();

                break;
            case R.id.monthRankText:
            case R.id.monthRankFL:
                weekRankText.setTextColor(getResources().getColor(R.color.rank_unpress_font));
                monthRankText.setTextColor(getResources().getColor(R.color.rank_press_font));

                current = all;
                mAdapter.notifyDataSetChanged();

                break;
        }
    }
}