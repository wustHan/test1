package com.example.testapplication.model.bean;

public class PickContactInfo {
    private UserInfo user;
    private boolean isChecked;

    public PickContactInfo() {
    }

    public PickContactInfo(UserInfo user, boolean isChecked) {
        this.user = user;
        this.isChecked = isChecked;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "PickContactInfo{" +
                "user=" + user +
                ", isChecked=" + isChecked +
                '}';
    }
}
