package com.dk.blackhole.viwes.fragments.imagesFrags;


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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dk.blackhole.viewModels.ImagesListViewModel;
import com.dk.blackhole.R;
import com.dk.blackhole.models.image.Image;
import com.dk.blackhole.models.image.ImagesModelHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ImagesListFragment extends Fragment {

    private RecyclerView mListView;
    private List<Image> mData  = new ArrayList<>();//to prevent null EXCEPTION
    private ImagesListAdapter adapter;
    private Context mContext;
    private ImagesListViewModel mVM;
    private LiveData<List<Image>> liveData;

    public interface Delegate{
        void onItemSelected(Image image);
    }

    private Delegate parent;


    public ImagesListFragment() {
        liveData = ImagesModelHelper.getInstance().getAllImages("dsds");

        if(adapter != null){
            adapter.notifyDataSetChanged();//refresh the page with new data
        }

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


    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override//could calls many times
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_images_list, container, false);
        Objects.requireNonNull(mContext = getContext());

        //liveData = mVM.getData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {//new data notification
                mData = images;
                adapter.notifyDataSetChanged();//refresh the page with new data
            }
        });

        adapter = new ImagesListAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mListView = view.findViewById(R.id.images_list_list);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(layoutManager);
        mListView.setAdapter(adapter);


        //next 3 lines give space between the items in the recyclerView
        DividerItemDecoration itemDecorator = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(mContext, R.drawable.divider_item_decorator_custom)));
        mListView.addItemDecoration(itemDecorator);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG","row was clicked" + position);
                Image image = mData.get(position);
                parent.onItemSelected(image);
            }
        });

        SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.image_list_swipe_refresh);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mVM.refresh(new ImagesModelHelper.CompleteListener() {
                    @Override
                    public void onComplete() {
                        swipeRefresh.setRefreshing(false);//stop the swipe spinner
                    }
                });
            }
        });
        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {//calls once
        super.onAttach(context);
        if(context instanceof  Delegate){
            parent = (Delegate)getActivity();
        }else{
            throw new  RuntimeException(context.toString() + "must implement Delegate");
        }


        mVM = new ViewModelProvider(this). get(ImagesListViewModel.class);
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
//            cb.setOnClickListener(new View.OnClickListener() {
//                @Override
////                public void onClick(View v) { image.setIsChecked(cb.isChecked());
//                }
//            });

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
            name.setText(st.getName());
            id.setText(st.getId());
//            cb.setChecked(st.getIsChecked());
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
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.image_list_row, viewGroup,false );
            //ImageRowViewHolder vh = new ImageRowViewHolder(v, listener);
            return new ImageRowViewHolder(v, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageRowViewHolder imageRowViewHolder, int i) {
            Image st = mData.get(i);
            imageRowViewHolder.bind(st);

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }
}
