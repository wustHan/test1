package com.example.testapplication.model.bean;

public class PickToDoInfo {
    private ToDoInfo toDoInfo;
    private boolean isChecked;

    public PickToDoInfo() {
    }

    public PickToDoInfo(ToDoInfo toDoInfo, boolean isChecked) {
        this.toDoInfo = toDoInfo;
        this.isChecked = isChecked;
    }

    public ToDoInfo getToDoInfo() {
        return toDoInfo;
    }

    public void setToDoInfo(ToDoInfo toDoInfo) {
        this.toDoInfo = toDoInfo;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
