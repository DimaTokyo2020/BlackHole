package com.dk.blackhole.models.image;

import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.dk.blackhole.models.image.ImagesModelHelper.listenerUpload;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class ImagesFirebaseStorage {

    private final String TAG = ImagesFirebaseStorage.class.getSimpleName();
    private final String FOLDER = "black_hole_images/";
    private FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
    static ImagesFirebaseStorage instance;


    private ImagesFirebaseStorage() { }

    public static ImagesFirebaseStorage getInstance(){
        if(instance == null){instance = new ImagesFirebaseStorage();}
        return instance;
    }


    public void uploadImageToFirebase(byte[] data, Image imageInfo, listenerUpload listenerUpload){

        String path = FOLDER + imageInfo.getId() + ".png";

        StorageReference fireBlackHoleRef = mFirebaseStorage.getReference(path);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("caption", imageInfo.getName())
                .build();

        UploadTask uploadTask = fireBlackHoleRef.putBytes(data, metadata);
        listenerUpload.started();

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Log.i(TAG, "Upload task complite!");
                listenerUpload.onCompleteUpload();
            }
        });

        Task<Uri> getDownloadUriTask = uploadTask.continueWithTask(
                new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw  task.getException();
                        }
                        return  fireBlackHoleRef.getDownloadUrl();
                    }
                }
        );
        getDownloadUriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    Log.i(TAG, "The URI: " + downloadUri);
                    listenerUpload.finished(downloadUri.toString(), path);
                }
                else {
                    listenerUpload.finished("", "");
                }

            }
        });
    }
}
