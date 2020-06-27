package com.DK.blackhole;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.DK.blackhole.Fragments.ImagesListFragment;
import com.DK.blackhole.Fragments.ImagesListFragmentDirections;
import com.DK.blackhole.model.Image;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity  implements ImagesListFragment.Delegate, NavigationView.OnNavigationItemSelectedListener {

    NavController navCtrl;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //Drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.syncState();




        navCtrl = Navigation.findNavController(this, R.id.home_nav_host);
        //NavigationUI.setupActionBarWithNavController(this, navCtrl);//automatic


    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START)) {//when we press back its close the drawer if its opened
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void onItemSelected(Image image) {


        //navigation specific
//        ImagesListFragmentDirections.ActionImagesListFragmentToImageDetailsFragment direction =
//                ImagesListFragmentDirections.actionImagesListFragmentToImageDetailsFragment(image);

        NavGraphDirections.ActionGlobalImageDetailsFragment direction =
                ImagesListFragmentDirections.actionGlobalImageDetailsFragment(image);
        navCtrl.navigate(direction);//open the specific details fragment
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            navCtrl.navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_home:
                navCtrl.navigate(R.id.action_global_mainFragment);
                break;
            case R.id.nav_all_images:
                navCtrl.navigate(R.id.action_global_imagesListFragment);
                break;
            case R.id.nav_profile:
                Toast.makeText(this, "not profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_camera:
                navCtrl.navigate(R.id.action_global_cameraFragment);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
