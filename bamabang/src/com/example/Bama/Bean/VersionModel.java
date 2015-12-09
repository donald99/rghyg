package com.example.Bama.Bean;

import com.meilishuo.gson.annotations.SerializedName;

public class VersionModel {
    @SerializedName("version_code")
    public String version_code;

    @SerializedName("feature")
    public String feature;

    @SerializedName("url")
    public String url;
}