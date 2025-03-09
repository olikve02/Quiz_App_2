package com.example.quizapp2;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "question")
public class Question {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String imageUri;
    @ColumnInfo
    private String correctAnswer;
    @ColumnInfo
    private String wrongAnswer1;
    @ColumnInfo
    private String wrongAnswer2;

    public void setId(int id) {
        this.id = id;
    }

    public Question(String imageUri, String correctAnswer, String wrongAnswer1, String wrongAnswer2) {
        this.imageUri = imageUri.toString();
        this.correctAnswer = correctAnswer;
        this.wrongAnswer1 = wrongAnswer1;
        this.wrongAnswer2 = wrongAnswer2;
    }

    public Question(){

    }

    public int getId() {
        return id;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getWrongAnswer1() {
        return wrongAnswer1;
    }

    public void setWrongAnswer1(String wrongAnswer1) {
        this.wrongAnswer1 = wrongAnswer1;
    }

    public String getWrongAnswer2() {
        return wrongAnswer2;
    }

    public void setWrongAnswer2(String wrongAnswer2) {
        this.wrongAnswer2 = wrongAnswer2;
    }
}
