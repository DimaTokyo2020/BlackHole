package com.DK.blackhole.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DK.blackhole.R;
import com.DK.blackhole.model.Image;
import com.DK.blackhole.model.Model;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesListFragment extends Fragment {

    private RecyclerView list;
    private List<Image> data;
    private ImagesListAdapter adapter;

    public interface Delegate{
        void onItemSelected(Image image);
    }

    private Delegate parent;

    void setTitle(String title){

    }

    public ImagesListFragment() {
        data = Model.instance.getAllImages();
    }

    /*
    public ImagesListFragment() {
        Model.instance.getAllImages(new Model.Listener<List<Image>>() {
            @Override
            public void onComplete(List<Image> _data) {
                data = _data;
                if (adapter != null){
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }*/



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images_list, container, false);

        list = view.findViewById(R.id.images_list_list);
        list.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);


        adapter = new ImagesListAdapter();
        list.setAdapter(adapter);
        //next 3 lines give space between the items in the recyclerView
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_item_decorator_custom));
        list.addItemDecoration(itemDecorator);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG","row was clicked" + position);
                Image image = data.get(position);
                parent.onItemSelected(image);
            }
        });

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof  Delegate){
            parent = (Delegate)getActivity();
        }else{
            throw new  RuntimeException(context.toString() + "must implement Delegate");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parent = null;
    }


    static class ImageRowViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView id;
        CheckBox cb;
        Image image;
        ImageView imageIV;


        private ImageRowViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.row_name_tv);
            id = itemView.findViewById(R.id.row_id_tv);
            cb = itemView.findViewById(R.id.row_cb);
            imageIV = itemView.findViewById(R.id.row_image);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image.isChecked = cb.isChecked();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onClick(position);
                        }
                    }
                }
            });

        }

        private void bind(Image st) {
            name.setText(st.name);
            id.setText(st.id);
            cb.setChecked(st.isChecked);
            image = st;
        }
    }

    interface OnItemClickListener{
        void onClick(int position);
    }


    class ImagesListAdapter extends RecyclerView.Adapter<ImageRowViewHolder>{
        private OnItemClickListener listener;

        void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }


        @NonNull
        @Override
        public ImageRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.list_row, viewGroup,false );
            //ImageRowViewHolder vh = new ImageRowViewHolder(v, listener);
            return new ImageRowViewHolder(v, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageRowViewHolder imageRowViewHolder, int i) {
            Image st = data.get(i);
            imageRowViewHolder.bind(st);

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
