package com.dk.blackhole.viwes.fragments.albumsFrags;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;

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
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dk.blackhole.App;
import com.dk.blackhole.models.album.Album;
import com.dk.blackhole.viewModels.HomeActivityViewModel;
import com.dk.blackhole.viwes.components.CustomViewPager;
import com.dk.blackhole.viwes.HomeActivity;
import com.dk.blackhole.viwes.components.OnSwipeTouchListenerVP;
import com.dk.blackhole.R;
import com.dk.blackhole.viwes.fragments.imagesFrags.ImagesListFragment;
import com.dk.blackhole.models.image.Image;
import com.dk.blackhole.models.image.ImagesModelHelper;
import com.dk.blackhole.viewModels.ImagesListViewModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsListFragment extends Fragment {


    private final String TAG = AlbumsListFragment.class.getSimpleName();
    private RecyclerView mRecyclerListView;
    private List<Album> mData  = new ArrayList<>();//to prevent null EXCEPTION
    private List<Image> mDataImagesList  = new ArrayList<>();//to prevent null EXCEPTION
    private AlbumsListAdapter adapter;
    private Context mContext;
    private HomeActivityViewModel mVM;
    private LiveData<List<Album>> liveData;
    private LiveData<List<Image>> liveDataUserImages;
    private View mView;
    private Observer<List<Album>> dataLiveObserverAlbums;
    private Observer<List<Image>> dataLiveObserverImages;

    //ViewPager helper
    private CustomViewPager mCurrentViewPager;
    private ImageButton mCurrentCloseViewPagerBT;
    private  boolean mIsViewpagerOpened = false;
    private int mLastOffsetScreenPosition;
    //

    private Delegate parent;

    public interface Delegate{
        void onItemSelected(Album albums);
    }


    public AlbumsListFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_albums_list, container, false);
        Objects.requireNonNull(mContext = getContext());

        adapter = new AlbumsListFragment.AlbumsListAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerListView = mView.findViewById(R.id.albums_list_list);
        mRecyclerListView.setHasFixedSize(true);
        mRecyclerListView.setLayoutManager(layoutManager);
        mRecyclerListView.setAdapter(adapter);

        //next 3 lines give space between the items in the recyclerView
        DividerItemDecoration itemDecorator = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(mContext, R.drawable.divider_item_decorator_custom)));
        mRecyclerListView.addItemDecoration(itemDecorator);

        //This line help as to close automatically the viewPager if it opened
        mRecyclerListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                Log.i(TAG, "Inside recyclerView onScrolled:  DX: " + dx + " DY:" +dy + " screenOffset: " + getScreenPosition());
                super.onScrolled(recyclerView, dx, dy);
                //This line close automatically the viewPager if its open and we get the offset scroll
                if(Math.abs(getScreenPosition() - mLastOffsetScreenPosition) > HomeActivity.mScreenHeight * 0.3){
                    if(mIsViewpagerOpened){closeViewPager(mCurrentViewPager, mCurrentCloseViewPagerBT);}}
            }
        });

        adapter.setOnItemClickListener(new AlbumsListFragment.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG","row was clicked" + position);
                Album album = mData.get(position);
                parent.onItemSelected(album);
            }
        });

        SwipeRefreshLayout swipeRefresh = mView.findViewById(R.id.image_list_swipe_refresh);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "OnRefresh");
                liveData = ((HomeActivity)getActivity()).getUserAlbums();
                liveData.removeObserver(dataLiveObserverAlbums);
                liveData.observe(getViewLifecycleOwner(),dataLiveObserverAlbums);
                String[] albumsIdes = ((HomeActivity)getActivity()).getUsersAlbumsIdes();
                liveDataUserImages = ImagesModelHelper.getInstance().getUserImages(albumsIdes);
                liveDataUserImages.removeObserver(dataLiveObserverImages);
                liveDataUserImages.observe(getViewLifecycleOwner(),dataLiveObserverImages);

                swipeRefresh.setRefreshing(false);
//                new ImagesModelHelper.CompleteListener() {
//                    @Override
//                    public void onComplete() {
//                        swipeRefresh.setRefreshing(false);//stop the swipe spinner
//                    }
//                });
            }
        });

        /* add to the toolbar icon if an option to create new album*/
