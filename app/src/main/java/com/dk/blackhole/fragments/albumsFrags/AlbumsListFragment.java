package com.dk.blackhole.fragments.albumsFrags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dk.blackhole.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsListFragment extends Fragment {

    public AlbumsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albums_list, container, false);
    }
}
