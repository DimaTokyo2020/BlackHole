package com.dk.blackhole.viwes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.dk.blackhole.NavGraphDirections;
import com.dk.blackhole.R;
import com.dk.blackhole.models.album.Album;
import com.dk.blackhole.models.album.AlbumsModelHelper;
import com.dk.blackhole.models.user.User;
import com.dk.blackhole.models.uses_and_albums.UsersAndAlbums;
import com.dk.blackhole.viwes.fragments.CameraFragment;
import com.dk.blackhole.viwes.fragments.imagesFrags.ImagesListFragment;
import com.dk.blackhole.viwes.fragments.imagesFrags.ImagesListFragmentDirections;
import com.dk.blackhole.models.image.Image;
import com.dk.blackhole.viewModels.HomeActivityViewModel;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**This activity manage next fragments:
 *                                     - CameraFragment - Open the camera and takes image.
 *                                     - AlbumsListFragment - Show all users albums.
 *                                     - ImagesListFragment - Show all images of specific album.
 *                                     - MainFragment - Hold shortcut for all fragments.
 *
 * More thinks that this activity does:
 *                                      - Initialize Drawer.
 *
 */

public class HomeActivity extends AppCompatActivity  implements ImagesListFragment.Delegate, NavigationView.OnNavigationItemSelectedListener {


    private final String TAG = HomeActivity.class.getSimpleName();
    public static final String USER_FROM_DB = "USER_FROM_DB";
    NavController mNavCtrl;
    private DrawerLayout mDrawer;
    private HomeActivityViewModel mVM;
    private User mCurrentUser;
    public static int  mScreenWidth;
    public static int  mScreenHeight;
    private MenuItem mUploadIconMenuItem;
    private MenuItem mAddAlbumIconMenuItem;
    private AlbumsModelHelper mAlbumsModelHelper;
    private List<Album> userAlbums = new ArrayList<>();
    private List<UsersAndAlbums> usersAndAlbums = new ArrayList<>();
    private LiveData<List<Album>> userAlbumsLiveData;
    private LiveData<List<UsersAndAlbums>> usersAndAlbumsLiveData;
    private LifecycleOwner mLifecycleOwner;
    private boolean isLiveDataInit = false;




    public interface ClickListener {
        void objectClicked();
    }
    public interface InputFromUserListener {
        void userInputInserted(String inputFromUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mLifecycleOwner = this;
        mAlbumsModelHelper = AlbumsModelHelper.getInstance();
        getUserInfoFromSignInActivity();
        getScreenSizes();
        mVM = new ViewModelProvider(this). get(HomeActivityViewModel.class);
        mVM.setUser(mCurrentUser.getId());
        initDrawer();
        mNavCtrl = Navigation.findNavController(this, R.id.home_nav_host);
        //NavigationUI.setupActionBarWithNavController(this, mNavCtrl);//automatic
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_activity, menu);
        mUploadIconMenuItem = menu.findItem(R.id.uploading);
        mAddAlbumIconMenuItem = menu.findItem(R.id.addAlbum);

        return true;
    }

    private void getUserInfoFromSignInActivity() {
        Serializable userFromSignInActivity = getIntent().getSerializableExtra(USER_FROM_DB);
        if (userFromSignInActivity != null) {
            mCurrentUser = (User) userFromSignInActivity;
        } else {
            Log.e(TAG, "userFromSignInActivity == null");
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.i(TAG,"HomeActivity onWindowFocusChanged");
        super.onWindowFocusChanged(hasFocus);
        if(false == isLiveDataInit){
            isLiveDataInit = true;

            userAlbumsLiveData = getUserAlbums();
            usersAndAlbumsLiveData = getUsersAndAlbums();



            if (userAlbumsLiveData != null) {
                userAlbumsLiveData.observe(this, new Observer<List<Album>>() {
                    @Override
                    public void onChanged(List<Album> albums) {//new data notification
                        userAlbums = albums;
                    }
                });
                usersAndAlbumsLiveData.observe(this, new Observer<List<UsersAndAlbums>>() {
                    @Override
                    public void onChanged(List<UsersAndAlbums> _usersAndAlbums) {
                        if(_usersAndAlbums != null && _usersAndAlbums.size() != 0) {
                            Log.i(TAG, "HomeActivity usersAndAlbumsLiveData onChanged: " + _usersAndAlbums.get(0).getModify());
                        }
                        if(_usersAndAlbums.size() != usersAndAlbums.size()) {
                            usersAndAlbums = _usersAndAlbums;
                            mVM.setUser(mCurrentUser.getId());
                            userAlbumsLiveData = getUserAlbums();
                            userAlbumsLiveData.observe(mLifecycleOwner, new Observer<List<Album>>() {
                                @Override
                                public void onChanged(List<Album> albums) {
                                    userAlbums = albums;
                                }
                            });
                            return;
                        }
                        for(int i = 0 ; i < _usersAndAlbums.size() ;i++){
                            if(true != _usersAndAlbums.get(i).equals(usersAndAlbums.get(i))){
                                usersAndAlbums = _usersAndAlbums;
                                mVM.setUser(mCurrentUser.getId());
                                userAlbumsLiveData = getUserAlbums();
                                userAlbumsLiveData.observe(mLifecycleOwner, new Observer<List<Album>>() {
                                    @Override
                                    public void onChanged(List<Album> albums) {
                                        userAlbums = albums;
                                    }
                                });
                                return;

                            }
                        }

                    }
                });
            }
        }
    }
    /**
     * Here we initialize the drawer with the user info
     */
    private void initDrawer() {
        //Drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.syncState();

        View navHeader = navigationView.getHeaderView(0);
        CircleImageView drawerHeaderCircleImageView = navHeader.findViewById(R.id.drawer_header_imageView);
        TextView userName = navHeader.findViewById(R.id.drawer_userName);
        TextView userEmail = navHeader.findViewById(R.id.drawer_userEmail);
        userName.setText(mVM.getUserDisplayName());
        userEmail.setText(mVM.getUserEmail());
        Picasso.get().load(mVM.getUserPhotoUrl()).fit().into(drawerHeaderCircleImageView);
    }


    public String[] getUsersAlbumsIdes(){
        String[] albumsIdes = new String[usersAndAlbums.size()];
        for(int i = 0; i < albumsIdes.length; i++){
            albumsIdes[i] = usersAndAlbums.get(i).getAlbumId();
        }
        return albumsIdes;
    }

    /**
     * Here we handle back press in case the drawer is open so we just want to close it and not to close the activity.
     */
    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START)) {//when we press back its close the drawer if its opened
            mDrawer.closeDrawer(GravityCompat.START);
        }
        else{ super.onBackPressed(); }
    }

    /**
     * When the user press specific image.
     * @param image
     */
    @Override
    public void onItemSelected(Image image) {
        NavGraphDirections.ActionGlobalImageDetailsFragment direction =
                ImagesListFragmentDirections.actionGlobalImageDetailsFragment(image);
        mNavCtrl.navigate(direction);//open the specific details fragment
    }