//        ((HomeActivity)getActivity()).addOptionToCreateNewAlbum(()->openFragmentForCreatingNewAlbum());



        liveData = ((HomeActivity)getActivity()).getUserAlbums();
        String[] albumsIdes = ((HomeActivity)getActivity()).getUsersAlbumsIdes();
        liveDataUserImages = ImagesModelHelper.getInstance().getUserImages(albumsIdes);


        // Inflate the layout for this fragment
        //liveData = mVM.getData();
        dataLiveObserverAlbums = new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {//new data notification
                mData = albums;
                Log.i(TAG, "notifyDataSetChanged albums size: " + mData.size());
                adapter.notifyDataSetChanged();//refresh the page with new data
            }
        };
        liveData.observe(getViewLifecycleOwner(), dataLiveObserverAlbums);

        dataLiveObserverImages = new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> image) {//new data notification
                mDataImagesList = image;
                Log.i(TAG,"notifyDataSetChanged image size: " + image.size());
                adapter.notifyDataSetChanged();//refresh the page with new data
            }
        };

        liveDataUserImages.observe(getViewLifecycleOwner(), dataLiveObserverImages);


        if(adapter != null){
            adapter.notifyDataSetChanged();//refresh the page with new data
        }
        return mView;
    }

    private void openFragmentForCreatingNewAlbum() {
        Toast.makeText(mContext, "openFrrrr", Toast.LENGTH_SHORT).show();
    }

    private void likeButtonPressed(String albumId, boolean isChecked){
        ((HomeActivity)getActivity()).likeButtonPressed(albumId, isChecked);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HomeActivity)getActivity()).closeOptionToCreateNewAlbum();
    }

    private int getScreenPosition(){return mRecyclerListView.computeVerticalScrollOffset();}

    @Override
    public void onAttach(@NonNull Context context) {//calls once
        super.onAttach(context);
        if(context instanceof ImagesListFragment.Delegate){
//            parent = (ImagesListFragment.Delegate)getActivity();
        }else{
            throw new  RuntimeException(context.toString() + "must implement Delegate");
        }



        mVM = new ViewModelProvider(this). get(HomeActivityViewModel.class);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        parent = null;
    }





      class AlbumRowViewHolder extends RecyclerView.ViewHolder{
        TextView nameTV;
        TextView id;
        TextView imagesNumber;
        TextView dateTV;
        TextView likesNumberTV;
        Album album;
        CheckBox likesCB;
        ImageView imageIV;
        CustomViewPager mViewPager;
        ImageButton  mMinimizeViewPagerBTN;


        private AlbumRowViewHolder(@NonNull View itemView, final AlbumsListFragment.OnItemClickListener listener) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.albumName);
            dateTV = itemView.findViewById(R.id.date_album_row);
            likesNumberTV = itemView.findViewById(R.id.likes_number_row);
            imagesNumber = itemView.findViewById(R.id.images_num_in_album_row);
            imageIV = itemView.findViewById(R.id.row_image);
            mViewPager = itemView.findViewById(R.id.viewPager2);
            mMinimizeViewPagerBTN = itemView.findViewById(R.id.minimizeViewPagerBTN);
            likesCB = itemView.findViewById(R.id.likes_check_box);




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
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.i(TAG, "onTouch: " + getAdapterPosition());
                    return false;
                }
            });

        }

        private void bind(Album st, int i, String[] imagesUrls) {


            String date = st.getWasCreated();
            String[] dateSpited = date.split("a");
            date = dateSpited[0];
            dateTV.setText(date);
            nameTV.setText(st.getName());
            imagesNumber.setText(""+st.getImagesNumber());
            likesNumberTV.setText(""+st.getLikesNumber());
            album = st;

//            likesCB.setOnClickListener(v->likeButtonPressed(mData.get(i).getId(), likesCB.isChecked()));
            likesCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(true ==likesCB.isChecked()){
                        likesCB.setChecked(true);
                        likeButtonPressed(album.getId(), true);
                        Log.i(TAG, "liked: " + album.getId());
                    }
                    else{
                        likesCB.setChecked(false);
                        likeButtonPressed(album.getId(), false);
                        Log.i(TAG, "Desliked: " + album.getId());
                    }
                }
            });

