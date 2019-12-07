package com.ssindher11.codevaradmin;

public class NotificationModel {

    private String date;
    private String message;
    private String time;

    public NotificationModel() {
    }

    public NotificationModel(String date, String message, String time) {
        this.date = date;
        this.message = message;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
