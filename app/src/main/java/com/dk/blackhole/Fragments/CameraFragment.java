package com.dk.blackhole.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dk.blackhole.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {

    private final int CAMERA_REQUEST = 1888;
    private ImageView imageFromCameraIV;
    private Bitmap imageFromCameraBM;

    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("TAG", "intent");
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        imageFromCameraIV = view.findViewById(R.id.imageFromCameraIV);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Objects.requireNonNull(imageFromCameraBM = (Bitmap) data.getExtras().get("data"));
            imageFromCameraIV.setImageBitmap(imageFromCameraBM);


            //photo=getEncodedString(theImage);
            //setDataToDataBase();
        }

    }
}
