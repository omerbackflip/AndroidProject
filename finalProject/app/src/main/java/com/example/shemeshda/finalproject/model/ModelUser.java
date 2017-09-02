package com.example.shemeshda.finalproject.model;

import android.app.Activity;

/**
 * Created by Daniel on 24/08/2017.
 */

public class ModelUser {

    public final static ModelUser instace = new ModelUser();

    ModelFirebaseUser  modelUserFB=new ModelFirebaseUser();



    public interface loginUserCallBack{
        void onLogin(boolean t);
    }

    /*
    Login the user to the App with it's details
     */
    public void loginUser(String email, String password, Activity activity, final loginUserCallBack callback){
         modelUserFB.loginUser( email,password,activity,callback);

    }

    public interface regUserCallBack{
        void onReg(boolean t);
    }

    /*
    When the user is firstly registered to the App
     */
    public void regUser( String email, String password,  Activity activity,final regUserCallBack callBack){
      modelUserFB.regUser(email,password, activity,callBack);
    }

    /*
    When the user singing out from the App
     */
    public void signOut(){
        modelUserFB.signOut();

    }

    /*
    Check (true or false) if the user is currently singing in to the app
     */
    public boolean isSignIn()
    {
        return modelUserFB.isSignIn();
    }


    /*
    Get & Set the user's userName
     */
    public String getUsername(){
        return modelUserFB.getUsername();
    }

    public void setUsername(String user){
        modelUserFB.setUsername(user);
    }
}
