package com.tsmsogn.stackmob;

import com.stackmob.sdk.model.StackMobModel;

public class PostMeta extends StackMobModel {
    private String key;
    private String value;

    public PostMeta(String key, String value) {
        super(PostMeta.class);
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
