package com.example.Bama.background.config;

/**
 * Created by sreay on 14-8-18.
 */
public class ServerConfig {
    // 测试环境
    public static String BASE_URL_TEST = "http://182.92.227.113/";
    // 正式环境
    public static String BASE_URL_OFFICAL = "http://182.92.227.113/";

    public static String BASE_URL = BASE_URL_OFFICAL;

    public static void initUrl(boolean boo) {
        if (boo) {
            ServerConfig.BASE_URL = ServerConfig.BASE_URL_TEST;
        } else {
            ServerConfig.BASE_URL = ServerConfig.BASE_URL_OFFICAL;
        }
    }


    public static final String URL_GET_VER_CODE = "xiaoxinli/api/accounts/register.do";
    public static final String URL_REGISTER = "xiaoxinli/api/accounts/activate.do";
    public static final String URL_LOGIN = "xiaoxinli/api/accounts/login.do";
    public static final String URL_GET_POSTS = "xiaoxinli/api/anonymous/queryAnonymous.do";
    public static final String URL_POST_TIEZI = "xiaoxinli/api/anonymous/pubTopic.do";
    public static final String URL_POST_DESC = "xiaoxinli/api/anonymous/anonymousDetail.do";
    public static final String URL_POST_REPLY = "xiaoxinli/api/anonymous/replyAnonymous.do";
    public static final String URL_MY_POST = "xiaoxinli/api/anonymous/myTopic.do";
    public static final String URL_MODIFY_PWD = "xiaoxinli/api/accounts/change_password.do";
    public static final String URL_MODIFY_AVATAR = "xiaoxinli/api/accounts/setImage2.do";
    public static final String URL_MODIFY_NICKNAME = "xiaoxinli/api/accounts/setNickName2.do";
    public static final String URL_GET_ACTIVITIES = "xiaoxinli/api/activity/queryActivitys.do";
    public static final String URL_QUERY_MY_ACTIVITIES = "xiaoxinli/api/activity/queryMyActivitys.do";
    public static final String URL_GET_GONGGAO = "xiaoxinli/api/activity/queryActivitys.do";
    public static final String URL_GET_XINLIBAIKE = "xiaoxinli/api/activity/queryActivitys.do";
    public static final String URL_GET_DOCTORS = "xiaoxinli/api/export/doctorlist.do";
    public static final String URL_BAOMING_HUODONG = "xiaoxinli/api/anonymous/praise.do";
    public static final String URL_QUERY_ACTIVITIES = "xiaoxinli/api/activity/queryNewActivitys.do";
    public static final String URL_GET_ORDERS = "xiaoxinli/api/export/appointment_history.do";
    public static final String URL_DELETE_ORDER = "xiaoxinli/api/export/delOrder.do";
    public static final String URL_YUYUE_DOCTOR = "xiaoxinli/api/export/appointment.do";
    public static final String URL_COMMENT_ORDER = "xiaoxinli/api/export/appraise.do";
    public static final String URL_ORDER_UPDATE_STATUS = "xiaoxinli/api/export/updateOrder.do";
    public static final String URL_GUANLIAN_SCHOOL = "xinlixue/appStuUser/stuUserInfoValidation.do";
    public static final String URL_DANGAN_LIST = "xinlixue/appStuUser/stuEvaluationHistory.do";
}
