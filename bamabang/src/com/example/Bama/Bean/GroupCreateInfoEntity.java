package com.example.Bama.Bean;

import com.meilishuo.gson.annotations.SerializedName;

public class GroupCreateInfoEntity extends BaseEntity {
    @SerializedName("content")
    public ContentEntity content;

    public static class ContentEntity {
        @SerializedName("id")
        public int id;
        @SerializedName("groupid")
        public String groupid;
        @SerializedName("name")
        public String name;
        @SerializedName("tagid")
        public String tagid;
        @SerializedName("picurl")
        public String picurl;
        @SerializedName("description")
        public String description;
        @SerializedName("bulletin")
        public String bulletin;
        @SerializedName("owner")
        public String owner;
        @SerializedName("created")
        public int created;
    }
}