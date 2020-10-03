package com.dk.blackhole.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Utils {

    public static final double COEFFICIENT_COLVER_BYTE_TO_MB = 0.0000009537;


    public static String getNewUniqueId(){return UUID.randomUUID().toString();}//random

    public static String getCurrentTime(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }



    static public byte[] imageViewToByteArr(ImageView imageView){
        Bitmap capture = Bitmap.createBitmap(
                imageView.getWidth(),
                imageView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas captureCanvas = new Canvas(capture);
        imageView.draw(captureCanvas);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        capture.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        byte[] data = outputStream.toByteArray();
        return data;

    }




}
