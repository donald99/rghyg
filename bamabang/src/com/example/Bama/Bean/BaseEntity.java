package com.example.Bama.Bean;

import com.meilishuo.gson.annotations.SerializedName;

public class BaseEntity{
    @SerializedName("code")
    public int code;
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
}
