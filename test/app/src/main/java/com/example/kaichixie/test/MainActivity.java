package com.example.kaichixie.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.MotionEvent.ACTION_DOWN;

class Markpos extends View{
     int x,y;

    public Markpos(Context context,int a, int b) {
        super(context);
        this.setWillNotDraw(false);
        Log.i("asdsadas","drawing");
        x=a;
        y=b;
    }

     @Override
     protected void onDraw(Canvas canvas) {
         Log.i("asdsadas","drawing");
         Paint ourblue=new Paint();
         ourblue.setStrokeWidth(15);
         ourblue.setColor(Color.rgb(139,139,0));
         super.onDraw(canvas);
         canvas.drawLine(x,0,x,y,ourblue);

     }
 }


public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;
    Button Markbutton=null;
    ConstraintLayout mymainlayout;
    View thumb;


    public void play(View view) {
        mp.start();
    }
    public void pause(View view) {
        mp.pause();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loopbutton;

        mymainlayout= findViewById(R.id.mymainlayout);

       mp=MediaPlayer.create(this, R.raw.blackspiderman);
       /*String requestURL=Uri.encode("https://s3.us-east-2.amazonaws.com/appsongs/simple+(prod.+Frith).mp3","UTF-8").replaceAll("\\+", "%20")
                .replaceAll("\\%21", "!")
                .replaceAll("\\%27", "'")
                .replaceAll("\\%28", "(")
                .replaceAll("\\%29", ")")
                .replaceAll("\\%7E", "~");
      /* String requestURL = String.format("https://s3.us-east-2.amazonaws.com/appsongs/simple+(prod.+Frith).mp3", Uri.encode("foo bar"), Uri.encode("100% fubar'd"));
        Uri onlinesong=Uri.parse(requestURL);
        onlinesong.getPath();

        try{
            mp = new MediaPlayer();
            mp.setDataSource(this,onlinesong);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare(); //don't use prepareAsync for mp3 playback

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*mp=MediaPlayer.create(this, "https://s3.us-east-2.amazonaws.com/appsongs/Childish+Gambino+-+This+Is+America+(LYRICS)+HD.mp3");*/


        /*The comments above are attempts to connect URI from AWS*/

        final SeekBar pg= findViewById(R.id.progressBar1);
        Markbutton= findViewById(R.id.Mark);
        final Markpos arr_marks[]= new Markpos[100]; /*This can include 100 marks we want in a song. But for now, we only use the first two for one loop*/
        final int loop_time[]=new int[2]; /*To record the time range for the loop */
        loop_time[0]=0;
        loop_time[1]=0;
        mymainlayout= findViewById(R.id.mymainlayout);
        loopbutton= findViewById(R.id.loop);

        /*This listener gets the song ready for playing*/

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                pg.setMax(mp.getDuration());
            }
        });


        pg.setOnSeekBarChangeListener
                (new SeekBar.OnSeekBarChangeListener() {

            @Override
        /*Boolean b is "from user", the version before it calls seekTo(i) every millisecond the song goes. To match the dragging thumb functionality and playing the song smoothly,
         * we only call seekTo() when it is a change from the user */
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(b==true){
                mp.seekTo(i);


                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

                });

        Markbutton.setOnClickListener(new View.OnClickListener() {

            int count=0;
            @Override
            public void onClick(View view) {

                /*Below, it is calculation for the x-axis of the thumb position*/
                int width = pg.getWidth()
                        - pg.getPaddingLeft()
                        - pg.getPaddingRight();
                int height=pg.getHeight()+pg.getBaseline();
                int thumbPos = pg.getPaddingLeft()
                        + width
                        * pg.getProgress()
                        / pg.getMax();

                /*Every time we create a markpos object, itself calls onDraw()*/
               arr_marks[count]=new Markpos(MainActivity.this,thumbPos,height);
               /*Record the first range*/
               if(count<2){
               loop_time[count]=mp.getCurrentPosition();

               }

               mymainlayout.addView(arr_marks[count]);/*This is called so the view can be put on the actual interface*/

               count=count+1;



            }
        });

        loopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pg.setProgress(loop_time[0]); /*loop_time[0] is the time start for the loop*/
                mp.seekTo(loop_time[0]);

            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if(
                        ((mp.getCurrentPosition()-mp.getCurrentPosition()%100)<=((loop_time[1]-(loop_time[1]%100))+50))&&
                                //For some reason the "mp.getCurrentPosition()-mp.getCurrentPosition()%100" is slightly bigger or smaller than the result i expected sometimes, so I put a range here.
                        ((mp.getCurrentPosition()-mp.getCurrentPosition()%100)>=((loop_time[1]-(loop_time[1]%100))-50))&& (loop_time[1]!=0) /*MAKE Sure range is set up*/
                        )
                {
                    pg.setProgress(loop_time[0]);
                    mp.seekTo(loop_time[0]);
                }

                else{

                        pg.setProgress(mp.getCurrentPosition());
                    }


            }
        }, 0, 100);/*timer checks every 100 milisecond*/


    }
}
