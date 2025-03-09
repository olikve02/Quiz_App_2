package com.example.quizapp2;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_images")
public class Image {
    @PrimaryKey(autoGenerate = true)
    int id = 0;
    @ColumnInfo
    private String uriString;
    @ColumnInfo
    private String name;


    public Image(String uri, String name) {
        this.uriString = uri;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Image(){

    }
    public String getUriString() {
        return uriString;
    }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
