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
    boolean clk=false;
    boolean setted;
    Button Markbutton=null;
    ConstraintLayout mymainlayout;
    View thumb;
    SeekBar change1;
    public void play(View view) {
        mp.start();
    }

    public void pause(View view) {
        mp.pause();
    }
    public void second(View view){
        clk=true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loopbutton;

        mymainlayout= findViewById(R.id.mymainlayout);

      /* mp=MediaPlayer.create(this, R.raw.blackspiderman);*/
       String requestURL=Uri.encode("https://s3.us-east-2.amazonaws.com/appsongs/simple+(prod.+Frith).mp3","UTF-8").replaceAll("\\+", "%20")
                .replaceAll("\\%21", "!")
                .replaceAll("\\%27", "'")
                .replaceAll("\\%28", "(")
                .replaceAll("\\%29", ")")
                .replaceAll("\\%7E", "~");
      /* String requestURL = String.format("https://s3.us-east-2.amazonaws.com/appsongs/simple+(prod.+Frith).mp3", Uri.encode("foo bar"), Uri.encode("100% fubar'd"));*/
        Uri onlinesong=Uri.parse(requestURL);
        onlinesong.getPath();

        try{
            mp = new MediaPlayer();
            mp.setDataSource(this,onlinesong);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare(); //don't use prepareAsync for mp3 playback

        } catch (IOException e) {
            e.printStackTrace();
        }


        /*mp=MediaPlayer.create(this, "https://s3.us-east-2.amazonaws.com/appsongs/Childish+Gambino+-+This+Is+America+(LYRICS)+HD.mp3");*/
        final SeekBar pg= findViewById(R.id.progressBar1);
        Markbutton= findViewById(R.id.Mark);
        final Markpos arr_marks[]= new Markpos[100];
        final int loop_time[]=new int[2];
        loop_time[0]=0;
        loop_time[1]=0;
        mymainlayout= findViewById(R.id.mymainlayout);
        loopbutton= findViewById(R.id.loop);

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                pg.setMax(mp.getDuration());
            }
        });





        pg.setOnSeekBarChangeListener
                (new SeekBar.OnSeekBarChangeListener() {
                    private int move;
            @Override

            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(b==true){
                mp.seekTo(i);


                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                    move=seekBar.getProgress();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


                });

        Markbutton.setOnClickListener(new View.OnClickListener() {

            int count=0;
            @Override
            public void onClick(View view) {

                Log.i("count",Integer.toString(count));


                int width = pg.getWidth()
                        - pg.getPaddingLeft()
                        - pg.getPaddingRight();
                int height=pg.getHeight()+pg.getBaseline();
                int thumbPos = pg.getPaddingLeft()
                        + width
                        * pg.getProgress()
                        / pg.getMax();

               /* Log.i("x", Integer.toString(x));
                Log.i("y",Integer.toString(y));*/
               arr_marks[count]=new Markpos(MainActivity.this,thumbPos,height);
               if(count<2){
               loop_time[count]=mp.getCurrentPosition();
                   Log.i("inside",Integer.toString(loop_time[count]));

               }
               mymainlayout.addView(arr_marks[count]);
               Log.i("x",Integer.toString(arr_marks[count].x));
               count=count+1;
               /* arr_marks[count].x = thumbPos;
                arr_marks[count].y = height;*/

                /*multiplemark.x=thumbPos;
                multiplemark.y=height;
                multiplemark.invalidate();*/

               /* Markpos test1 = new Markpos(MainActivity.this,test[0],test[1]);
                mymainlayout=(ConstraintLayout) findViewById(R.id.mymainlayout);
                mymainlayout.addView(multiplemark);*/


            }
        });

        loopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ttt",Integer.toString(loop_time[0]));
                pg.setProgress(loop_time[0]);
                mp.seekTo(loop_time[0]);

            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i("mpcurrent", Integer.toString(mp.getCurrentPosition()));

                Log.i("a", Integer.toString(mp.getCurrentPosition()-mp.getCurrentPosition()%100));
                Log.i("b", Integer.toString(loop_time[1]-(loop_time[1]%100)));
                if(
                        ((mp.getCurrentPosition()-mp.getCurrentPosition()%100)<=((loop_time[1]-(loop_time[1]%100))+50))&&

                                //For some reason the "mp.getCurrentPosition()-mp.getCurrentPosition()%100" is slightly bigger or smaller than the result i expected sometimes, so I put a range here.

                        ((mp.getCurrentPosition()-mp.getCurrentPosition()%100)>=((loop_time[1]-(loop_time[1]%100))-50))
                        ){
                    pg.setProgress(loop_time[0]);
                    mp.seekTo(loop_time[0]);
                }

                else{

                        pg.setProgress(mp.getCurrentPosition());
                    }


            }
        }, 0, 100);


    }
}
