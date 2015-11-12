package com.example.Bama.Bean;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

public class GroupMemberRankEntity extends BaseEntity{
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
