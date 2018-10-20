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
import java.util.HashMap;
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

    public void createVendor() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Vendor3");
        data.put("longitude", 5500);
        data.put("latitude", 6400);

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
        Map<String, Object> data = new HashMap<>();
        data.put("num1", 1234);
        data.put("num2", 4321);

        System.out.println(12341234);

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
