package com.ibrahimbinmansoor.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;


import android.annotation.SuppressLint;
import android.app.DownloadManager;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaySongs extends AppCompatActivity {
    ImageView preSong;
    ImageView nextSong;
    ImageView pauseSong;
    public int currSong = 0;
    int totalSongs;
    JSONArray jsonArray;
    Button download;
    Bundle bundle;
    boolean isClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Playing");
        setContentView(R.layout.activity_play_songs);
        preSong = findViewById(R.id.preSong);
        nextSong = findViewById(R.id.nextSong);
        pauseSong = findViewById(R.id.pauseSong);
        download = findViewById(R.id.download);

        Intent intent = getIntent();
        String intentData = intent.getStringExtra("apiResponse");
        currSong = intent.getIntExtra("songId", 0);

        // Initializing the fragment
        bundle = new Bundle();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentOnline fragmentOnline = new FragmentOnline();

        try {
            // The data sent across activity cannot be in the form of a JsonArray.
            // Hence the JsonArray was sent as a string and is converted to its original form here.
            jsonArray = new JSONArray(intentData);
            totalSongs = jsonArray.length();
            // currSong holds the id of the song whose information was sent to this activity
            JSONObject jsonObject = jsonArray.getJSONObject(currSong);
            String musicUrl = jsonObject.getString("file");
            String songName = jsonObject.getString("title");
            String songArtist = jsonObject.getString("artist");

            bundle.putString("url", musicUrl);
            bundle.putString("songName", songName);
            bundle.putString("songArtist", songArtist);
            fragmentOnline.setArguments(bundle);

            fragmentTransaction.add(R.id.fragment1, fragmentOnline, "MY tag");
            fragmentTransaction.commit();
        } catch (JSONException e) {
            Toast.makeText(this, "Enable to retrieve data", Toast.LENGTH_SHORT).show();
        }


        preSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This methods decrements the value of currSong (the id of the current song)
                // and passes the data of the new song to the fragment.
                currSong--;
                if (currSong < 0) {
                    currSong = totalSongs - 1;
                }
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(currSong);
                    String musicUrl = jsonObject.getString("file");
                    String songName = jsonObject.getString("title");
                    String songArtist = jsonObject.getString("artist");

                    bundle.putString("url", musicUrl);
                    bundle.putString("songName", songName);
                    bundle.putString("songArtist", songArtist);

                    sendFragmentData(bundle);

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Enable to retrieve data", Toast.LENGTH_SHORT).show();
                }

            }
        });

        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This methods increments the value of currSong (the id of the current song)
                // and passes the data of the new song to the fragment.
                currSong++;
                if (currSong >= totalSongs) {
                    currSong = 0;
                }
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(currSong);

                    String musicUrl = jsonObject.getString("file");
                    String songName = jsonObject.getString("title");
                    String songArtist = jsonObject.getString("artist");

                    bundle.putString("url", musicUrl);
                    bundle.putString("songName", songName);
                    bundle.putString("songArtist", songArtist);

                    sendFragmentData(bundle);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Unable to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pauseSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This method stops or plays the songs based on a boolean value that is changed on the button click.
                isClicked = !isClicked;

                if (isClicked) {
                    pauseSong.setImageResource(R.drawable.play);
                } else {
                    pauseSong.setImageResource(R.drawable.pause);
                }

                // Targets the already running fragment to pause and play the song.
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment1);

                if (fragment instanceof FragmentOnline) {
                    FragmentOnline fragmentOnline = (FragmentOnline) fragment;
                    fragmentOnline.pauseMusic();
                }

            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(currSong);
                    String musicUrl = jsonObject.getString("file");
                    String fileName = jsonObject.getString("title") + ".mp3";
                    downloadFile(musicUrl, fileName);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Enable to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // This methods downloads the current song to the music directory.
    private void downloadFile(String url, String fileName) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(fileName);
        request.setDescription("Downloading");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, fileName);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        if (downloadManager != null) {
            long downloadId = downloadManager.enqueue(request);
            registerDownloadCompleteReciever(downloadId, fileName);
        }
    }

    // these two methods are to display a notification when the song has been successfully downloaded
    private void registerDownloadCompleteReciever(long downlodId, String fileName) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == downlodId) {
                    showDownloadCompleteNotification(fileName);
                    unregisterReceiver(this);
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @SuppressLint("MissingPermission")
    private void showDownloadCompleteNotification(String fileName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            NotificationChannel channel = new NotificationChannel("channel1", "Download Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Download complete")
                .setContentText(fileName + " has been downloaded successfully")
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, builder.build());
    }

    // A method to send data to the fragment each time a button is pressed.
    private void sendFragmentData(Bundle bundle) {
        FragmentOnline fragment = new FragmentOnline();
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment1, fragment);
        fragmentTransaction.commit();
    }
}