//            if (bars.get(position).isWatched()) {
//                holder.thumbnailView.setVisibility(View.GONE);
//            } else {
//                holder.thumbnailView.setVisibility(View.VISIBLE);
//            }

                    Log.i(TAG, "inside bind");
            initPagerViewer(mViewPager,mMinimizeViewPagerBTN,imagesUrls);
        }
    }

    interface OnItemClickListener{
        void onClick(int position);
    }


    class AlbumsListAdapter extends RecyclerView.Adapter<AlbumsListFragment.AlbumRowViewHolder>{
        private AlbumsListFragment.OnItemClickListener listener;

        void setOnItemClickListener(AlbumsListFragment.OnItemClickListener listener){
            this.listener = listener;
        }


        @NonNull
        @Override
        public AlbumsListFragment.AlbumRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.album_list_row, viewGroup,false );
            //ImageRowViewHolder vh = new ImageRowViewHolder(v, listener);
            return new AlbumsListFragment.AlbumRowViewHolder(v, listener);
        }

        /**
         * This function called every time new row created.
         * @param imageRowViewHolder
         * @param i
         */
        @Override
        public void onBindViewHolder(@NonNull AlbumsListFragment.AlbumRowViewHolder imageRowViewHolder, int i) {
            Log.i(TAG, "onBindViewHolder");

            Album st = mData.get(i);
            ArrayList<String> imagesUrls = new ArrayList<>();
            for(Image image : mDataImagesList){
                if(image.getAlbumId().equals(st.getId())){
                    imagesUrls.add(image.getImgUrl());
                }
            }
            String[] imagesUrlss = new String[imagesUrls.size()];
            for(int j = 0; j < imagesUrls.size(); j++){
                imagesUrlss[j] = imagesUrls.get(j);
            }

            imageRowViewHolder.bind(st, i, imagesUrlss);


        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }


    private void initPagerViewer(CustomViewPager viewPager, ImageButton closePagerViewBT, String[] imagesUrls){


//ViewPager  //imageSlider
//        CustomViewPager viewPager = mView.findViewById(R.id.viewPager);
        viewPager.setPagingEnabled(false);//stop swiping
        viewPager.setPadding(0, 0, 100, 0);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(10);




        ViewPagerAdapter adapter = new ViewPagerAdapter(App.context , imagesUrls);

        viewPager.setAdapter(adapter);
        setViewPagerOnTouchListener(viewPager, closePagerViewBT);

        viewPager.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                Log.i("dima", "onhover");
                return false;
            }
        });

        viewPager.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.i("dima", "onDrage");
                return false;
            }
        });
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("dima", "onClick");
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


//                Log.i("dimaa", "position: " + position);
//                Log.i("dimaa", "positionOffset: " + positionOffset);
//                Log.i("dimaa", "positionOffsetPixels: " + positionOffsetPixels);
//                if(position == 0 && positionOffset > 0.1 && position < 0.2){
////                ObjectAnimator animation = ObjectAnimator.ofFloat(viewPager, "translationX", -800f );
////                animation.setDuration(2000);
////                    animation.start();
////
//                    Handler handler = new Handler();
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            ObjectAnimator animation = ObjectAnimator.ofFloat(viewPager, "translationX", -600f );
//                            animation.setDuration(500);
//                            animation.start();
//                        }
            }



            @Override
            public void onPageSelected(int position) {
                Log.i("dimaa", "b");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("dimaa", "a");
            }
        });



        closePagerViewBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeViewPager(viewPager,closePagerViewBT);
            }
        });
    }

    private void closeViewPager(CustomViewPager viewPager, ImageButton closePagerViewBT) {
        mIsViewpagerOpened = false;
        if(viewPager == null || closePagerViewBT == null){return;}
        ObjectAnimator animation = ObjectAnimator.ofFloat(viewPager, "translationX", +0f );
        animation.setDuration(500);
        animation.start();
        closePagerViewBT.setVisibility(View.INVISIBLE);
        viewPager.setCurrentItem(0,true);
        viewPager.setPagingEnabled(false);//stop swiping
        setViewPagerOnTouchListener(viewPager, closePagerViewBT);
    }


    private void setViewPagerOnTouchListener(CustomViewPager viewPager, ImageButton closePagerViewBT) {
        viewPager.setOnTouchListener(new OnSwipeTouchListenerVP(App.context){
            public void onSwipeLeft(){
                if(mCurrentCloseViewPagerBT != null && mCurrentViewPager != null){closeViewPager(mCurrentViewPager, mCurrentCloseViewPagerBT);}//If the user open another viewPager we close the other that opened before
                //Next lines gives as the ability to access the specific viewpager anytime we need
                mIsViewpagerOpened = true;
                mCurrentViewPager = viewPager;
                mCurrentCloseViewPagerBT = closePagerViewBT;
                mLastOffsetScreenPosition = getScreenPosition();
                //////

                Log.i(TAG,"Left move animation value: " + (-HomeActivity.mScreenWidth * 0.80f));
                ObjectAnimator animation = ObjectAnimator.ofFloat(viewPager, "translationX", -HomeActivity.mScreenWidth * 0.80f);// This line open the viewpager to the left side
                animation.setDuration(500);
                animation.start();
                viewPager.setPagingEnabled(true);//start swiping between images
                viewPager.setOnTouchListener(null);//stop on touchListener
                closePagerViewBT.setVisibility(View.VISIBLE);

            }
            //TODO find a way to close the viewPager by swipeRight, right know its impossible because its make the swiping between images not smooth when there is a listener on swipe.
        });

    }
}




