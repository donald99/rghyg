package com.example.Bama.Bean;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

public class GroupMemberRankEntity {
    public static String accountId;
    public static String accountName;
    public static String avatar;
    public static String rankValue;
    public static String activityValue;

    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("content")
    public ContentEntity content;

    public static class ContentEntity {
        @SerializedName("all")
        public List<RankEntity> all;
        @SerializedName("week")
        public List<RankEntity> week;

        public static class RankEntity {
            @SerializedName("id")
            public int id;
            @SerializedName("name")
            public String name;
            @SerializedName("avatar")
            public String avatar;
            @SerializedName("username")
            public String username;
            @SerializedName("password")
            public String password;
            @SerializedName("coin")
            public int coin;
            @SerializedName("color")
            public String color;
            @SerializedName("ranking")
            public int ranking;
        }
    }
}
