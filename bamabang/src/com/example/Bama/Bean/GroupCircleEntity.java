package com.example.Bama.Bean;


import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

public class GroupCircleEntity{
    @SerializedName("code")
    public String code;
    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("content")
    public List<ContentEntity> content;


    public class ContentEntity {
        @SerializedName("id")
        public String id;
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
        @SerializedName("ownerid")
        public String ownerid;
        @SerializedName("owner")
        public String owner;
        @SerializedName("status")
        public String status;
        @SerializedName("created")
        public String created;
    }

}
