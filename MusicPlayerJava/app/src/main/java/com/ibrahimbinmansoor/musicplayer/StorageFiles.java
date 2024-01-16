package com.ibrahimbinmansoor.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class StorageFiles extends AppCompatActivity implements MusicAdapter.OnItemClickListener {
    Button showFiles;
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;
    List<MusicItem> musicItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Storage Library");
        setContentView(R.layout.activity_storage_files);

        showFiles = findViewById(R.id.showFiles);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    musicItems = getFileNames();
                    displayFileNames(musicItems);
                } else {
                    requestPermission();
                }
            }
        });
    }

    // These three methods are used to ask the user to allow or deny permission to access the storage.
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                List<MusicItem> fileNames = getFileNames();
                displayFileNames(fileNames);
            }
        }
    }

    // A method used to get the mp3 files from storage.
    private List<MusicItem> getFileNames() {
        List<MusicItem> fileNames = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // Projection is used define the data that you want to retrieve
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
        };

        // Selection is used to specify the criteria upon which the files are retrieved
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
                + " AND " + MediaStore.Audio.Media.DATA + " LIKE '%/Music/%'";

        Cursor cursor = getContentResolver().query(uri, projection, selection, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                fileNames.add(new MusicItem(fileName, "", filePath, cursor.getPosition()));
            }
            cursor.close();
        }

        return fileNames;
    }

    private void displayFileNames(List<MusicItem> fileNames) {
        musicAdapter = new MusicAdapter(fileNames, this);
        recyclerView.setAdapter(musicAdapter);
    }

    // RecyclerView's onItemClick
    @Override
    public void onItemClick(int position) {
        if (musicItems != null) {
            Intent intent = new Intent(this, PlayMusic.class);
            intent.putExtra("musicItems", new Gson().toJson(musicItems));
            intent.putExtra("currSong", position);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.apiSongsList) {
            Intent intent = new Intent(this, SongDisplay.class);
            startActivity(intent);
        } else if (itemId == R.id.storageSongsList) {
            Intent intent = new Intent(this, StorageFiles.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}