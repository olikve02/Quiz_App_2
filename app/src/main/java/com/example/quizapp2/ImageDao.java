package com.example.quizapp2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDao {
    @Insert
    void insert(Image image);
    @Query("SELECT * FROM quiz_images")
    LiveData<List<Image>> getAllImages();

    @Query("SELECT * FROM quiz_images")
    List<Image> getAllImagesAsList();
    @Delete
    void removeImage(Image image);
}
