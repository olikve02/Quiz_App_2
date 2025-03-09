package com.example.quizapp2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuestionDao {
    @Insert
    void insert(Question question);
    @Query("SELECT * FROM question")
    LiveData<List<Question>> getAllQuestions();
}
