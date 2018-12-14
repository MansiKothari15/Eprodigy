package com.bacancy.eprodigy.Models;

public class ChatMediaModel {

    String imgName="";
    String imgPath="";

    public ChatMediaModel(String imgName, String imgPath) {
        this.imgName = imgName;
        this.imgPath = imgPath;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
