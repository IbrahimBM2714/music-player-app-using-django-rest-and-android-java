package com.ibrahimbinmansoor.musicplayer;

import androidx.fragment.app.Fragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class FragmentOnline extends Fragment {

    TextView song, artist;
    SeekBar seekBar;
    String url;
    String songName1;
    String artistName1;
    MediaPlayer mediaPlayer2;
    View view;
    Handler handler;
    TextView songDurationEnd;
    TextView songDurationStart;
//    boolean isPaused = false;
    boolean isUserSeeking = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_online, container, false);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        song = view.findViewById(R.id.song);
        artist = view.findViewById(R.id.artist);
        seekBar = view.findViewById(R.id.seekBar);
        songDurationEnd = view.findViewById(R.id.songDurationEnd);
        songDurationStart = view.findViewById(R.id.songDurationStart);
        mediaPlayer2 = new MediaPlayer();
        handler = new Handler();

        Bundle args = getArguments();

        // retrieving the data sent to the fragment.
        if (args != null) {
            try {
                url = args.getString("url");
                artistName1 = args.getString("songArtist");
                songName1 = args.getString("songName");
            } catch (Exception e) {

            }
        }


        song.setText(songName1);
        artist.setText(artistName1);

        playMusic(url);

        // this seekBar listener is to update the song based on the user's movement of the seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (mediaPlayer2 != null && isUserSeeking) {
                        mediaPlayer2.seekTo(progress);
                    }

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserSeeking = false;
            }
        });


        return view;
    }

    void pauseMusic() {
        if (mediaPlayer2 != null) {
            if (mediaPlayer2.isPlaying()) {
                mediaPlayer2.pause();
            } else {
                mediaPlayer2.start();
            }
        }
    }

    // This method plays the song.
    private void playMusic(String url) {
        //Releasing the current mediaPlayer so that the songs don't play on top of each other.
        if (mediaPlayer2 != null) {
            if (mediaPlayer2.isPlaying()) {
                mediaPlayer2.pause();
            }
            mediaPlayer2.stop();
            mediaPlayer2.reset();
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }


        // Preparing and playing the song.
        mediaPlayer2 = new MediaPlayer();
        mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer2.setDataSource(url);
            mediaPlayer2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {

                    mediaPlayer2.start();

                    // Updating the song end duration number
                    seekBar.setMax(mediaPlayer2.getDuration());
                    int duration = mediaPlayer2.getDuration();
                    int minutes = (duration / 1000) / 60;
                    int seconds = (duration / 1000) % 60;
                    String durationString = String.format("%02d:%02d", minutes, seconds);
                    songDurationEnd.setText(durationString);

                    updateSeekBar();
                }
            });

            // Set the buffer percentage progress
            mediaPlayer2.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
                    // Calculate the buffer progress in relation to the seek bar maximum value
                    int bufferProgress = (seekBar.getMax() / 100) * percent;
                    seekBar.setSecondaryProgress(bufferProgress);
                }
            });
            mediaPlayer2.prepareAsync();
        } catch (Exception e) {
        }
    }

    public void updateSeekBar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer2 != null && mediaPlayer2.isPlaying()) {
                    seekBar.setProgress(mediaPlayer2.getCurrentPosition());
                    handler.postDelayed(this, 100);

                }
            }
        }, 100);
    }

//     onPause and onStop stops the mediaPlayer and the current song.
    @Override
    public void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer2 != null) {
            if (mediaPlayer2.isPlaying()) {
                mediaPlayer2.stop();
            }
            mediaPlayer2.reset();
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}