package com.example.Bama.background;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.example.Bama.R;
import com.example.Bama.background.config.CommonPreference;
import com.example.Bama.chat.chatuidemo.Constant;
import com.example.Bama.chat.chatuidemo.db.UserDao;
import com.example.Bama.chat.chatuidemo.domain.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account {
    public static final String kUsrInfo = "key_user_info";
    public static final String kUserId = "key_user_id";
    public static final String kPassword = "key_password";
    public static final String kAvatar = "key_avatar";
    public static final String kCreated = "key_create";
    public static final String kUserName = "key_user_name";
    public static final String kNickName = "key_nick_name";
    public static final String kBady = "key_bady";

    public String userId;
    public String password;
    public String created;
    public String baby;
    public String avatar;
    public String userName;
    public String name;

    // 用户选择照片时需要记住用户的偏好的文件夹的key
    public static String kLastSelectDir = "lastSelectDir";
    public String lastSelectDir;

    public static Account loadAccount() {
        String userInfo = CommonPreference.getStringValue(kUsrInfo, "");
        Account account = new Account();
        if (TextUtils.isEmpty(userInfo)) {
            return account;
        }
        try {
            JSONObject user = new JSONObject(userInfo);
            account.userId = user.optString(kUserId);
            account.userName = user.optString(kUserName);
            account.name = user.optString(kNickName);
            account.password = user.optString(kPassword);
            account.avatar = user.optString(kAvatar);
            account.created = user.optString(kCreated);
            account.baby = user.optString(kBady);

            account.lastSelectDir = user.optString(kLastSelectDir);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return account;
    }

    public void saveMeInfoToPreference() {
        JSONObject info = new JSONObject();
        try {
            info.put(kUserId, userId);
            info.put(kUserName, userName);
            info.put(kNickName, name);
            info.put(kPassword, password);
            info.put(kAvatar, avatar);
            info.put(kCreated, created);
            info.put(kBady, baby);
            info.put(kLastSelectDir, lastSelectDir);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CommonPreference.setStringValue(kUsrInfo, info.toString());
    }

    public void clearMeInfo() {
        userId = "";
        userName = "";
        name = "";
        password = "";
        avatar = "";
        created = "";
        baby = "";
        lastSelectDir = "";
        saveMeInfoToPreference();
    }




    public static void toLoginChatServer(final Context contxt,final String userId,final String password) {
        EMChatManager.getInstance().login(userId, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                HCApplication.getInstance().setUserName(userId);
                HCApplication.getInstance().setPassword(password);
                try {
                    EMChatManager.getInstance().loadAllLocalGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    processContactsAndGroups(contxt);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                        HCApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
            }
        });
    }

    public static void processContactsAndGroups(Context mContxt) throws EaseMobException {
        List<String> usernames = EMContactManager.getInstance().getContactUserNames();
        EMLog.d("roster", "contacts size: " + usernames.size());
        Map<String, User> userlist = new HashMap<String, User>();
        for (String username : usernames) {
            User user = new User();
            user.setUsername(username);
            setUserHearder(username, user);
            userlist.put(username, user);
        }

        User addFriends = new User();
        String addFriendStr = mContxt.getString(R.string.add_friend);
        addFriends.setUsername(Constant.ADD_FRIEND);
        addFriends.setNick(addFriendStr);
        addFriends.setHeader("");
        userlist.put(Constant.ADD_FRIEND, addFriends);

        User newFriends = new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = mContxt.getString(R.string.Application_and_notify);
        newFriends.setNick(strChat);

        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);

        User groupUser = new User();
        String strGroup = mContxt.getString(R.string.group_chat);
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        User chatRoomItem = new User();
        String strChatRoom = mContxt.getString(R.string.chat_room);
        chatRoomItem.setUsername(Constant.CHAT_ROOM);
        chatRoomItem.setNick(strChatRoom);
        chatRoomItem.setHeader("");
        userlist.put(Constant.CHAT_ROOM, chatRoomItem);

        HCApplication.getInstance().setContactList(userlist);
        System.out.println("----------------" + userlist.values().toString());
        UserDao dao = new UserDao(mContxt);
        List<User> users = new ArrayList<User>(userlist.values());
        dao.saveContactList(users);

        List<String> blackList = EMContactManager.getInstance().getBlackListUsernamesFromServer();
        EMContactManager.getInstance().saveBlackList(blackList);

        EMChatManager.getInstance().fetchJoinedGroupsFromServer();
    }

    public static void setUserHearder(String username, User user) {
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                    .toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }
}
