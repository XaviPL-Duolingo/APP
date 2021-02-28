package com.duolingo.app.model;

import android.graphics.Bitmap;

public class Language {

    String lamguageName;
    Bitmap flag;

    public Language(String lamguageName) {
        this.lamguageName = lamguageName;
        // this.flag = flag;
    }

    public String getLamguageName() {
        return lamguageName;
    }

    public void setLamguageName(String lamguageName) {
        this.lamguageName = lamguageName;
    }

    public Bitmap getFlag() {
        return flag;
    }

    public void setFlag(Bitmap flag) {
        this.flag = flag;
    }
}
