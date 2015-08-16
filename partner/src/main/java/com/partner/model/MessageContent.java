package com.partner.model;

import java.io.Serializable;

/**
 * Created by yuym on 2015/7/4.
 */
public class MessageContent implements Serializable{

    private String title;

    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