//    /**
//     * Here we handle all the drawer action.
//     * @param item
//     * @return
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home){
//            mNavCtrl.navigateUp();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    public LiveData<List<Album>> getUserAlbums() {
        return mVM.getUserAlbums();
    }

    public LiveData<List<UsersAndAlbums>> getUsersAndAlbums() {
        return mVM.getUsersAndAlbums();
    }

    public String getUserID(){return mCurrentUser.getId();}


    public void startUploadAnimation(){
        mUploadIconMenuItem.setIcon(R.drawable.animation_upload);
        ((AnimationDrawable)mUploadIconMenuItem.getIcon()).start();

    }



    public void stopUploadAnimation(){
        ((AnimationDrawable)mUploadIconMenuItem.getIcon()).stop();
        mUploadIconMenuItem.setIcon(null);
    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_home:
                mNavCtrl.navigate(R.id.action_global_mainFragment);
                break;
            case R.id.nav_all_images:
                mNavCtrl.navigate(R.id.action_global_imagesListFragment);
                break;
            case R.id.nav_profile:
                Toast.makeText(this, "not profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_camera:
                Bundle bundle = new Bundle();
                bundle.putSerializable(CameraFragment.USER_INFO, mCurrentUser);
                mNavCtrl.navigate(R.id.action_global_cameraFragment, bundle);
                break;
            case R.id.nav_logOut:
//                mVM.signOut();
                requestToSignOutTheSignInActivity();
                break;

        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void requestToSignOutTheSignInActivity(){
        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
        intent.putExtra(SignInActivity.INTENT_ACCOUNT_REQUESTS, SignInActivity.SIGN_OUT_REQUEST);
        startActivity(intent);
        finish();
    }


    public void likeButtonPressed(String albumId, boolean isChecked) {
        mVM.increaseOrDecreaseLikeForAlbum(albumId, isChecked);
    }




    /**
     * Give the runtime device screen size.
     * Used for adapting he UI for specific screens.
     */
    private void getScreenSizes() {
        Log.i(TAG, "Inside getScreenSizes");
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;
        Log.i(TAG, "Screen sizes: Width: " + mScreenWidth + " Height: " + mScreenHeight);
    }

    public void addOptionToCreateNewAlbum(ClickListener clickListener){
        mAddAlbumIconMenuItem.setIcon(R.drawable.ic_add_new);
        mAddAlbumIconMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                clickListener.objectClicked();
                return false;
            }
        });
    }

    public void closeOptionToCreateNewAlbum(){
        mAddAlbumIconMenuItem.setIcon(null);
        mAddAlbumIconMenuItem.setOnMenuItemClickListener(null);
    }

    public void  popAlumsChoose(InputFromUserListener inputFromUserListener){

        String[] albumsChoose = new String[userAlbums.size() + 1];//first is new album
        albumsChoose[0] = "New albums";
        for(int i = 1; i< albumsChoose.length; i++) {
            albumsChoose[i] = userAlbums.get(i-1).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick album name");
        builder.setItems(albumsChoose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which == 0 ) {
                    popWindowForSettingAlbumName(inputFromUserListener);
                }
                else{
                    inputFromUserListener.userInputInserted(userAlbums.get(which-1).getId());
                }


            }
        });
        builder.show();
    }

    void popWindowForSettingAlbumName(InputFromUserListener inputFromUserListener){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("New album");
        alert.setMessage("Please insert albums name");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String albumName = input.getText().toString();
                if(true == albumName.isEmpty()){ albumName = "Album without name"; }
                String userId = mCurrentUser.getId();
                Album newAlbum = new Album(albumName, userId);
                mAlbumsModelHelper.addNewAlbumToDb(newAlbum);
                inputFromUserListener.userInputInserted(newAlbum.getId());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }


}
