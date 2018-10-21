package com.example.admin.vend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText email = (EditText)findViewById(R.id.emailTxt1);
        final EditText password = (EditText)findViewById(R.id.passwordTxt1);
        final EditText phoneNumber = (EditText)findViewById(R.id.phoneNumberTxt1);

        Button signup = (Button)findViewById(R.id.signUpBtn);
        Switch vendorSwitch = (Switch)findViewById(R.id.switch1);

        final int[] isVendor = {0};

        vendorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isVendor[0] = 1;
                } else {
                    isVendor[0] = 0;
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseAuth mAuth = FirebaseAuth.getInstance();

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("HERE", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates;

                                    if(isVendor[0] == 1) {
                                        profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName("vendor")
                                                .build();
                                    } else {
                                        profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName("user" + phoneNumber.getText().toString())
                                                .build();
                                        database data = new database();
                                        data.createUser(phoneNumber.getText().toString());
                                    }

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("HERE", "User profile updated.");
                                                    }
                                                }
                                            });



                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("HERE", "createUserWithEmail:failure", task.getException());
                                }
                            }
                        });

            }
        });



    }

}
