package com.example.shemeshda.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shemeshda.finalproject.model.ModelFirebaseUser;
import com.example.shemeshda.finalproject.model.ModelRowView;
import com.example.shemeshda.finalproject.model.ModelUser;
import com.example.shemeshda.finalproject.model.RowVew;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link addPost.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link addPost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addPost extends Fragment {
    // private static String username ;
    private static final String ID = "id";
    private addPost.OnFragmentInteractionListener mListener;


    public addPost() {
        // Required empty public constructor
    }

    public static addPost newInstance() {
        addPost fragment = new addPost();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view =inflater.inflate(R.layout.fragment_add_row, container, false);


        final TextView text= (TextView)view.findViewById(R.id.posttext);
        ImageButton img= (ImageButton) view.findViewById(R.id.postimage);

        Button post= (Button)view.findViewById(R.id.addpost);
        Button cancel= (Button)view.findViewById(R.id.cancelpost);


        post.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                RowVew rw = new RowVew();
                rw.text=text.getText().toString();
                rw.user= ModelUser.instace.getUsername();
                rw.imageUrl="";
                ModelRowView.instace.addRow(rw);


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Posted!").setTitle("Post");
                AlertDialog dialog = builder.create();
                dialog.show();
                mListener.onFragmentInteraction(4);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(1);
            }
        });


        return view;
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem register = menu.findItem(R.id.main_add);
        menu.findItem(R.id.main_add).setVisible(false);
        menu.findItem(R.id.main_logout).setVisible(true);
        register.setVisible(true);
    }

    // TODO: Rename method, update argument and hook method into UI eve

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof loginFrag.OnFragmentInteractionListener) {
            mListener = (addPost.OnFragmentInteractionListener) context;
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

