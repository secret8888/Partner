package com.partner.model;

import java.io.Serializable;

/**
 * Created by yuym on 2015/7/4.
 */
public class MessageInfo implements Serializable{

    private int messageId;

    private int messageActivity;

    private int messageStatus;

    private int messageToUser;

    private String messageToUserHeadImage;

    private String messageToUserName;

    private int messageFromUser;

    private String messageFromUserHeadImage;

    private String messageFromUserName;

    private int messageType;

    private long createTime;

    private long lastModTime;

    private MessageContent messageContent;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageActivity() {
        return messageActivity;
    }

    public void setMessageActivity(int messageActivity) {
        this.messageActivity = messageActivity;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public int getMessageToUser() {
        return messageToUser;
    }

    public void setMessageToUser(int messageToUser) {
        this.messageToUser = messageToUser;
    }

    public String getMessageToUserHeadImage() {
        return messageToUserHeadImage;
    }

    public void setMessageToUserHeadImage(String messageToUserHeadImage) {
        this.messageToUserHeadImage = messageToUserHeadImage;
    }

    public String getMessageToUserName() {
        return messageToUserName;
    }

    public void setMessageToUserName(String messageToUserName) {
        this.messageToUserName = messageToUserName;
    }

    public int getMessageFromUser() {
        return messageFromUser;
    }

    public void setMessageFromUser(int messageFromUser) {
        this.messageFromUser = messageFromUser;
    }

    public String getMessageFromUserHeadImage() {
        return messageFromUserHeadImage;
    }

    public void setMessageFromUserHeadImage(String messageFromUserHeadImage) {
        this.messageFromUserHeadImage = messageFromUserHeadImage;
    }

    public String getMessageFromUserName() {
        return messageFromUserName;
    }

    public void setMessageFromUserName(String messageFromUserName) {
        this.messageFromUserName = messageFromUserName;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastModTime() {
        return lastModTime;
    }

    public void setLastModTime(long lastModTime) {
        this.lastModTime = lastModTime;
    }

    public MessageContent getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(MessageContent messageContent) {
        this.messageContent = messageContent;
    }
}
