package com.ibrahimbinmansoor.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SongDisplay extends AppCompatActivity {
    int totalSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("API Library");
        setContentView(R.layout.activity_song_display);

        // RecyclerView initialization
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // store the songs from the api
        List<DataItem> dataItems = new ArrayList<>();

        // Using the Volley library to get the JsonArray from the API.
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://randomuser.me/api/";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Toast.makeText(SongDisplay.this, "success", Toast.LENGTH_SHORT).show();

                    // Iterating through all the songs and adding them to a List to be given to the recycler view
                    totalSongs = response.length();
                    for (int i = 0; i < totalSongs; i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String songTitle = jsonObject.getString("title");
                        String songArtist = jsonObject.getString("artist");
                        String songUrl = jsonObject.getString("file");
                        dataItems.add(new DataItem(i, songTitle, songArtist, songUrl));
                    }

                    DataItemAdapter adapter = new DataItemAdapter(dataItems);
                    recyclerView.setAdapter(adapter);

                    // Launching another activity when a song is clicked
                    adapter.setOnItemClickListener(new DataItemAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int itemId) {
                            Intent intent = new Intent(SongDisplay.this, PlaySongs.class);
                            intent.putExtra("apiResponse", response.toString());
                            intent.putExtra("songId", itemId);
                            startActivity(intent);
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(SongDisplay.this, "Unable to retrieve data from api", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SongDisplay.this, "Unable to access api", Toast.LENGTH_SHORT).show();
                Toast.makeText(SongDisplay.this, "Make sure the api is online and open the app again", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonArrayRequest);


    }

    // Options menu
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