package com.dk.blackhole.model;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

public class ModelFirebase {

    private final String TAG = ModelFirebase.class.getSimpleName();
    private FirebaseFirestore mDB;
    ListenerRegistration mListenerRegistration;

    public ModelFirebase() {

        mDB = FirebaseFirestore.getInstance();

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

    public void dataTypes() {
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
        for(int i = 0 ; i < 100; i++ ){
            Image image = new Image("" + (i * 2), "dima" + i, "" + (3*i), true);
            mDB.collection("images").add(image);
        }
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

    public void getMultipleDocuments(){
//after collection can be add where!
        mDB.collection("images").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.i(TAG, document.getId() + " => " + document.getData());
                        }
                    }
                else{
                    Log.i(TAG, "Error getting documents: " , task.getException());
                }
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
}









/*
import android.util.Log;

import androidx.annotation.NonNull;
/
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ImageFirebase {

    final static String IMAGE_COLLECTION = "images";

    public static void getAllImagesSince(long since, final ImageModel.Listener<List<Image>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(since,0);
        db.collection(IMAGE_COLLECTION).whereGreaterThanOrEqualTo("lastUpdated", ts)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Image> stData = null;
                if (task.isSuccessful()){
                    stData = new LinkedList<Image>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Map<String,Object> json = doc.getData();
                        Image image = factory(json);
                        stData.add(image);
                    }
                }
                listener.onComplete(stData);
                Log.d("TAG","refresh " + stData.size());
            }
        });
    }

    public static void getAllImages(final ImageModel.Listener<List<Image>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(IMAGE_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Image> stData = null;
                if (task.isSuccessful()){
                    stData = new LinkedList<Image>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Image image = doc.toObject(Image.class);
                        stData.add(image);
                    }
                }
                listener.onComplete(stData);
            }
        });
    }

    public static void addImage(Image image, final ImageModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(IMAGE_COLLECTION).document(image.getId()).set(toJson(image)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener!=null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    private static Image factory(Map<String, Object> json){
        Image img = new Image();
        img.id = (String)json.get("id");
        img.name = (String)json.get("name");
        img.size = (String)json.get("size");
        img.height = (String)json.get("height");
        img.width = (String)json.get("width");
        img.time_stamp = (String)json.get("time_stamp");
        img.user_uploaded = (String)json.get("user_uploaded");
        img.event = (String)json.get("event");
        img.comments = (String)json.get("comments");
        //st.imgUrl = (String)json.get("imgUrl");
        //st.isChecked = (boolean)json.get("isChecked");
        Timestamp ts = (Timestamp)json.get("lastUpdated");
        if (ts != null) img.lastUpdated = ts.getSeconds();
        return img;
    }

    private static Map<String, Object> toJson(Image img){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", img.id);
        result.put("name", img.name);
        result.put("size", img.size);
        result.put("height", img.height);
        result.put("width", img.width);
        result.put("time_stamp", img.time_stamp);
        result.put("user_uploaded", img.user_uploaded);
        result.put("event", img.event);
        result.put("comments", img.comments);
        //result.put("imgUrl", st.imgUrl);
        //result.put("isChecked", st.isChecked);
        result.put("lastUpdated", FieldValue.serverTimestamp());
        return result;
    }

}
*/