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
import com.dk.blackhole.App;
import com.dk.blackhole.viwes.components.CustomViewPager;
import com.dk.blackhole.viwes.HomeActivity;
import com.dk.blackhole.viwes.components.OnSwipeTouchListenerVP;
import com.dk.blackhole.R;
import com.dk.blackhole.viwes.fragments.imagesFrags.ImagesListFragment;
import com.dk.blackhole.models.image.Image;
import com.dk.blackhole.models.image.ImagesModel;
import com.dk.blackhole.viewModels.ImagesListViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsListFragment extends Fragment {


    private final String TAG = AlbumsListFragment.class.getSimpleName();
    private RecyclerView mRecyclerListView;
    private List<Image> mData  = new ArrayList<>();//to prevent null EXCEPTION
    private AlbumsListAdapter adapter;
    private Context mContext;
    private ImagesListViewModel mVM;
    private LiveData<List<Image>> liveData;

    //ViewPager helper
    private CustomViewPager mCurrentViewPager;
    private ImageButton mCurrentCloseViewPagerBT;
    private  boolean mIsViewpagerOpened = false;
    private int mLastOffsetScreenPosition;
    //

    private String[] imageUrls = new String[]{
            "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
    };


//    public interface Delegate{
//        void onItemSelected(Image image);
//    }


    private ImagesListFragment.Delegate parent;
    private View mView;



    public AlbumsListFragment() {
        liveData = ImagesModel.instance.getAllImages();

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_albums_list, container, false);
        Objects.requireNonNull(mContext = getContext());
        // Inflate the layout for this fragment
        //liveData = mVM.getData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {//new data notification
                mData = images;
                adapter.notifyDataSetChanged();//refresh the page with new data
            }
        });

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
                Image image = mData.get(position);
                parent.onItemSelected(image);
            }
        });

        SwipeRefreshLayout swipeRefresh = mView.findViewById(R.id.image_list_swipe_refresh);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mVM.refresh(new ImagesModel.CompleteListener() {
                    @Override
                    public void onComplete() {
                        swipeRefresh.setRefreshing(false);//stop the swipe spinner
                    }
                });
            }
        });

        return mView;
    }


    private int getScreenPosition(){return mRecyclerListView.computeVerticalScrollOffset();}

    @Override
    public void onAttach(@NonNull Context context) {//calls once
        super.onAttach(context);
        if(context instanceof ImagesListFragment.Delegate){
            parent = (ImagesListFragment.Delegate)getActivity();
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





      class AlbumRowViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView id;
        CheckBox cb;
        Image image;
        ImageView imageIV;
        CustomViewPager mViewPager;
        ImageButton  mMinimizeViewPagerBTN;


        private AlbumRowViewHolder(@NonNull View itemView, final AlbumsListFragment.OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.row_name_tv);
            id = itemView.findViewById(R.id.row_id_tv);
            cb = itemView.findViewById(R.id.row_cb);
            imageIV = itemView.findViewById(R.id.row_image);
            mViewPager = itemView.findViewById(R.id.viewPager2);
            mMinimizeViewPagerBTN = itemView.findViewById(R.id.minimizeViewPagerBTN);


            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { image.setIsChecked(cb.isChecked());
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
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.i(TAG, "onTouch: " + getAdapterPosition());
                    return false;
                }
            });

        }

        private void bind(Image st, int i) {
//            name.setText(st.getName());
//            id.setText(st.getId());
            cb.setChecked(st.getIsChecked());
            image = st;
//            if (bars.get(position).isWatched()) {
//                holder.thumbnailView.setVisibility(View.GONE);
//            } else {
//                holder.thumbnailView.setVisibility(View.VISIBLE);
//            }

            Log.i(TAG, "inside bind");
            initPagerViewer(mViewPager,mMinimizeViewPagerBTN,i);
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
            Image st = mData.get(i);
            imageRowViewHolder.bind(st, i);


        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }


    private void initPagerViewer(CustomViewPager viewPager, ImageButton closePagerViewBT, int i){


//ViewPager  //imageSlider
//        CustomViewPager viewPager = mView.findViewById(R.id.viewPager);
        viewPager.setPagingEnabled(false);//stop swiping
        viewPager.setPadding(0, 0, 100, 0);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(10);



        ViewPagerAdapter adapter = new ViewPagerAdapter(App.context , imageUrls);
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




