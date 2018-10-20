package com.example.admin.vend;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class database {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getLocations() {
        Log.d("HERE", "TOp getting documents: ");


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

    public void createVendor() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Vendor3");
        data.put("longitude", 5500);
        data.put("latitude", 6400);

        db.collection("locations")
                .add(data);
    }

    public void createUser(String phoneNumber) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Vendor3");
        data.put("longitude", 5500);
        data.put("latitude", 6400);

        db.collection("users")
                .document(phoneNumber)
                .set(data);
    }

}
