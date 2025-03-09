package com.example.quizapp2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Gallery extends AppCompatActivity implements ImageAdapter.onDeleteIconClickListener {
    private ImageAdapter imageAdapter;
    private LiveData<List<Image>> allImagesLiveData;
    private Uri newImageUri;
    private String newImageName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        reGrantPermissions();
        setUpRecycler();

        //Add new photo with phone gallery
        Button bntAddImage = findViewById(R.id.btnAddImage);
        bntAddImage.setOnClickListener(view -> openPhoneGallery());



    }

    public void openPhoneGallery(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        galleryLauncher.launch(intent);
    }
    private final ActivityResultLauncher<Intent> galleryLauncher =
        registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null){
                        newImageUri = result.getData().getData();
                        getContentResolver().takePersistableUriPermission(newImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        showNameInput();
                    }
                }
        );

   private void showNameInput(){
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle("Gi ett navn til bildet");

       final EditText input = new EditText(this);
       builder.setView(input);


       builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               newImageName = input.getText().toString();
               insertImage();
           }
       });
       builder.show();
   }
   public void insertImage(){
       Image image = new Image(newImageUri.toString(), newImageName);
       AppDatabase db = AppDatabase.getDatabase(this);
       AppDatabase.databaseWriteExecutor.execute(() -> {
           db.imageDao().insert(image);
       });
   }
    public void setUpRecycler(){


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        imageAdapter = new ImageAdapter(this);
        recyclerView.setAdapter(imageAdapter);


        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        allImagesLiveData = db.imageDao().getAllImages();


        allImagesLiveData.observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {

                imageAdapter.submitList(images);
            }
        });
    }

    @Override
    public void onImageDeleteClick(Image image) {
        AppDatabase db = AppDatabase.getDatabase(this);

        //Make dialoque box for deletion of images
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Er du sikker på at du vil slette bildet?");
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    db.imageDao().removeImage(image);
                });
            }
        });

        builder.setNegativeButton("Nei", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();

    }
    private void reGrantPermissions() {
        AppDatabase db = AppDatabase.getDatabase(this);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Image> allImages = db.imageDao().getAllImagesAsList(); // Get all images (not LiveData)

            for (Image image : allImages) {
                try {
                    Uri uri = Uri.parse(image.getUriString());

                    // Check if we already have permission
                    boolean hasPermission = false;
                    for (UriPermission permission : getContentResolver().getPersistedUriPermissions()) {
                        if (permission.getUri().equals(uri)) {
                            hasPermission = true;
                            break;
                        }
                    }

                    if (!hasPermission) {
                        getContentResolver().takePersistableUriPermission(
                                uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                        Log.d("Gallery", "Re-granted permission for: " + uri);
                    } else {
                        Log.d("Gallery", "Permission already granted for: " + uri);
                    }

                } catch (SecurityException e) {
                    Log.e("Gallery", "Failed to re-grant permission for: " + image.getUriString(), e);
                    // Do NOT delete the image, just log the error.
                }
            }
        });
    }
}





