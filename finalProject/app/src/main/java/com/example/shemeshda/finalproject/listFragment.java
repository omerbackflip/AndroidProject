package com.example.shemeshda.finalproject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shemeshda.finalproject.model.ModelRowView;
import com.example.shemeshda.finalproject.model.ModelUser;
import com.example.shemeshda.finalproject.model.RowVew;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static android.view.View.GONE;

/**
 * Created by shemeshda on 20/08/2017.
 */

public class listFragment extends Fragment {
    ListView list;
    List<RowVew> data; //List of the posts that the user's has uploaded

    StudentsListAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public listFragment() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ModelRowView.UpdatepostsEvent event) {
       boolean exist = false;
        adapter.notifyDataSetChanged();
       for (int i=0;i<data.size();i++) {
           if ((data.get(i).id == event.rb.id)) {
               exist = true;
               if ((!event.rb.isDeleted))
                   data.set(i, event.rb);
               else
                   data.remove(i);
               break;

           }
       }
        if (!exist&&(!event.rb.isDeleted)){
            data.add(event.rb);
        }
        list.setSelection(adapter.getCount() - 1);
        adapter.notifyDataSetChanged();

    }

    public static listFragment newInstance() {
        listFragment fragment = new listFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //set the option menu
    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem register = menu.findItem(R.id.main_add);
        menu.findItem(R.id.main_add).setVisible(true);
        menu.findItem(R.id.main_logout).setVisible(true);
        register.setVisible(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_image_list, container, false);

        data = ModelRowView.instace.getAllrows();

        list = (ListView) view.findViewById(R.id.imagelist);
        adapter = new StudentsListAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onFragmentInteraction2(data.get(position).user);
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        EventBus.getDefault().register(this);

        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int id);
        void onFragmentInteraction2(String id);
    }
    class StudentsListAdapter extends BaseAdapter {
        LayoutInflater inflater = getActivity().getLayoutInflater();


        /*
        Get count of all of the posts
         */
        @Override
        public int getCount() {
           return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }


        /*
        set and configure the list row view and functionality
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.fragment_list_row, null);
            }

            TextView text  = (TextView) convertView.findViewById(R.id.textImage);
            TextView user  = (TextView) convertView.findViewById(R.id.imageuser);
            final ImageView edit=(ImageView) convertView.findViewById(R.id.edidBTN);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.rowImage);
            edit.setVisibility(GONE);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.getImagrPB);
            final RowVew rv = data.get(position);

            if(rv.user.equals(ModelUser.instace.getUsername()))
            {
                edit.setVisibility(View.VISIBLE);
            }

            text.setText(rv.text);
            user.setText(rv.user);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.avatar, null);
            imageView.setImageDrawable(drawable);
            imageView.setTag(rv.imageUrl);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=getActivity().getIntent();
                    intent.putExtra("RID",rv.id);
                    mListener.onFragmentInteraction(5);
                }
            });

            if (rv.imageUrl != null && !rv.imageUrl.isEmpty() && !rv.imageUrl.equals("")){
                progressBar.setVisibility(View.VISIBLE);
                ModelRowView.instace.getImage(rv.imageUrl, new ModelRowView.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        String tagUrl = imageView.getTag().toString();
                        if (tagUrl.equals(rv.imageUrl)) {
                            imageView.setImageBitmap(image);
                            progressBar.setVisibility(GONE);
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
            else
                imageView.setVisibility(GONE);

            return convertView;
        }
    }


    }

