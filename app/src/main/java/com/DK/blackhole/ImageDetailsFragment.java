package com.DK.blackhole;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.DK.blackhole.model.Image;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageDetailsFragment extends Fragment {
    private Image image;
    TextView name;
    TextView id;

    public ImageDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_details, container, false);

        name = view.findViewById(R.id.image_details_name_tv);
        id = view.findViewById(R.id.image_details_id_tv);

        if (image != null){
            update_display();
        }
        return view;
    }

    private void update_display() {
        name.setText(image.name);
        id.setText(image.id);
    }

    public void setImage(Image image) {
        this.image = image;
        if (name != null){
            update_display();
        }
    }

}
