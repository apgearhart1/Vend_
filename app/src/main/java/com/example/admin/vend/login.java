package com.example.admin.vend;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.admin.vend.database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class login extends AppCompatActivity {

    private Notifications Notifications = new Notifications(this);
    private Sekretz sekretz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        createNotificationChannel();

        final EditText email = (EditText)findViewById(R.id.emailTxt);
        final EditText password = (EditText)findViewById(R.id.passwordTxt);

        final TextView hw = (TextView)findViewById(R.id.sample_text);

        Button login = (Button)findViewById(R.id.loginBtn);
        Button signup = (Button)findViewById(R.id.signUpBtn);
        Button maps = (Button)findViewById(R.id.map);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseAuth mAuth = FirebaseAuth.getInstance();

                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("HERE", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user.getDisplayName().contains("user")) {
                                        sekretz = new Sekretz(user.getDisplayName().substring(4));
                                        startActivity(new Intent(login.this, MapsActivity.class));
                                    } else {
                                        startActivity(new Intent(login.this, addSpot.class));
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("HERE", "signInWithEmail:failure", task.getException());
                                }

                            }
                        });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(login.this, signup.class));
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
        Toast.makeText(getApplicationContext(), "onStop called", Toast.LENGTH_LONG).show();

        final boolean firstTime[] = {true};
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference colRef = db.collection("locations");
        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("HERE", "Listen failed.", e);
                    return;
                }
                if(firstTime[0]) {
                    firstTime[0] = false;
                } else if(sekretz.model != null){
                    sendAlerts(documentSnapshots);
                }
            }
        });

    }

    private void sendAlerts(QuerySnapshot documentSnapshots) {
        for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
            if(dc.getType() == DocumentChange.Type.ADDED) {
                Map<String, Object> data = dc.getDocument().getData();
                String name = (String) data.get("name");

                if(name.equals("")) {
                    return;
                }

                int category = Integer.parseInt((String) data.get("category"));
                double latitude, longitude;

                if(data.get("latitude") instanceof Long) {
                    latitude = ((Long) data.get("latitude")).doubleValue();
                }
                else {
                    latitude = (double) data.get("latitude");
                }
                if(data.get("longitude") instanceof Long) {
                    longitude = ((Long) data.get("longitude")).doubleValue();
                }
                else {
                    longitude = (double) data.get("longitude");
                }

                if(sekretz.predict(latitude, longitude, category) > 0.5) {
                    sendNotification(name, latitude, longitude, category);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public void sendNotification (String name, double lat, double lng, int category) {
        Notifications.sendNotification(name, lat, lng, category);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            System.out.println("test");
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
