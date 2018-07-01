package com.example.kaichixie.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class open extends AppCompatActivity {
    MediaPlayer song;
    AudioManager audiovolume;
    public void playing(View view){

        song.start();
    }
    public void pause(View view){
        song.pause();
    }
    // Used to load the 'native-lib' library on application startup.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        
        int min=0;
        song= MediaPlayer.create(this, R.raw.blackspiderman);
        audiovolume = (AudioManager)getSystemService(AUDIO_SERVICE);
        int maxvalue = audiovolume.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ;
        SeekBar volumecontrol = (SeekBar)findViewById(R.id.volumecontrol);
        volumecontrol.setMax(maxvalue);
        volumecontrol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audiovolume.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




        // Example of a call to a native method

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

}
