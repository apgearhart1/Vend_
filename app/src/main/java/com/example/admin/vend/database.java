package com.example.admin.vend;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class database {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getLocations() {

        db.collection("locations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("HERE", document.getId() + " => " + document.getString("name")); // document.getData();
                            }
                        } else {
                            Log.d("HERE", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void createVendor(String name, double lon, double lat, String open, String close, String category) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("longitude", lon);
        data.put("latitude", lat);
        data.put("open", open);
        data.put("close", close);
        data.put("category", category);

        Log.d("HERE", "START");

        db.collection("locations")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("HERE", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("HERE", "Error adding document", e);
                    }
                });

        Log.d("HERE", "REALLY????");
    }

    public void createUser(String phoneNumber) {
        Map<String, Object> nums = new HashMap<>();
        nums.put("num1", 0);
        nums.put("num2", 1);

        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();


        System.out.println(12341234);

        for(int i = 1; i <= 8; i++) {
            maps.add(nums);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("parameters", maps);

        db.collection("users")
                .document(phoneNumber)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("HERE", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("HERE", "Error writing document", e);
                    }
                });
    }

}
