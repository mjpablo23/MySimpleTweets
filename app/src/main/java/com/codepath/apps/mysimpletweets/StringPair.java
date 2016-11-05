package com.codepath.apps.mysimpletweets;

/**
 * Created by paulyang on 11/5/16.
 */
public class StringPair {
    private String key;
    private String value;

    public StringPair(String k, String v) {
        setKey(k);
        setValue(v);
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
