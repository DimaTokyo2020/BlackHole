package com.dk.blackhole.models.uses_and_albums;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dk.blackhole.models.album.Album;
import com.dk.blackhole.models.image.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersAndAlbumsFirebase {

    private final String TAG = UsersAndAlbumsFirebase.class.getSimpleName();
    private final String COLLECTION_PATH = "UsersAndAlbums";
    private static UsersAndAlbumsFirebase instance;
    private static FirebaseFirestore mFirebaseFireStore;


    private UsersAndAlbumsFirebase() { }

    public static UsersAndAlbumsFirebase getInstance(){
        if(instance == null){
            instance = new UsersAndAlbumsFirebase();
            mFirebaseFireStore = FirebaseFirestore.getInstance();
        }

        return instance;
    }

    public void getAlbumsByUserId(String userId, UsersAndAlbumsModelHelper.ListenerOnComplete<List<UsersAndAlbums>> listListenerOnComplete) {
        mFirebaseFireStore.collection(COLLECTION_PATH).whereEqualTo("userId", userId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<UsersAndAlbums> data = new ArrayList<>();
                        if (task.isSuccessful()) {

                            for (DocumentSnapshot document : task.getResult()) {
//                        document.exists();//maybe needed for prevent exceptions
                                data.add(document.toObject(UsersAndAlbums.class));
                                Log.i(TAG, document.getId() + " => " + document.getData());
                            }
                            listListenerOnComplete.onComplete(data);
                        } else {
                            Log.i(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void addNewUsersAndAlbumsEntity(UsersAndAlbums usersAndAlbums, UsersAndAlbumsModelHelper.ListenerOnComplete<Boolean> booleanListenerOnComplete) {
        mFirebaseFireStore.collection(COLLECTION_PATH).document(usersAndAlbums.getId()).set(usersAndAlbums)
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
                        Log.e(TAG, "Failed to upload image");
                    }
                });
    }


    public void listenToMultipleOnlyChanges(String userId, UsersAndAlbumsModelHelper.ListenerOnComplete<UsersAndAlbums> listenerOnComplete){

        mFirebaseFireStore.collection(COLLECTION_PATH).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.e(TAG, "Listen failed; " + e);
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    UsersAndAlbums usersAndAlbums = dc.getDocument().toObject(UsersAndAlbums.class);
                    /* Only update for current user! */
                    if(usersAndAlbums.getUserId().equals(userId)){
                        switch (dc.getType()) {
                            case ADDED:
                                listenerOnComplete.onComplete(usersAndAlbums);
                                Log.i(TAG, "New UsersAndAlbums entity: " + dc.getDocument().getData());
                                break;
                            case MODIFIED:
                                listenerOnComplete.onComplete(usersAndAlbums);
                                Log.i(TAG, "Modified UsersAndAlbums entity: " + dc.getDocument().getData());
                                break;
                            case REMOVED:
                                listenerOnComplete.onComplete(usersAndAlbums);
                                Log.i(TAG, "removed UsersAndAlbums entity " + dc.getDocument().getData());
                                break;
                        }
                    }
                }
            }
        });
    }

}
