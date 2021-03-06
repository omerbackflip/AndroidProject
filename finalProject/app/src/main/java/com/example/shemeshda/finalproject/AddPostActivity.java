package com.example.shemeshda.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shemeshda.finalproject.model.ModelRowView;
import com.example.shemeshda.finalproject.model.ModelUser;
import com.example.shemeshda.finalproject.model.RowVew;


import static android.view.View.GONE;

public class AddPostActivity extends Activity {

    // private static String username ;
    private static final String ID = "id";
    Bitmap imageBitmap;
    ProgressBar progressBar;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESAULT_SUCCESS = 0;
    final static int RESAULT_FAIL = 1;




    public AddPostActivity() {
        // Required empty public constructor
    }


    //all the button listeners and the the views
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        final TextView text= (TextView)findViewById(R.id.posttext);

        final Button post= (Button)findViewById(R.id.addpost);
       final Button cancel= (Button)findViewById(R.id.cancelpost);
        ImageButton image=(ImageButton)findViewById(R.id.postimage);
        progressBar = (ProgressBar) findViewById(R.id.addpostPB);
        progressBar.setVisibility(GONE);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();

            }
        });
        post.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                post.setEnabled(false);
                cancel.setEnabled(false);
                final RowVew rw = new RowVew();
                rw.text=text.getText().toString();
                rw.user= ModelUser.instace.getUsername();
                final Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                final Context context=v.getContext();
                rw.imageUrl="";



                rw.id=ModelRowView.instace.RandID();


                if (imageBitmap != null) {
                    ModelRowView.instace.saveImage(imageBitmap, rw.user + String.valueOf(rw.id) + ".jpeg", new ModelRowView.SaveImageListener() {
                        @Override
                        public void complete(String url) {
                            rw.imageUrl = url;
                            ModelRowView.instace.addRow(rw);
                            setResult(RESAULT_SUCCESS);
                            startActivity(myIntent);
                            progressBar.setVisibility(GONE);
                            finish();
                        }

                        @Override
                        public void fail() {
                            setResult(RESAULT_FAIL);
                        }
                    });
                }else{
                    ModelRowView.instace.addRow(rw);
                    setResult(RESAULT_SUCCESS);
                    startActivity(myIntent);

                    finish();
                }



            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main,menu);
        menu.findItem(R.id.main_add).setVisible(false);
        menu.findItem(R.id.main_logout).setVisible(false);
        return true;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int i);
    }


    //Open the C
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(MainActivity.getMyContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageButton imageView=(ImageButton)findViewById(R.id.postimage);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    /*
    When the user clicked on the options menu.
    The user has several options
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case android.R.id.home:
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


}

