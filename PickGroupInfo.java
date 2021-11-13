package com.example.testapplication.model.bean;

import com.hyphenate.chat.EMGroup;

public class PickGroupInfo {
    private EMGroup emGroup;
    private boolean isChecked;

    public PickGroupInfo() {
    }

    public PickGroupInfo(EMGroup emGroup, boolean isChecked) {
        this.emGroup = emGroup;
        this.isChecked = isChecked;
    }

    public EMGroup getEmGroup() {
        return emGroup;
    }

    public void setEmGroup(EMGroup emGroup) {
        this.emGroup = emGroup;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
