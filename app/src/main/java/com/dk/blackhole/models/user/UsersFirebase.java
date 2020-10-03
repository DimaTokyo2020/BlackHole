package com.dk.blackhole.models.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dk.blackhole.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class UsersFirebase {

    private final String TAG = UsersFirebase.class.getSimpleName();
    private final String TABLE_NAME = "Users";

    private FirebaseFirestore mFirebaseFireStore;
    private static UsersFirebase instance;

    private UsersFirebase() { this.mFirebaseFireStore = FirebaseFirestore.getInstance(); }

    public static UsersFirebase getInstance() {
        if(instance == null){ instance = new UsersFirebase();}
        return instance;
    }



    public void getUserByEmail(String userEmail, UserModelHelper.listenerOnComplete listenerOnComplete){
       mFirebaseFireStore.collection(TABLE_NAME).whereEqualTo("email", userEmail).get()
               .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
               if(true == documents.isEmpty()){ listenerOnComplete.onComplete(null); }
               else {
                   for (DocumentSnapshot document : documents) {
                       User userFromFirebase = document.toObject(User.class);
                       Log.i(TAG, "User with email: " + userEmail + " is exist in the Firebase");
                       listenerOnComplete.onComplete(userFromFirebase);
                       break;
                   }
               }
           }
       });
    }


    public void insertNewUser(User user, UserModelHelper.listenerOnComplete<User> listenerOnComplete){

        Map<String, Object> userMap = user.toMap();
        mFirebaseFireStore.collection("Users").document(user.getId()).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listenerOnComplete.onComplete(user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to insert new user to the firebase: " + e);
                listenerOnComplete.onComplete(null);
            }
        });
    }

    public void updateUser(User updatedUser, UserModelHelper.listenerOnComplete<User> listenerOnComplete) {

        DocumentReference refOfUserInFirebase =
                mFirebaseFireStore.collection(TABLE_NAME).document(updatedUser.getId());
        updatedUser.setLastModify(Utils.getCurrentTime());
        refOfUserInFirebase.update(updatedUser.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful() == true) {
                    listenerOnComplete.onComplete(updatedUser);
                }
                else{listenerOnComplete.onComplete(null);}

            }
        });
    }
}
