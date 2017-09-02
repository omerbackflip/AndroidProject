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

public class registerFrag extends Fragment {
    private static String username ;
    private static final String ID = "id";
    private loginFrag.OnFragmentInteractionListener mListener;



    public registerFrag() {
        // Required empty public constructor
    }

    public static registerFrag newInstance() {
        registerFrag fragment = new registerFrag();
        Bundle args = new Bundle();
        args.putString(ID, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username= getArguments().getString(ID);
        }
    }
    //set the option menu
    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem register = menu.findItem(R.id.main_add);
        menu.findItem(R.id.main_add).setVisible(false);
        menu.findItem(R.id.main_logout).setVisible(false);
        register.setVisible(false);
    }
//create the button listenrs and views
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view =inflater.inflate(R.layout.fragment_regiser, container, false);
        final ProgressBar progressBar= (ProgressBar) view.findViewById(R.id.regtPB);
        progressBar.setVisibility(GONE);

        final EditText username= (EditText)view.findViewById(R.id.userreg); //set the userName
        final EditText pass= (EditText) view.findViewById(R.id.passreg); //set the Password

        final Button reg= (Button)view.findViewById(R.id.regreg);
        final Button bk= (Button)view.findViewById(R.id.backreg);


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg.setEnabled(false);
                bk.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                String user = username.getText().toString();
                String pas = pass.getText().toString();
                boolean a=pas.isEmpty();
                Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9]{6,12}"); //This is how we want out Pattern password to be
                boolean b=PASSWORD_PATTERN.matcher(pas).matches();
                    /*
                   checking input and validate it the email structure and the password Pattern
                    */

                if (!a) {
                    //If the Email address is not in the correct format
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                        progressBar.setVisibility(GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Invalid Email").setTitle("Failed");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }


                    //If the password is not in the correct format
                    if (!b) {
                        progressBar.setVisibility(GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Password Must contain at least 6 charcters,\nletters and degets only!").setTitle("Failed");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    if(!a && b) //if the password is not empty and the password is in the correct format
                    {
                        ModelUser.instace.regUser(user, pas, getActivity(), new ModelUser.regUserCallBack() {
                                    @Override
                                    public void onReg(boolean t) {
                                        progressBar.setVisibility(GONE);
                                        if (t) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage("Register Succefully").setTitle("Success");
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                            mListener.onFragmentInteraction(1);
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage("Email Already Exist").setTitle("Failed");
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                            mListener.onFragmentInteraction(2);
                                        }
                                    }
                                }
                        );

                    }

                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Password Must contain at least 6 charcters,\nletters and degets only!").setTitle("Failed");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    mListener.onFragmentInteraction(2);
                }
            }


        });

        bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(0);
            }
        });


        return view;
    }


    // TODO: Rename method, update argument and hook method into UI eve

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof loginFrag.OnFragmentInteractionListener) {
            mListener = (loginFrag.OnFragmentInteractionListener) context;
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
}
