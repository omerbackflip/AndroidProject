package com.example.shemeshda.finalproject.model;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.shemeshda.finalproject.finalProject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

import static android.R.attr.delay;
import static java.lang.Thread.sleep;

/**
 * Created by Daniel on 24/08/2017.
 */

public class ModelFirebaseUser
{

    private FirebaseAuth mAuth;
     FirebaseUser user;
     String userName;




    public void loginUser(final String email, String password, Activity act, final ModelUser.loginUserCallBack callback)  {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            userName=user.getEmail();
                            callback.onLogin(true);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(finalProject.getMyContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            callback.onLogin(false);

                        }
                    }
                });

    }


    public void regUser(final String email, String password, Activity activity,final ModelUser.regUserCallBack callback)
    {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    user = mAuth.getCurrentUser();
                    userName=user.getEmail();
                    callback.onReg(true);

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(finalProject.getMyContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    callback.onReg(false);
                }
            }
        });

    }

    public void signOut()
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();
        user=null;
        userName=null;
    }

    public boolean isSignIn()
    {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if( user!=null) {
            userName=user.getEmail();
            return true;
        }
        else
        {
            return false;
        }
    }
public String getUsername()
{

        return userName;
}

    public void setUsername(String user)
    {

        userName=user;
    }

}
