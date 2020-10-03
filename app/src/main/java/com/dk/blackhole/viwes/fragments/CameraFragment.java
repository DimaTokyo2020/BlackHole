package com.dk.blackhole.viwes.fragments;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.dk.blackhole.App;
import com.dk.blackhole.R;
import com.dk.blackhole.models.image.Image;
import com.dk.blackhole.models.image.ImagesModelHelper;
import com.dk.blackhole.models.user.User;
import com.dk.blackhole.models.user.UserModelHelper;
import com.dk.blackhole.utils.Utils;
import com.dk.blackhole.utils.ImageMovementOnTouchListener;
import com.dk.blackhole.viwes.HomeActivity;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.dk.blackhole.utils.Utils.COEFFICIENT_COLVER_BYTE_TO_MB;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {

    private final String TAG = CameraFragment.class.getSimpleName();
    public static final String USER_INFO = "USER_INFO";

    private String mCurrentPhotoPath;

    private ImageView mImageFromCameraIV;
    private final int CAMERA_REQUEST = 1888;
    private ArrayList<Button> buttonsList;
    private Handler mButtonVisibilityHandler;
    private ImageMovementOnTouchListener imageMovementOnTouchListener;
    private ImagesModelHelper mImagesModelHelper;
    private User mCurrentUser;
    private Image mCurrentImageInfo;


    ///////////////////////
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    ProgressBar progressBar;
    ///////////////////////

    /* Required empty public constructor */
    public CameraFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "CameraFragment onCreateView");

        /* Inflate the layout for this fragment */
        View viewOfTheFragment = inflater.inflate(R.layout.fragment_camera, container, false);
        mImageFromCameraIV = viewOfTheFragment.findViewById(R.id.imageFromCameraIV);
        initButtons(viewOfTheFragment);
        mImagesModelHelper = ImagesModelHelper.getInstance();
        progressBar = viewOfTheFragment.findViewById(R.id.progressCircular);
        mButtonVisibilityHandler = new Handler();

        /* Give the ability to zoom in and out*/
        imageMovementOnTouchListener = new ImageMovementOnTouchListener(mImageFromCameraIV, new ImageMovementOnTouchListener.ImageSizeChangedListener() {
            @Override
            public void onChange() {
                for(Button button : buttonsList){button.setAlpha(0.2f);}
                mButtonVisibilityHandler.removeCallbacks(mSetButtonsVisible);
                mButtonVisibilityHandler.postDelayed(mSetButtonsVisible,250);
            }
        });
        mImageFromCameraIV.setOnTouchListener((view,motionEvent)->imageMovementOnTouchListener.onTouch(view,motionEvent));

        dispatchTakePictureIntent();

        return viewOfTheFragment;
    }


    /**
     * This method like onCreate in Activity.
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundleFromOtherFragOrActivity = this.getArguments();
        if (bundleFromOtherFragOrActivity != null) {
            mCurrentUser = (User)bundleFromOtherFragOrActivity.getSerializable(USER_INFO);
        }
    }

    private void initButtons(View viewOfTheFragment) {
        buttonsList = new ArrayList<>();

        Button button = viewOfTheFragment.findViewById(R.id.uploadBTN);
        button.setOnClickListener(v->upLoadImage());
        buttonsList.add(button);

        button = viewOfTheFragment.findViewById(R.id.retakeImageBTN);
        button.setOnClickListener(v->reTakeImage());
        buttonsList.add(button);

        button = viewOfTheFragment.findViewById(R.id.deleteBTN);
        button.setOnClickListener(v->closeThisFragmentAndOpeHomeFragment());
        buttonsList.add(button);

        button = viewOfTheFragment.findViewById(R.id.moveToOriginalPositionBTN);
        button.setOnClickListener(v->returnImageToOriginalPosition());
        buttonsList.add(button);
    }

    private void closeThisFragmentAndOpeHomeFragment() {
        getFragmentManager().popBackStack();

    }

    private void reTakeImage() {
        dispatchTakePictureIntent();
    }

    /**
     * 1) We ask the user in which album he/she want to insert the new image.
     * 2) Then we upload the new image and updating the db.
     * 3) Updating the images number in this album.
     */
    private void upLoadImage() {
        ((HomeActivity)getActivity()).popAlumsChoose(new HomeActivity.InputFromUserListener() {
            @Override
            public void userInputInserted(String inputFromUser) {

                mCurrentImageInfo.setAlbumId(inputFromUser);
                mImagesModelHelper.uploadAndInsertImageToDb(mImageFromCameraIV, mCurrentImageInfo, new ImagesModelHelper.listenerUpload() {
                    HomeActivity homeActivity = (HomeActivity)getActivity();

                    @Override
                    public void started() { homeActivity.startUploadAnimation(); }

                    @Override
                    public void onCompleteUpload() { homeActivity.stopUploadAnimation();}

                    @Override
                    public void finished(String uploadUri, String path) { }
                });
            }
        });

    }

    public void returnImageToOriginalPosition() {
        mImageFromCameraIV.setScaleX(1f);
        mImageFromCameraIV.setScaleY(1f);
        ObjectAnimator animation = ObjectAnimator.ofFloat(mImageFromCameraIV, "translationX", 0f);
        animation.setDuration(500);
        animation.start();
        animation = ObjectAnimator.ofFloat(mImageFromCameraIV, "translationY", 0f);
        animation.setDuration(500);
        animation.start();
    }

    /**
     * Here we get notify that the image was taken and we can pull it.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
            int imageOrientation = getImageOrientation(mCurrentPhotoPath);
            bmp = rotateBitmap(bmp, imageOrientation);
            mImageFromCameraIV.setImageBitmap(bmp);

            mImageFromCameraIV.setScaleX(1f);
            mImageFromCameraIV.setScaleY(1f);
            createImageInfo();
        }
    }

    private void createImageInfo(){
        /* calculate image size in MB */
        File file = new File(mCurrentPhotoPath);
        String imageSizeInMb = String.valueOf(file.length() * COEFFICIENT_COLVER_BYTE_TO_MB);
        String imageWidth = String.valueOf(mImageFromCameraIV.getWidth());
        String imageHeight = String.valueOf(mImageFromCameraIV.getHeight());
        String creationTime = Utils.getCurrentTime();
        /* generated new unique id */
        String imageId = Utils.getNewUniqueId();
        String userId = mCurrentUser.getId();
        mCurrentImageInfo = new Image(imageId, imageId, "album_test", userId, imageSizeInMb, imageHeight, imageWidth, true, 0, "", creationTime, creationTime, false);
    }


    /**
     * This method call the standard camera app on the phone to take
     * image and decide where to save  this new image.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(App.context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try { photoFile = createImageFile(); } catch (IOException ex) { Log.e(TAG, "Failed to create image file!");}
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(App.context,
                        "com.dk.blackhole",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    /**
     * This method create the file where the image will be placed
     * after the standard camera app will take image.
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = App.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private int getImageOrientation(String path){
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int imageOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        return imageOrientation;
    }

    /**
     * Rotate the image to the right orientation.
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private final Runnable mSetButtonsVisible = new Runnable() {
        @Override
        public void run() {
            for(Button button : buttonsList){button.setAlpha(1f);}
        }
    };


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        App.context.sendBroadcast(mediaScanIntent);
    }


    private void fitImageInsideImageView() {
        // Get the dimensions of the View
        int targetW = mImageFromCameraIV.getWidth();
        int targetH = mImageFromCameraIV.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int imageOrientation = getImageOrientation(mCurrentPhotoPath);
        bitmap = rotateBitmap(bitmap, imageOrientation);

        mImageFromCameraIV.setImageBitmap(bitmap);
    }







}
