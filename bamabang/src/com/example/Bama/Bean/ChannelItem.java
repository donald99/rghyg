package com.example.Bama.Bean;

import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;


public class ChannelItem extends BaseEntity {
    @SerializedName("content")
    public List<ContentEntity> content;

    public static class ContentEntity {
        @SerializedName("tagid")
        public int tagid;
        @SerializedName("name")
        public String name;
    }
}