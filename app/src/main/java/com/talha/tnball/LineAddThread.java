package com.talha.tnball;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by HP-NPC on 24/12/2017.
 */

public class LineAddThread extends Thread {
    DrawingThread drawingThread;
    AnimationThread animationThread;
    boolean lineAddFlag = true;
    public LineAddThread(DrawingThread drawingThread,AnimationThread animationThread){
        this.drawingThread = drawingThread;
        this.animationThread = animationThread;
    }

    @Override
    public void run() {
        lineAddFlag = true;
        while (lineAddFlag){
            double height = drawingThread.displayY/4.5;
            if(drawingThread.lines.size()!=0){
                float lineHieght = drawingThread.lines.get(drawingThread.lines.size()-1).centerY;
                if(height<lineHieght){
                    drawingThread.addLine();
                }
            }else {
                drawingThread.addLine();
            }

            //drawingThread.addLine();
            try {
                sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
