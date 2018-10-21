package com.example.admin.vend;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sekretz {
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static String phoneNumber;
    public static List<Map<String, Double>> model;
    private static double lat = 39.1836;
    private static double lng = -96.5717;

    public Sekretz(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        getModel();
    }

    private void getModel() {
        firestore.collection("users").document(phoneNumber).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()) {
                    model = (List<Map<String, Double>>)task.getResult().getData().get("parameters");
                }
            }
        });
    }

    public static double predict(double lat, double lng, int category) {
        double distance = -(latLngDistToMiles(lat, lng, Sekretz.lat, Sekretz.lng) - 10)/20;
        Map<String, Double> model = Sekretz.model.get(category-1);
        double num1 = model.get("num1");
        double num2 = model.get("num2");

        double prediction = 1/(1+Math.pow(Math.E, -num1 - num2 * distance));
        return prediction;
    }

    public static void putModel() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("parameters", model);
        firestore.collection("users").document(phoneNumber).set(data);
    }

    private static void gradientDescent(double lat, double lng, int category, int y) {
        double p = predict(lat, lng, category);
        double j = Math.log(p) * y - Math.log(1-p) * (1-y);
        double gradient1 = p-y;
        double gradient2 = latLngDistToMiles(lat, lng, Sekretz.lat, Sekretz.lng)*(p-y);
        model.get(category-1).put("num1", model.get(category-1).get("num1") - gradient1*0.2);
        model.get(category-1).put("num2", model.get(category-1).get("num2") - gradient2*0.2);
    }

    public static void train(double lat, double lng, int category, int y) {
        System.out.println("PhoneNumber: " + phoneNumber);
        for(int i = 0; i < 5; i++) {
            gradientDescent(lat, lng, category, y);
        }

        putModel();
    }

    public static double latLngDistToMiles(double myLat, double myLng) {
        return latLngDistToMiles(Sekretz.lat, Sekretz.lng, myLat, myLng);
    }

    private static double latLngDistToMiles(double lat, double lng, double myLat, double myLng) {
        double milesPerKm = 0.621371;
        double R = 6371 * Math.pow(10, 3);
        double a1 = myLat * Math.PI / 180;
        double a2 = lat * Math.PI / 180;
        double dphi = (lat-myLat) * Math.PI/180;
        double dlambda = (lng-myLng) * Math.PI/180;

        double a = Math.sin(dphi/2) * Math.sin(dphi/2) +
                Math.cos(a1) * Math.cos(a2) *
                Math.sin(dlambda/2) * Math.sin(dlambda/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c * milesPerKm;
    }
}
