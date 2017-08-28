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

import com.example.shemeshda.finalproject.model.ModelUser;

import java.util.regex.Pattern;

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
    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem register = menu.findItem(R.id.main_add);
        menu.findItem(R.id.main_add).setVisible(false);
        menu.findItem(R.id.main_logout).setVisible(false);
        register.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view =inflater.inflate(R.layout.fragment_regiser, container, false);


       final EditText username= (EditText)view.findViewById(R.id.userreg);
        final EditText pass= (EditText) view.findViewById(R.id.passreg);

        Button reg= (Button)view.findViewById(R.id.regreg);
        Button bk= (Button)view.findViewById(R.id.backreg);


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String user = username.getText().toString();
                String pas = pass.getText().toString();
                boolean a=pas.isEmpty();
                Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9]{6,12}");
                boolean b=PASSWORD_PATTERN.matcher(pas).matches();
                if (!a) {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user).matches()) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Invalid Email").setTitle("Failed");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }



                    if (!b) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Password Must contain at least 6 charcters,\nletters and degets only!").setTitle("Failed");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    if(!a && b)
                    {
                        ModelUser.instace.regUser(user, pas, getActivity(), new ModelUser.regUserCallBack() {
                                    @Override
                                    public void onReg(boolean t) {
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
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
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
