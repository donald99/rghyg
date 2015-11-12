package com.example.Bama.ui;

import android.content.Context;
import android.text.TextUtils;
import com.example.Bama.Bean.ChannelItem;
import com.example.Bama.background.HCApplication;
import com.example.Bama.background.config.ServerConfig;
import com.example.Bama.ui.fragment.GroupFragment;
import com.example.Bama.util.Request;
import com.example.Bama.util.ToastUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class RequestUtil {
    private RequestUtil mRequestUtil;
    public RequestUtil getIntance(){
        if (mRequestUtil==null){
            mRequestUtil = new RequestUtil();
            return mRequestUtil;
        }
        return mRequestUtil;
    }

    public static void jubaoGroup(Context context,String fortype,String froid, String jubaoInfo){
        if (TextUtils.isEmpty(HCApplication.getInstance().getAccount().userId)){
            ToastUtil.makeLongText("请先登录");
            return;
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("uid",HCApplication.getInstance().getAccount().userId));
        params.add(new BasicNameValuePair("fortype",fortype));
        params.add(new BasicNameValuePair("froid",froid));
        params.add(new BasicNameValuePair("msg",jubaoInfo));

        Request.doRequest(context, params, ServerConfig.URL_GET_CONTACT_REPORT, Request.GET, new Request.RequestListener() {

            @Override
            public void onException(Request.RequestException e) {
            }

            @Override
            public void onComplete(String response) {

                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject object = new JSONObject(response);

                        if (response.contains("OK")){
                            ToastUtil.makeLongText("举报成功");
                        }else{
                            ToastUtil.makeLongText("举报失败");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    ToastUtil.makeLongText("举报失败");
                }
            }
        });
    }

    public static void queryTagList(Context context,final GroupFragment.QueryTagListCallback callback){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Request.doRequest(context, params, ServerConfig.URL_GET_TAG_LIST, Request.GET, new Request.RequestListener() {

            @Override
            public void onException(Request.RequestException e) {
            }

            @Override
            public void onComplete(String response) {

                if (!TextUtils.isEmpty(response)) {
                    ChannelItem item = HCApplication.getInstance().getGson().fromJsonWithNoException(response,ChannelItem.class);
                    if (item!=null && item.content!=null && callback != null){
                        callback.queryTagList(item.content);
                    }
                }else{
//                    ToastUtil.makeLongText("获取群组失败");
                }
            }
        });
    }
}
