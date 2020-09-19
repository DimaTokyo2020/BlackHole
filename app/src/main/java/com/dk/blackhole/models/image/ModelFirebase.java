package com.dk.blackhole.models.image;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dk.blackhole.models.album.Album;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ModelFirebase {

    private final String TAG = ModelFirebase.class.getSimpleName();
    private FirebaseFirestore mDB;
    ListenerRegistration mListenerRegistration;



    public ModelFirebase() {

        mDB = FirebaseFirestore.getInstance();

    }

    public void getAllImages(ImagesModel.Listener<List<Image>> listener){




//        listener.onComplete();

    }

    public void testSet() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Los Angeles");
        city.put("state", "CA");
        city.put("country", "USA");
        mDB.collection("cities").document("LA").set(city).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "failed");
            }
        });
    }

    public void testCreate() {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        mDB.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }

    public void updateTest() {
        Map<String, Object> data = new HashMap<>();
        data.put("capital", false);
        mDB.collection("cities").document("BJ")
                .set(data, SetOptions.merge());
    }

    public void dataTypesAdd() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("StringExample", "Hello world!");
        docData.put("booleanExample", true);
        docData.put("numberExample", 3.14159265);
        docData.put("dataExample", new Timestamp(new Date()));
        docData.put("listExample", Arrays.asList(1, 2, 3));
        docData.put("nullExample", null);

        mDB.collection("dataTypes").add(docData);
    }

    public void classAdd() {
        String newUniqueId = getNewUniqueId();
            Image image = new Image(newUniqueId, "dima" , "" + 3,"dima","dima", true ,545.9,true);
            mDB.collection("images").document(newUniqueId).set(image);
    }

    public void updateDocument() {

        DocumentReference washingtonRef =
                mDB.collection("images").document("hSEtM9asEva41qHeoJ1W");
// Set the "isCapital" field of the city 'DC'
        washingtonRef.update("name", "sergay",
                "width", "55").addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "failed: " + e.getMessage());
            }
        });
    }

    public void updateDocumentTimestamp() {

        DocumentReference washingtonRef =
                mDB.collection("images").document("hSEtM9asEva41qHeoJ1W");
// Set the "isCapital" field of the city 'DC'
        washingtonRef.update("timestamp", FieldValue.serverTimestamp()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "failed: " + e.getMessage());
            }
        });


    }

    public void deleteDocument(){
        mDB.collection("cities").document("LA")
                .delete();
    }

    public void getDocuments(){


        DocumentReference docRef = mDB.collection("cities").document("BJ");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }}});

    }

    public void getCustomObject(){

        DocumentReference docRef =
                mDB.collection("images").document("hSEtM9asEva41qHeoJ1W");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                Image image = documentSnapshot.toObject(Image.class);
                Log.i(TAG, "reciaeved: " + image.getName());
            }
        });
    }

    /**
     * Get all images from firebase.
     * @param listener finally push it to the listener.
     */
    public void getMultipleDocuments(ImagesModel.Listener<List<Image>> listener){

//after collection can be add where!
        mDB.collection("images").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                ArrayList<Image> data = new ArrayList<>();
                if(task.isSuccessful()){

                    for(DocumentSnapshot document : task.getResult()){
//                        document.exists();//maybe needed for prevent exceptions
                        data.add(document.toObject(Image.class));
                        Log.i(TAG, document.getId() + " => " + document.getData());
                        }
                }
                else{
                    Log.i(TAG, "Error getting documents: " , task.getException());
                }
                listener.onComplete(data);
            }
        });
    }

    public void listenToMultipleChanges(){

        mDB.collection("images").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(e != null) {
                    Log.e(TAG, "Listen failed; " + e);
                }
                List<String> images = new ArrayList<>();
            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                if(doc.get("name") != null){
                    images.add(doc.getString("name"));
                }
            }
            Log.i(TAG, "Current image: " + images);
                }
        });
    }

    public void listenToMultipleOnlyChanges(){

        mListenerRegistration = mDB.collection("images").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.e(TAG, "Listen failed; " + e);
                }
                List<String> images = new ArrayList<>();
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.i(TAG, "new image: " + dc.getDocument().getData());
                            break;
                        case MODIFIED:
                            Log.i(TAG, "modify image: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            Log.i(TAG, "removed image: " + dc.getDocument().getData());
                            break;
                    }
                }
            }
        });
    }

    public void detachListeners(){
        mListenerRegistration.remove();
    }

    public void createUser(String userName, String userEmail) {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("name", userName);
        user.put("email", userName);
        user.put("userAlbums", new ArrayList<String>());

// Add a new document with a generated ID
        mDB.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void addCollection(String albumId){
        Image newImage = new Image();
        mDB.collection("albums").document(albumId).collection("new Coollection").add(newImage);

    }

    private String getNewUniqueId(){return UUID.randomUUID().toString();}//random

    public void getAllUserAlbums(ImagesModel.Listener<List<Image>> listener){

        //after collection can be add where!
        mDB.collection("albums").whereArrayContains("owners" , "max").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                ArrayList<Image> data = new ArrayList<>();
                if(task.isSuccessful()){

                    for(DocumentSnapshot document : task.getResult()){
//                        document.exists();//maybe needed for prevent exceptions
//                        data.add(document.toObject(Image.class));
                        Log.i(TAG, document.getId() + " => " + document.getData());
                    }
                }
                else{
                    Log.i(TAG, "Error getting documents: " , task.getException());
                }
                listener.onComplete(data);
            }
        });
    }


    //For Test
    public void createFakeAlbums(){
//        String newUniqueId = getNewUniqueId();
        ArrayList<Image> fakeImages = new ArrayList<>();
        ArrayList<Album> fakeAlbums = new ArrayList<>();
        String[] owners ={"max", "sergey" , "dima"};
        Random random = new Random();



        for (int i = 0; i < 2; i++) {
            fakeImages.add(new Image(getNewUniqueId(), "fakeImage-" + (i + 1), "fake-" + (i + 1), "fake-" + (i + 1),
                    "fake-" + (i + 1), true, (i + 1), true));
        }

        for(int j = 0; j < 2; j++) {
            ArrayList<String> owner = new ArrayList<>();
            owner.add(owners[random.nextInt(3)]);
            fakeAlbums.add(new Album(getNewUniqueId(), "fakeAlbum-" + (j+1), owner,
                    "fakeAlbum-" + (j+1),"fakeAlbum-" + (j+1),fakeImages));
        }

        for(int k = 0; k < fakeAlbums.size(); k++){
            mDB.collection("albums").document(fakeAlbums.get(k).getId()).set(fakeAlbums.get(k)).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("dimaa", e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("dimaa", "yeaaa!!");
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("dimaa", "yhthgeaaa!!");
                }
            });}

    }

    public void insertAlbum(){

        Album album = new Album();
//        Image image = new Image();
//        mDB.collection("images").document(newUniqueId).set(image);
        mDB.collection("images").document("gfgfg").set(album).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("dimaa", e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("dimaa", "yeaaa!!");
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("dimaa", "yhthgeaaa!!");
            }
        });}


    public void getCustomObject2(){

        DocumentReference docRef =
                mDB.collection("images").document("hSEtM9asEva41qHeoJ1W");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
//                Image image = documentSnapshot.toObject(Image.class);
                Log.i(TAG, "reciaeved: " + documentSnapshot);
            }
        });
    }

}




