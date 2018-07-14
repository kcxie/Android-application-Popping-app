package com.example.kaichixie.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

 class Markpos extends View{
     int x,y;

    public Markpos(Context context,int a, int b) {
        super(context);
        Log.i("asdsadas","drawing");
        x=a;
        y=b;
    }

     @Override
     protected void onDraw(Canvas canvas) {
         Log.i("asdsadas","drawing");
         Paint ourblue=new Paint();
         ourblue.setColor(Color.BLUE);
         super.onDraw(canvas);
         canvas.drawLine(0,0,canvas.getWidth()/2,canvas.getHeight()/2,ourblue);

     }
 }


public class MainActivity extends AppCompatActivity {
    MediaPlayer mp;
    boolean clk=false;
    boolean setted;
    Button secondbutton=null;
    ConstraintLayout mymainlayout;
    public void play(View view) {

        mp.start();

    }

    public void pause(View view) {

        mp.pause();

    }
    public void second(View view){
        clk=true;
    }



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Markpos test1 = new Markpos(this,30,60);
        mymainlayout=(ConstraintLayout) findViewById(R.id.mymainlayout);

        mp=MediaPlayer.create(this, R.raw.blackspiderman);
        final SeekBar pg=(SeekBar)findViewById(R.id.progressbar);

        pg.setMax(mp.getDuration());
        pg.setScaleY(4f);
        pg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mp.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        secondbutton= (Button) findViewById(R.id.button5);
        secondbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Markpos test1 = new Markpos(MainActivity.this,30,60);
                mymainlayout=(ConstraintLayout) findViewById(R.id.mymainlayout);
                mymainlayout.addView(test1);


            }
        });


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {



                pg.setProgress(mp.getCurrentPosition());


            }
        }, 0, 300);


    }
}
