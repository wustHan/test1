package com.example.testapplication.model.bean;
//待办模型
public class TodoModel {

    private int modelId;//模板ID
    private String modelName;//模板名称
    private int imageId;//模板图片

    public TodoModel(int modelId,String modelName){
        this.modelName = modelName;
        this.modelId = modelId;
    }
    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
