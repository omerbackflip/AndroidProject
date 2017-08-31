package com.example.shemeshda.finalproject;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;



import java.util.List;

import android.app.Fragment;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shemeshda.finalproject.model.ModelRowView;
import com.example.shemeshda.finalproject.model.ModelUser;
import com.example.shemeshda.finalproject.model.RowVew;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by shemeshda on 20/08/2017.
 */

public class listFragment extends Fragment {
    ListView list;
    List<RowVew> data;

    StudentsListAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public listFragment() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ModelRowView.UpdateStudentEvent event) {
       boolean exist = false;
       for (RowVew r: data){
           if (r.id==event.rb.id){
              exist = true;
               break;
           }
        }
        if (!exist){
            data.add(event.rb);
        }
        adapter.notifyDataSetChanged();
        list.setSelection(adapter.getCount() - 1);

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
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        void onFragmentInteraction(int id);
        void onFragmentInteraction2(String id);
    }
    class StudentsListAdapter extends BaseAdapter {
        LayoutInflater inflater = getActivity().getLayoutInflater();////check

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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.fragment_list_row, null);
            }
            TextView text  = (TextView) convertView.findViewById(R.id.textImage);
            TextView user  = (TextView) convertView.findViewById(R.id.imageuser);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.rowImage);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.getImagrPB);
           final RowVew rv = data.get(position);
            text.setText(rv.text);
            user.setText(rv.user);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.avatar, null);
            imageView.setImageDrawable(drawable);

            imageView.setTag(rv.imageUrl);

            if (rv.imageUrl != null && !rv.imageUrl.isEmpty() && !rv.imageUrl.equals("")){
                progressBar.setVisibility(View.VISIBLE);
                ModelRowView.instace.getImage(rv.imageUrl, new ModelRowView.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        String tagUrl = imageView.getTag().toString();
                        if (tagUrl.equals(rv.imageUrl)) {
                            imageView.setImageBitmap(image);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
            return convertView;
        }
    }


    }

