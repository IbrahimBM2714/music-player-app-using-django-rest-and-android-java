package com.ibrahimbinmansoor.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PlayMusic extends AppCompatActivity {

    ImageView preSong1, pauseSong1, nextSong1;
    int musicsLength;
    int currSong;
    List<MusicItem> allMusicItems;
    Bundle bundle;
    boolean isClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Playing");
        setContentView(R.layout.activity_play_music);

        nextSong1 = findViewById(R.id.nextSong1);
        preSong1 = findViewById(R.id.preSong1);
        pauseSong1 = findViewById(R.id.pauseSong1);

        allMusicItems = new ArrayList<>();

        bundle = new Bundle();

        Intent intent = getIntent();
        String musicItems = intent.getStringExtra("musicItems");
        currSong = intent.getIntExtra("currSong", 0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FragmentOnline fo = new FragmentOnline();

        if (musicItems != null) {
            /* Data sent across activities cannot be in the form of an array List, which is why
            * musicItems ArrayList was sent as a string and converted to its original form here
            *  using Gson*/
            allMusicItems = new Gson().fromJson(musicItems, new TypeToken<List<MusicItem>>() {
            }.getType());
        } else{
            Toast.makeText(this, "Unable to retrieve files from storage", Toast.LENGTH_SHORT).show();
        }
        musicsLength = allMusicItems.size();

        String musicUrl = allMusicItems.get(currSong).getFilePath();
        String songName = allMusicItems.get(currSong).getName();
        String artistName = "";

        bundle.putString("url", musicUrl);
        bundle.putString("songName", songName);
        bundle.putString("songArtist", artistName);
        fo.setArguments(bundle);

        fragmentTransaction.add(R.id.fragment2, fo, "MY tag");
        fragmentTransaction.commit();


        pauseSong1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isClicked = !isClicked;

                if (isClicked) {
                    pauseSong1.setImageResource(R.drawable.play);
                } else {
                    pauseSong1.setImageResource(R.drawable.pause);
                }

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment2);

                if (fragment instanceof FragmentOnline) {
                    FragmentOnline fragmentOnline = (FragmentOnline) fragment;
                    fragmentOnline.pauseMusic();
                }
            }
        });

        nextSong1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currSong++;
                if (currSong >= musicsLength) {
                    currSong = 0;
                }

                String musicUrl = allMusicItems.get(currSong).getFilePath();
                String songName = allMusicItems.get(currSong).getName();
                String artistName = "";

                bundle.putString("url", musicUrl);
                bundle.putString("songName", songName);
                bundle.putString("songArtist", artistName);
                sendFragmentData(bundle);

            }
        });

        preSong1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currSong--;
                if (currSong < 0) {
                    currSong = musicsLength - 1;
                }

                String musicUrl = allMusicItems.get(currSong).getFilePath();
                String songName = allMusicItems.get(currSong).getName();
                String artistName = "";

                bundle.putString("url", musicUrl);
                bundle.putString("songName", songName);
                bundle.putString("songArtist", artistName);
                sendFragmentData(bundle);

            }
        });

    }

    private void sendFragmentData(Bundle bundle) {
        FragmentOnline fragment = new FragmentOnline();
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment2, fragment);
        fragmentTransaction.commit();
    }

}