package com.dk.blackhole.fragments.imagesFrags;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.dk.blackhole.R;
import com.dk.blackhole.fragments.imagesFrags.ImageDetailsFragmentArgs;
import com.dk.blackhole.model.Image;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageDetailsFragment extends Fragment {
    private final String TAG = ImageDetailsFragment.class.getSimpleName();
    private Image image;
    private TextView name;
    private TextView id;
    private Button backBTN;

    public ImageDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_details, container, false);

        initUIElements(view);
        Bundle arguments = getArguments();
        if(arguments != null) {
            image = ImageDetailsFragmentArgs.fromBundle(arguments).getImage();//here we receive the image from imagesList
        }
        else {
            Log.e(TAG, "onCreateView arguments = null");
        }

        if (image != null){
            update_display();
        }
        return view;
    }

    private void initUIElements(View view) {
        name = view.findViewById(R.id.image_details_name_tv);
        id = view.findViewById(R.id.image_details_id_tv);
        backBTN = view.findViewById(R.id.closeDetailsImageFragmentBTN);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.popBackStack();
            }
        });

    }

    private void update_display() {
        name.setText(image.getName());
        id.setText(image.getId());
    }

    public void setImage(Image image) {
        this.image = image;
        if (name != null){
            update_display();
        }
    }

}
