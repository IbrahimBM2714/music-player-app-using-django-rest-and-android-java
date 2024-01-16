package com.ibrahimbinmansoor.musicplayer;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataItemViewHolder extends RecyclerView.ViewHolder {

    private TextView songTitleTextView;
    private TextView songArtistTextView;

    public DataItemViewHolder(@NonNull View itemView) {
        super(itemView);
        songTitleTextView = itemView.findViewById(R.id.songTitleTextView);
        songArtistTextView = itemView.findViewById(R.id.songArtistTextView);
    }

    public void bindData(DataItem dataItem){
        songTitleTextView.setText(String.valueOf(dataItem.getSongTitle()));
        songArtistTextView.setText(String.valueOf(dataItem.getSongArtist()));
    }
}
