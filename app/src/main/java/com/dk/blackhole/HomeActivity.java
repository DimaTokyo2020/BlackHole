package com.dk.blackhole;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.dk.blackhole.fragments.imagesFrags.ImagesListFragment;
import com.dk.blackhole.fragments.imagesFrags.ImagesListFragmentDirections;
import com.dk.blackhole.model.Image;
import com.dk.blackhole.viewModels.HomeActivityViewModel;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity  implements ImagesListFragment.Delegate, NavigationView.OnNavigationItemSelectedListener {

    NavController mNavCtrl;
    private DrawerLayout mDrawer;
    private HomeActivityViewModel mVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mVM = new ViewModelProvider(this). get(HomeActivityViewModel.class);
        initDrawer();
        mNavCtrl = Navigation.findNavController(this, R.id.home_nav_host);
        //NavigationUI.setupActionBarWithNavController(this, mNavCtrl);//automatic
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
                mNavCtrl.navigate(R.id.action_global_cameraFragment);
                break;
            case R.id.nav_logOut:
                mVM.signOut();
                startMainActivity();
                break;

        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void startMainActivity(){
        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}
