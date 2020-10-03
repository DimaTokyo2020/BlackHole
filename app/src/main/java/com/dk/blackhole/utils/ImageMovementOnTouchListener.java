package com.dk.blackhole.utils;

import android.animation.ObjectAnimator;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.dk.blackhole.App;

public class ImageMovementOnTouchListener extends ScaleGestureDetector.SimpleOnScaleGestureListener implements View.OnTouchListener {


    private final String TAG = ImageMovementOnTouchListener.class.getSimpleName();

    float prevGestureDetectorScaleFactor = 1f;
    PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
    PointF StartPT = new PointF(); // Record Start Position of 'img'
    ScaleGestureDetector scaleGestureDetector;
    ImageView mImageFromCameraIV;
    boolean twoFinger;
    boolean mImagedSizeChangeInThisTouch;
    private float mScaleFactor = 1.0f;
    ImageSizeChangedListener mImageSizeChangedListener;
    ObjectAnimator animation;


    public interface ImageSizeChangedListener{
        void onChange();
    }



    public ImageMovementOnTouchListener(ImageView imageFromCameraIV, ImageSizeChangedListener imageSizeChangedListener) {
        scaleGestureDetector = new ScaleGestureDetector(App.context, this);
        mImageFromCameraIV = imageFromCameraIV;
        mImageSizeChangedListener = imageSizeChangedListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {



        scaleGestureDetector.onTouchEvent(event);
        Log.i(TAG, "mImagedSizeChangeInThisTouch:" + mImagedSizeChangeInThisTouch);


        /* Next 2 if's prevent jumps after resizing image*/
        if(event.getPointerCount() > 1 ){twoFinger = true; }
        if( event.getAction() == MotionEvent.ACTION_UP){twoFinger = false;}


        if(twoFinger  == false ){
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    mImageSizeChangedListener.onChange();
                    mImageFromCameraIV.setX((int) (StartPT.x + event.getX() - DownPT.x));
                    mImageFromCameraIV.setY((int) (StartPT.y + event.getY() - DownPT.y));
                    StartPT.set(mImageFromCameraIV.getX(), mImageFromCameraIV.getY());
                    break;
                case MotionEvent.ACTION_DOWN:
                    DownPT.set(event.getX(), event.getY());
                    StartPT.set(mImageFromCameraIV.getX(), mImageFromCameraIV.getY());
                    break;
                case MotionEvent.ACTION_UP:
                    // Nothing have to do
                    break;
                default:
                    break;
            }
        }
        return true;
    }



    /**
     * This method do the zoom in/out.
     */




        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            Log.i(TAG, "onScale start");
            float newGestureDetectorScaleFactor = scaleGestureDetector.getScaleFactor();
            mScaleFactor *= newGestureDetectorScaleFactor;
            /* This if prevent jump when you yry to zoom in or out. make the zoom more smooth */
            if((prevGestureDetectorScaleFactor > 1 && newGestureDetectorScaleFactor > 1) || (prevGestureDetectorScaleFactor < 1 && newGestureDetectorScaleFactor < 1)){
                Log.i(TAG, "scaleGestureDetector.getScaleFactor:" + scaleGestureDetector.getScaleFactor());
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
                Log.i(TAG, "mScaleFactor:" + mScaleFactor);
                //prevent zoom out if the image is already fit the screen.
                if(mScaleFactor > 1){
                    mImageSizeChangedListener.onChange();
                }
                else{
                    mScaleFactor = 1f;
                    returnImageToOriginalPosition();
                }
                mImageFromCameraIV.setScaleX(mScaleFactor);
                mImageFromCameraIV.setScaleY(mScaleFactor);
            }
            mImagedSizeChangeInThisTouch = true;
            prevGestureDetectorScaleFactor = newGestureDetectorScaleFactor;
            Log.i(TAG, "onScale finished");
            return true;
        }

    public void returnImageToOriginalPosition() {
        animation = ObjectAnimator.ofFloat(mImageFromCameraIV, "translationX", 0f);
        animation.setDuration(500);
        animation.start();
        animation = ObjectAnimator.ofFloat(mImageFromCameraIV, "translationY", 0f);
        animation.setDuration(500);
        animation.start();
    }


//        class ReturnImageToOriginalPosition implements  Runnable{
//
//            int coefficientOfReturnX;
//            int coefficientOfReturnY;
//            boolean first = true;
//
//
//            @Override
//            public void run() {
//
//                Log.i(TAG, "inside handler: " + this.coefficientOfReturnX + " | "  + this.coefficientOfReturnY);
//
//                temp.set(mImageFromCameraIV.getX() + coefficientOfReturnX, mImageFromCameraIV.getX() + coefficientOfReturnY);
//                mImageFromCameraIV.setX(temp.x);
//                mImageFromCameraIV.setY
//
//                Log.i(TAG, "inside handler: " + temp.x + " | " + temp.y);
//                /* Fix bug */
//                if(coefficientOfReturnX == 0 || coefficientOfReturnY == 0){
//                    handler.removeCallbacks(returnImageToOriginalPosition);
//                }
//                else if(temp.x < originalImagePosition[0] + Math.abs(coefficientOfReturnX) && temp.x > originalImagePosition[0] - Math.abs(coefficientOfReturnX)){
//                    mImageFromCameraIV.setX(originalImagePosition[0]);
//                    mImageFromCameraIV.setY(originalImagePosition[1]);
//                }
//                else{
//                    handler.post(returnImageToOriginalPosition);
//                }
//
//            }
//        }

}



