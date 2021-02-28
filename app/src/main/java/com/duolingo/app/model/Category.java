package com.duolingo.app.model;

import android.graphics.Bitmap;

public class Category {

    String title;
    String level;
    String progress;
    Bitmap image;

    public Category(String title, String level, String progress) {
        this.title = title;
        this.level = level;
        this.progress = progress;
        // this.image = image;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
