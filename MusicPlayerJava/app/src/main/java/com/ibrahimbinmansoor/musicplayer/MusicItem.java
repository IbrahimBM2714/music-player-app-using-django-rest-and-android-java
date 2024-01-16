package com.ibrahimbinmansoor.musicplayer;

public class MusicItem {
    private String name;
    private String artist;
    private String filePath;
    private int currSong;

    public MusicItem(String name, String artist, String filePath, int currSong) {
        this.name = name;
        this.artist = artist;
        this.filePath = filePath;
        this.currSong = currSong;
    }

    public int getCurrSong() {
        return currSong;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

}
