package com.example.shemeshda.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shemeshda.finalproject.model.ModelUser;

public class MainActivity extends Activity implements listFragment.OnFragmentInteractionListener, loginFrag.OnFragmentInteractionListener,addPost.OnFragmentInteractionListener {
    private static Context context;
    FragmentTransaction tran = getFragmentManager().beginTransaction();
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        loginFrag lf = loginFrag.newInstance();
        tran.add(R.id.main_container, lf);


        tran.commit();
        tran.show(lf);
    }
    public static Context getMyContext() {
        return context;
    }

    @Override
    public void onFragmentInteraction(int i) {

        switch (i) {
            case 0:
            {
                loginFrag list = loginFrag.newInstance();
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, list);
                tran.commit();
                break;
            }
            case 1: {
                listFragment list = listFragment.newInstance();
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, list);
                tran.commit();
                break;
            }
            case 2: {
                registerFrag list = registerFrag.newInstance();
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, list);
                tran.commit();
                break;
            }
            case 3: {
                addPost list = addPost.newInstance();
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, list);
                tran.commit();
                break;
            }
            case 4: {
                listFragment list = listFragment.newInstance();
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, list);
                tran.commit();
                break;
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.main_add: {
                onFragmentInteraction(3);
                break;
            }
            case R.id.main_logout: {
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
