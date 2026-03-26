package com.zdx.ai.sdk.domain.model;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private String touser = "opo1P3DS0ApRx229mh5HW1g9y3PA";
    private String template_id = "scjRjtU7q43JuNU2ysWSR8i1EpRZ8EmGNFcWtaVo2Gw";
    private String url = "https://github.com/zdx457/log/blob/master/2026-03-25/neN0K1rapAXb.md";
    private Map<String, Map<String, String>> data = new HashMap<>();

    public void put(String key, String value) {
        data.put(key, new HashMap<String, String>() {
            {
                put("value", value);
            }
        });
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }
}
