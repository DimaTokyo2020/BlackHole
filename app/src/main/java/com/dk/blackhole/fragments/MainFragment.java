package com.dk.blackhole.fragments;


import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.Transliterator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.AnimatorRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.dk.blackhole.App;
import com.dk.blackhole.CustomViewPager;
import com.dk.blackhole.OnSwipeTouchListener;
import com.dk.blackhole.R;
import com.dk.blackhole.fragments.albumsFrags.ViewPagerAdapter;
import com.dk.blackhole.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Objects;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment{


    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private final String TAG = MainFragment.class.getSimpleName();
    private NavController navCtrl;

    private View mView;
    private Bitmap theImage;
    private Activity activity;
    private ImageView newPhotoImageView;

    private String[] imageUrls = new String[]{
            "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
    };



    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        verifyStoragePermissions(getActivity());
        Objects.requireNonNull(activity = getActivity());
        newPhotoImageView = mView.findViewById(R.id.newPhotoImageView);
        navCtrl = Navigation.findNavController(activity, R.id.home_nav_host);



        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            Log.i("TAG", "request");
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }



        (mView.findViewById(R.id.takeImageButton)).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Log.i("TAG", "intent");
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

        });
        (mView.findViewById(R.id.openImageListFragmentBTN)).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Log.i(TAG, "opening ImagesListFragment");
                navCtrl.navigate(R.id.action_global_imagesListFragment);
            }

        });



//ViewPager  //imageSlider
        CustomViewPager viewPager = mView.findViewById(R.id.viewPager);
        viewPager.setPagingEnabled(false);//stop swiping
        viewPager.setPadding(0, 0, 100, 0);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(10);



        ViewPagerAdapter adapter = new ViewPagerAdapter(App.context , imageUrls);
        viewPager.setAdapter(adapter);
        viewPager.setOnTouchListener(new OnSwipeTouchListener(App.context){
            public void onSwipeLeft(){
                Log.i("dima","boom boom");
                ObjectAnimator animation = ObjectAnimator.ofFloat(viewPager, "translationX", -1200f );
                animation.setDuration(500);
                animation.start();
                viewPager.setPagingEnabled(true);//start swiping
                viewPager.setOnTouchListener(null);//stop on touchListener

            }


        });
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


                Log.i("dimaa", "position: " + position);
                Log.i("dimaa", "positionOffset: " + positionOffset);
                Log.i("dimaa", "positionOffsetPixels: " + positionOffsetPixels);
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


        ImageButton imageButton = mView.findViewById(R.id.closeViewPagerBT);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animation = ObjectAnimator.ofFloat(viewPager, "translationX", +0f );
                animation.setDuration(500);
                animation.start();
                imageButton.setVisibility(View.GONE);
                viewPager.setCurrentItem(0,true);
                //stop swipe
                //set listener on  scrool
            }
        });
        return mView;
    }




    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity/
     */
    private static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void copyFile(String inputPath, String inputFile, String outputPath) {

        InputStream in;
        OutputStream out;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            //in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            //out = null;

        } catch (Exception e) { Log.e("tag", Objects.requireNonNull(e.getMessage())); }
    }

    public void copyThat(View view) {

        copyFile("/storage/emulated/0/X/","X.zip","///10.0.0.138/Cruzer Blade-1/");
    }

    private static final int BUFFER_SIZE = 4096;


    public void downloadFromUsb(View view) {



        Thread t = new Thread(){
            public void run(){
                yeah();

                String user = "dima";
                String pass ="dima";

                String sharedFolder="Cruzer Blade-1";
                String path="smb://10.0.0.138/"+sharedFolder+"/x.txt";
                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",user, pass);
                SmbFile smbFile;
                try { smbFile = new SmbFile(path,auth);
                    SmbFileOutputStream smbfos = new SmbFileOutputStream(smbFile);

                    smbfos.write("testing....and writing to a file".getBytes());
                    System.out.println("completed ...nice !");
                } catch (IOException e) { e.printStackTrace(); }

                /*
        //String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
        //String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
        String host = "10.0.0.138";
        String user = "Admin";
        String pass = "Admin";
        String filePath = "/Cruzer Blade-1/x.txt";
        String savePath = "/sdcard/emulated/0/";


        //String ftpUrl = ;
        // ftpUrl = String.format(ftpUrl, user, pass, host, filePath);
        //Log.i("dima","URL: " + ftpUrl);

        try {
            URL url = new URL("ftp://dima:dima@//10.0.0.138/Cruzer%20Blade-1/x.txt");
            Log.i("TAG","1");
            URLConnection conn = url.openConnection();
            Log.i("TAG","2");
            InputStream inputStream = conn.getInputStream();
            Log.i("TAG","3");
            FileOutputStream outputStream = new FileOutputStream(savePath);
            Log.i("TAG","4");
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                Log.i("TAG","5");
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            Log.i("TAG","File downloaded");
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
            }
        };
        t.start();
    }

    private void yeah() {
        SmbFile[] domains;
        try {
            domains = (new SmbFile("smb://")).listFiles();
            for (SmbFile domain : domains) {
                System.out.println(domain);
                SmbFile[] servers = domain.listFiles();
                for (SmbFile server : servers) {
                    Log.i("TAG", "\t" + server);
                }
            }
        } catch (SmbException | MalformedURLException e) { e.printStackTrace(); }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {


            theImage = (Bitmap) Objects.requireNonNull(data.getExtras().get("data"));
            newPhotoImageView.setImageBitmap(theImage);

            //photo=getEncodedString(theImage);
            //setDataToDataBase();
        }


    }
    private String getEncodedString(Bitmap bitmap){

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100, os);

  /* or use below if you want 32 bit images

   bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);*/

        byte[] imageArr = os.toByteArray();

        return Base64.encodeToString(imageArr, Base64.URL_SAFE);

    }





}
