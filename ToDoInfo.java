package com.example.testapplication.model.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Timer;

public class ToDoInfo implements Serializable {
    private int toDo_id;//ID
    private String toDo_title;//标题
    private String toDo_place;//地点
    private String toDo_content;//内容
    private String startTime;//开始时间
    private String endTime;//结束时间
    private int color;//重要程度
    private String toDo_sender;//发送者
    private String toDo_receiverGroup;//参与者
    private String toDO_somethingElse;//其他
    private int toDo_ifTop;//是否置顶
    private int model_type;//模型类型
    private int ifDone;//是否完成
    private int type;//待办类型1为展示2为未处理3为最近删除

    public ToDoInfo() {
        toDo_place = "未定";
        toDo_content = "不明";
        toDo_receiverGroup = "不明";
        toDO_somethingElse = "未定";
        ifDone = 0;
        type = 2;
    }

    public ToDoInfo(String toDo_title, String toDo_place, String toDo_content, String startTime, String endTime, int color, String toDo_sender, String toDo_receiverGroup, String toDO_somethingElse, int toDo_ifTop, int model_type, int ifDone,int type) {
        this.toDo_title = toDo_title;
        this.toDo_place = toDo_place;
        this.toDo_content = toDo_content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
        this.toDo_sender = toDo_sender;
        this.toDo_receiverGroup = toDo_receiverGroup;
        this.toDO_somethingElse = toDO_somethingElse;
        this.toDo_ifTop = toDo_ifTop;
        this.model_type = model_type;
        this.ifDone = ifDone;
        this.type = type;
    }

    public int getIfDone() {
        return ifDone;
    }

    public void setIfDone(int ifDone) {
        this.ifDone = ifDone;
    }

    public int getModel_type() {
        return model_type;
    }

    public void setModel_type(int model_type) {
        this.model_type = model_type;
    }

    public int getToDo_id() {
        return toDo_id;
    }

    public void setToDo_id(int toDo_id) {
        this.toDo_id = toDo_id;
    }

    public String getToDo_title() {
        return toDo_title;
    }

    public void setToDo_title(String toDo_title) {
        this.toDo_title = toDo_title;
    }

    public String getToDo_place() {
        return toDo_place;
    }

    public void setToDo_place(String toDo_place) {
        this.toDo_place = toDo_place;
    }

    public String getToDo_content() {
        return toDo_content;
    }

    public void setToDo_content(String toDo_content) {
        this.toDo_content = toDo_content;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getToDo_sender() {
        return toDo_sender;
    }

    public void setToDo_sender(String toDo_sender) {
        this.toDo_sender = toDo_sender;
    }

    public String getToDo_receiverGroup() {
        return toDo_receiverGroup;
    }

    public void setToDo_receiverGroup(String toDo_receiverGroup) {
        this.toDo_receiverGroup = toDo_receiverGroup;
    }

    public String getToDO_somethingElse() {
        return toDO_somethingElse;
    }

    public void setToDO_somethingElse(String toDO_somethingElse) {
        this.toDO_somethingElse = toDO_somethingElse;
    }

    public int getToDo_ifTop() {
        return toDo_ifTop;
    }

    public void setToDo_ifTop(int toDo_ifTop) {
        this.toDo_ifTop = toDo_ifTop;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return  toDo_title +
                "&&&" + toDo_place +
                "&&&" + toDo_content +
                "&&&" + startTime +
                "&&&" + endTime +
                "&&&" + color +
                "&&&" + toDo_sender +
                "&&&" + toDo_receiverGroup +
                "&&&" + toDO_somethingElse +
                "&&&" + toDo_ifTop +
                "&&&" + model_type +
                "&&&" + ifDone ;
    }
}
