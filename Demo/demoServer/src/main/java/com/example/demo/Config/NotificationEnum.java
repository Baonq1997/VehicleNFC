package com.example.demo.config;

public enum NotificationEnum {
    CHECK_IN("Đã nhận yêu cầu đậu xe","",null),CHECK_OUT("Kết thúc đậu xe","",null);

    String title;
    String body;
    String json;

    NotificationEnum(String title, String body, String json) {
        this.title = title;
        this.body = body;
        this.json = json;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
