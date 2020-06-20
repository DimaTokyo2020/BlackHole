package com.DK.blackhole;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.DK.blackhole.model.Image;

public class FragmentHomeActivity extends AppCompatActivity implements ImagesListFragment.Delegate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_home);

        ImagesListFragment imagesListFragment = new ImagesListFragment();
        ImageDetailsFragment imageDetailsFragment = new ImageDetailsFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.home_fragment_container, imagesListFragment,"TAG");
        transaction.commit();


    }

    void openImageDetails(Image image){
        ImageDetailsFragment imageDetailsFragment = new ImageDetailsFragment();
        imageDetailsFragment.setImage(image);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                R.animator.slide_in_right, R.animator.slide_out_left);
        transaction.replace(R.id.home_fragment_container, imageDetailsFragment,"TAG");
        transaction.addToBackStack("TAG");
        transaction.commit();
    }

    @Override
    public void onItemSelected(Image image) {
        openImageDetails(image);
    }
}

