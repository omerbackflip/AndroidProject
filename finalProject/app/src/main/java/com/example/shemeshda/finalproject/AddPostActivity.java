package com.example.shemeshda.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shemeshda.finalproject.model.ModelRowView;
import com.example.shemeshda.finalproject.model.ModelUser;
import com.example.shemeshda.finalproject.model.RowVew;

import java.util.Random;

public class AddPostActivity extends Activity {

    // private static String username ;
    private static final String ID = "id";
    Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESAULT_SUCCESS = 0;
    final static int RESAULT_FAIL = 1;



    public AddPostActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        final TextView text= (TextView)findViewById(R.id.posttext);
        ImageButton img= (ImageButton) findViewById(R.id.postimage);

        Button post= (Button)findViewById(R.id.addpost);
        Button cancel= (Button)findViewById(R.id.cancelpost);
        ImageButton image=(ImageButton)findViewById(R.id.postimage);

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
                final RowVew rw = new RowVew();
                rw.text=text.getText().toString();
                rw.user= ModelUser.instace.getUsername();
                rw.imageUrl="";

                Random rand=new Random();
                int  randomNum = 1 + rand.nextInt((3000000 - 1) + 1);

                while(!ModelRowView.instace.checkID(randomNum))
                {
                    randomNum = 1 + rand.nextInt((3000000 - 1) + 1);

                }

                rw.id=randomNum;


                if (imageBitmap != null) {
                    ModelRowView.instace.saveImage(imageBitmap, rw.user + String.valueOf(randomNum) + ".jpeg", new ModelRowView.SaveImageListener() {
                        @Override
                        public void complete(String url) {
                            rw.imageUrl = url;
                            ModelRowView.instace.addRow(rw);
                            setResult(RESAULT_SUCCESS);
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
                    finish();
                }


                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }


    // TODO: Rename method, update argument and hook method into UI eve



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int i);
    }



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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.main_add: {
                Intent myIntent = new Intent(this, AddPostActivity.class);
                startActivity(myIntent);
                break;
            }
            case R.id.main_logout: {
                ModelUser.instace.signOut();
                ModelUser.instace.signOut();
                Intent myIntent = new Intent(this, MainActivity.class);
                startActivity(myIntent);

                break;
            }
            case android.R.id.home:
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


}

