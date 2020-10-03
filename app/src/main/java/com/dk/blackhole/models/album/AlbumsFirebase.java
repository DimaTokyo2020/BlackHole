package com.dk.blackhole.models.album;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlbumsFirebase {

    private final String TAG = AlbumsFirebase.class.getSimpleName();
    private final String COLLECTION_PATH = "Albums";

    private FirebaseFirestore mFirebaseFireStore;
    private static AlbumsFirebase instance;

    private AlbumsFirebase() { this.mFirebaseFireStore = FirebaseFirestore.getInstance(); }

    public static AlbumsFirebase getInstance() {
        if(instance == null){ instance = new AlbumsFirebase();}
        return instance;
    }

    public void addNewAlbum(Album newAlbum, AlbumsModelHelper.ListenerOnComplete<Boolean> listenerOnComplete) {
        mFirebaseFireStore.collection(COLLECTION_PATH).document(newAlbum.getId()).set(newAlbum)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listenerOnComplete.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listenerOnComplete.onComplete(false);
                        Log.e(TAG, "Failed to upload image");
                    }
                });
    }


    public void getUserAlbums(String userId, AlbumsModelHelper.ListenerOnComplete<List<Album>> listenerOnComplete) {

        mFirebaseFireStore.collection(COLLECTION_PATH).whereEqualTo("owner", userId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        ArrayList<Album> data = new ArrayList<>();
                        if (task.isSuccessful()) {

                            for (DocumentSnapshot document : task.getResult()) {
//                        document.exists();//maybe needed for prevent exceptions
                                data.add(document.toObject(Album.class));
                                Log.i(TAG, document.getId() + " => " + document.getData());
                            }
                            listenerOnComplete.onComplete(data);
                        } else {
                            Log.i(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    public void updateAlbumImagesNumber(String albumId, Integer imagesNumber, String timeOfUpdate, AlbumsModelHelper.ListenerOnComplete<Boolean> booleanListenerOnComplete) {
        DocumentReference albumRef = mFirebaseFireStore.collection(COLLECTION_PATH).document(albumId);


        albumRef.update("imagesNumber", imagesNumber, "lastModify", timeOfUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        booleanListenerOnComplete.onComplete(true);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        booleanListenerOnComplete.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                booleanListenerOnComplete.onComplete(false);
                Log.i(TAG, "failed: " + e.getMessage());
            }
        });
    }

    public void getAlbum(String albumId, AlbumsModelHelper.ListenerOnComplete<Album> booleanListenerOnComplete) {
        mFirebaseFireStore.collection(COLLECTION_PATH).whereEqualTo("id", albumId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        ArrayList<Album> data = new ArrayList<>();
                        if (task.isSuccessful()) {

                            for (DocumentSnapshot document : task.getResult()) {
//                        document.exists();//maybe needed for prevent exceptions
                                data.add(document.toObject(Album.class));
                                Log.i(TAG, document.getId() + " => " + document.getData());
                            }
                            booleanListenerOnComplete.onComplete(data.get(0));
                        } else {
                            Log.i(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void updateAlbumLikesNumber(String albumId, Integer likesNumber, String timeOfUpdate, AlbumsModelHelper.ListenerOnComplete<Boolean> booleanListenerOnComplete) {
        DocumentReference albumRef = mFirebaseFireStore.collection(COLLECTION_PATH).document(albumId);


        albumRef.update("likesNumber", likesNumber, "lastModify", timeOfUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        booleanListenerOnComplete.onComplete(true);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        booleanListenerOnComplete.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        booleanListenerOnComplete.onComplete(false);
                        Log.i(TAG, "failed: " + e.getMessage());
                    }
                });
    }

    void getAlbumsByIdes(String[] albumsIdes, AlbumsModelHelper.ListenerOnComplete<List<Album>> listenerOnComplete){
        /* We cant use "whereIn" with more then 10 elements so we separate the requests */
        List<String> albumsIdesArray = Arrays.asList(albumsIdes);
        int numberOfRequestNeeded = albumsIdes.length / 10;

        for(int i = 0; i <= numberOfRequestNeeded; i++) {
            List<String> subArray;
            if(i != numberOfRequestNeeded) {
                subArray = albumsIdesArray.subList(0 + (10 * i), 10 + (10 * i));
            }
            else{
                subArray = albumsIdesArray.subList(0 + (10 * i), albumsIdes.length);
            }
            Log.i(TAG, "Send request AlbumsFirebase.getAlbumsByIdes()");
            if (subArray.size() != 0 && false == subArray.get(0).equals("")) {
                mFirebaseFireStore.collection(COLLECTION_PATH).whereIn("id", subArray).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                Log.i(TAG, "Remote DB answered AlbumsFirebase.getAlbumsByIdes()");
                                ArrayList<Album> data = new ArrayList<>();
                                if (task.isSuccessful()) {

                                    for (DocumentSnapshot document : task.getResult()) {
//                        document.exists();//maybe needed for prevent exceptions
                                        data.add(document.toObject(Album.class));
                                        Log.i(TAG, document.getId() + " => " + document.getData());
                                    }
                                    listenerOnComplete.onComplete(data);
                                } else {
                                    Log.i(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        }
    }
}
