package com.example.shemeshda.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shemeshda.finalproject.model.ModelUser;

public class MainActivity extends Activity implements listFragment.OnFragmentInteractionListener, loginFrag.OnFragmentInteractionListener {
    private static Context context;
    FragmentTransaction tran = getFragmentManager().beginTransaction();
    static final int REQUEST_WRITE_STORAGE = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        //We are asking for permision to read and writ to the user local storage
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }

        setContentView(R.layout.activity_main);
        loginFrag lf = loginFrag.newInstance();
        tran.add(R.id.main_container, lf);


        tran.commit();
        tran.show(lf);
    }
    public static Context getMyContext() {

        return context;
    }

    //Listens to the fragment transactions and switched fragments and activities
    @Override
    public void onFragmentInteraction(int i) {

        switch (i) {
            case 0: //Login fragment
            {
                loginFrag list = loginFrag.newInstance();
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, list);
                tran.commit();
                break;
            }
            case 1: { //List fragment - the list of all the photos and discriptions of the users
                Log.d("gold","first check");
                listFragment list = listFragment.newInstance();
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, list);
                tran.commit();
                break;
            }
            case 2: { //Register fragment
                registerFrag list = registerFrag.newInstance();
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, list);
                tran.commit();
                break;
            }
            case 3: { //Add post fragment
                Intent myIntent = new Intent(this, AddPostActivity.class);
                startActivity(myIntent);
                finish();
                break;
            }
            case 4: {
                listFragment list = listFragment.newInstance();
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, list);
                tran.commit();
                break;
            }
            case 5:
            {
                Intent myIntent = new Intent(this, EditRowActivity.class); //Edit post fragments
                int k=getIntent().getIntExtra("RID",0);
                myIntent.putExtra("RID2",k);
                startActivity(myIntent);
                finish();
                break;
            }
        }


    }

    //The listener who listens menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.main_add: { //If the user wants to add new post
                Intent myIntent = new Intent(this, AddPostActivity.class);
                startActivity(myIntent);
                finish();
                break;
            }
            case R.id.main_logout: { //if the user wants to logout from his account
                ModelUser.instace.signOut();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Hope To See You Again! ").setTitle("Loged-Out");
                AlertDialog dialog = builder.create();
                dialog.show();

                onFragmentInteraction(0);
                break;
            }
            case android.R.id.home:
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public void onFragmentInteraction2(String id) {

    }

}
