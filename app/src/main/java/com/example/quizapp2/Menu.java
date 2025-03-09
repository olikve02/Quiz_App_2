package com.example.quizapp2;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Database;
import androidx.room.Room;

import java.util.List;

public class Menu extends AppCompatActivity {
    private LiveData<List<Image>> listLiveData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        setContentView(R.layout.menu);

        Button galleryBtn = findViewById(R.id.galleryBtn);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Gallery.class);
                startActivity(intent);
            }
        });

        Button quizBtn = findViewById(R.id.quizBtn);
        quizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(Menu.this, QuizActivity.class);
               startActivity(intent);
            }
        });


        listLiveData = db.imageDao().getAllImages();
        listLiveData.observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                if(images.isEmpty()){
                    loadStandardImages(db);
                }
            }
        });


    }
    public void loadStandardImages(AppDatabase db){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            db.imageDao().insert(new Image(MyUtils.getUriToDrawable(this, R.drawable.bil).toString(), "bil"));
            db.imageDao().insert(new Image(MyUtils.getUriToDrawable(this, R.drawable.boat).toString(), "baat"));
            db.imageDao().insert(new Image(MyUtils.getUriToDrawable(this, R.drawable.hest).toString(), "hest"));
        });
    }
}
