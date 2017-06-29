package com.getit.getit.pojo;

/**
 * Created by kashivivek on 06-14-2017.
 */

public class DataModel {
    // Getter and Setter model for recycler view items
    private String title;
    private int image;
    private String tag;

    public DataModel(String title,  int image, String tag) {

        this.title = title;
        this.tag = tag;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getTag() {
        return tag;
    }

    public int getImage() {
        return image;
    }
}
