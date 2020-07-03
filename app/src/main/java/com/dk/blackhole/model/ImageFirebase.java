package com.dk.blackhole.model;
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