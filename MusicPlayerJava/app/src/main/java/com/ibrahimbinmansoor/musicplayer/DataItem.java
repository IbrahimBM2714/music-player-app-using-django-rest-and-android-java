package com.ibrahimbinmansoor.musicplayer;

public class DataItem {
    private int id;
    private String songTitle;
    private String songArtist;
    private String songUrl;

    public DataItem(int id, String songTitle, String songArtist, String songUrl){
        this.id = id;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.songUrl = songUrl;
    }

    public int getId() {
        return id;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getSongUrl() {
        return songUrl;
    }
}
