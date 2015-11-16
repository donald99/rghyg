package com.example.Bama.Bean;


import com.meilishuo.gson.annotations.SerializedName;

import java.util.List;

public class GroupCircleEntity extends BaseEntity{
    @SerializedName("content")
    public List<ContentEntity> content;

    public static class ContentEntity {
        /**
         * id : 1
         * groupid : 127035778260796004
         * name : 测试群1
         * tagid : 1
         * description : 测试群
         * bulletin : 这是衣蛾测试的群
         * ownerid : 1
         * owner : test1
         * status : 0
         * created : 11231233
         */

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
        public boolean status;
        @SerializedName("created")
        public int created;

		/**xiaoyu增加**/
		public int peopleCount;
    }
}
