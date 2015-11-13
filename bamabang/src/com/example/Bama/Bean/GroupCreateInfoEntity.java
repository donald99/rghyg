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
        public int tagid;
        @SerializedName("description")
        public String description;
        @SerializedName("bulletin")
        public String bulletin;
        @SerializedName("ownerid")
        public String ownerid;
        @SerializedName("owner")
        public String owner;
        @SerializedName("status")
        public int status;
        @SerializedName("created")
        public int created;
    }
}
//{
//        "status": true,
//        "code": 0,
//        "message": "",
//        "content": {
//        "id": 1,
//        "groupid": "127035778260796004",
//        "name": "测试群1",
//        "tagid": 1,
//        "description": "测试群",
//        "bulletin": "这是衣蛾测试的群",
//        "ownerid": "1",
//        "owner": "test1",
//        "status": 0,
//        "created": 11231233
//        }
//        }