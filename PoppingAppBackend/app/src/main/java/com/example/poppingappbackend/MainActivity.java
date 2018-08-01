package com.example.poppingappbackend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;



public class MainActivity extends AppCompatActivity {
    EditText SongNameEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SongNameEt = (EditText)findViewById(R.id.song_name);
    }

    public void OnSearch(View view)
    {
        String songname = SongNameEt.getText().toString();
        String type = "search";

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, songname);
    }
}
