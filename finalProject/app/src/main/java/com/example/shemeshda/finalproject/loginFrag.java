package com.example.shemeshda.finalproject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.shemeshda.finalproject.model.ModelUser;

import java.util.regex.Pattern;

import static android.view.View.GONE;


/**
 * Created by shemeshda on 20/08/2017.
 */

public class loginFrag extends Fragment{
    private static String username ;
    private static final String ID = "id";
    private OnFragmentInteractionListener mListener;


    public loginFrag() {
        // Required empty public constructor
    }

    public static loginFrag newInstance() {
        loginFrag fragment = new loginFrag();
        Bundle args = new Bundle();
        args.putString(ID, username);
        fragment.setArguments(args);
        return fragment;
    }
    /*
    set the option menu
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem register = menu.findItem(R.id.main_add);
        menu.findItem(R.id.main_add).setVisible(false);
        menu.findItem(R.id.main_logout).setVisible(false);
        register.setVisible(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username= getArguments().getString(ID);
        }
    }
    //all the button listeners and the the views
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View view =inflater.inflate(R.layout.fragment_login, container, false);
       final ProgressBar progressBar= (ProgressBar) view.findViewById(R.id.loginPB);
        progressBar.setVisibility(GONE);


       final EditText username= (EditText)view.findViewById(R.id.userlogin);
        final EditText pass= (EditText) view.findViewById(R.id.passlogin);

      final  Button login= (Button)view.findViewById(R.id.loginbtn);
        final Button reg= (Button)view.findViewById(R.id.loginreg);


        if(ModelUser.instace.isSignIn()) {
           mListener.onFragmentInteraction(1); //If the user is singed in, we don't show the login fragment

       }
      else { //If the user is not singed in yet
                 login.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    login.setEnabled(false);
                   reg.setEnabled(false);
                   progressBar.setVisibility(View.VISIBLE);
                   final String user = username.getText().toString();
                   final  String pas = pass.getText().toString();
                   /*
                   checking input and validate it the email structure and the password Pattern
                    */
                   if(!pas.isEmpty())
                {
                  if(!android.util.Patterns.EMAIL_ADDRESS.matcher(user).matches()) //Check if the input correct
                  {
                      progressBar.setVisibility(GONE);
                      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                      builder.setMessage("Invalid Email").setTitle("Failed");
                      AlertDialog dialog = builder.create();
                      dialog.show();
                  }
                   Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9]{6,12}"); //Check if the password is invalid and in the correct format
                   if(!PASSWORD_PATTERN.matcher(pas).matches())
                   {
                       progressBar.setVisibility(GONE);
                       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                       builder.setMessage("Password Must contain at least 6 charcters,\nletters and degets only!").setTitle("Failed");
                       AlertDialog dialog = builder.create();
                       dialog.show();
                   }

                   ModelUser.instace.loginUser(user,pas, getActivity(), new ModelUser.loginUserCallBack() {
                       @Override
                       public void onLogin(boolean t) {
                           progressBar.setVisibility(GONE);
                           if (t) { //Check if there was a success in the log in procsses to the App

                               AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                               builder.setMessage("Loged In Succefully").setTitle("Success");
                               AlertDialog dialog = builder.create();
                               dialog.show();
                               mListener.onFragmentInteraction(1);
                           } else {
                               mListener.onFragmentInteraction(0);
                           }
                       }
                   });

               }
               else {
                       progressBar.setVisibility(GONE);
                       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                       builder.setMessage("Password Must contain at least 6 charcters,\nletters and degets only!").setTitle("Failed");
                       AlertDialog dialog = builder.create();
                       dialog.show();
                       mListener.onFragmentInteraction(0);
                   }
           }
                 });

           reg.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   mListener.onFragmentInteraction(2);
               }
           });

       }

        return view;}


    // TODO: Rename method, update argument and hook method into UI eve

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int i);
    }

}
