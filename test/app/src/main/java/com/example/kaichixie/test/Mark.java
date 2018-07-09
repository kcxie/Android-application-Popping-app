package com.example.kaichixie.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class Mark extends View {
    Paint paint =new Paint();
    int setX, setY;

    public Mark(Context context,int x, int y) {
        super(context);
        setX=x;
        setY=y;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int yy=this.setY+10;
        canvas.drawLine(this.setX,this.setY,this.setX,yy,paint);
    }
}
