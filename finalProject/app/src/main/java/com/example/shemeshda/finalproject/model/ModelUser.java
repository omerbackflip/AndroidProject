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

    public void loginUser(String email, String password, Activity activity, final loginUserCallBack callback){
         modelUserFB.loginUser( email,password,activity,callback);

    }

    public interface regUserCallBack{
        void onReg(boolean t);
    }

    public void regUser( String email, String password,  Activity activity,final regUserCallBack callBack){
      modelUserFB.regUser(email,password, activity,callBack);
    }

    public void signOut(){
        modelUserFB.signOut();

    }
    public boolean isSignIn()
    {
        return modelUserFB.isSignIn();
    }

    public String getUsername(){
        return modelUserFB.getUsername();
    }

    public void setUsername(String user){
        modelUserFB.setUsername(user);
    }
}
