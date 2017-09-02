package com.example.shemeshda.finalproject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.shemeshda.finalproject.model.ModelFirebaseUser;
import com.example.shemeshda.finalproject.model.ModelRowView;
import com.example.shemeshda.finalproject.model.ModelUser;
import com.example.shemeshda.finalproject.model.RowVew;
import java.util.Random;
import static android.view.View.GONE;


/*
When the user wants to Edit row - means edit his post
The user can only edit HIS post, and not edit posts of other users
 */
public class EditRowActivity extends Activity {
    private static final String ID = "id";
    Bitmap imageBitmap;
    private MenuItem delete;
    ProgressBar progressBar;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESAULT_SUCCESS = 0;
    final static int RESAULT_FAIL = 1;
    RowVew rw;



    public EditRowActivity() {
        // Required empty public constructor
    }
    //all the button listeners and the the views
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_row);
        Intent intent = getIntent();
        int s=intent.getIntExtra("RID2",0);
        final EditText text= (EditText)findViewById(R.id.edittext);
        final  Button post= (Button)findViewById(R.id.save_edit_post);
       final Button cancel= (Button)findViewById(R.id.cancelEdit);
        final ImageView imageView = (ImageView)findViewById(R.id.editimage);

        progressBar = (ProgressBar) findViewById(R.id.editpostPB);
        progressBar.setVisibility(GONE);

                rw=ModelRowView.instace.getRowbyIDsql(String.valueOf(s));
                text.setText(rw.text);
                if (!rw.imageUrl.equals("")) {
                    ModelRowView.instace.getImage(rw.imageUrl, new ModelRowView.GetImageListener() {
                        @Override
                        public void onSuccess(Bitmap image) {
                            imageView.setImageBitmap(image);
                        }

                        @Override
                        public void onFail() {

                        }
                    });
                }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();

            }
        });
        post.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                    delete.setVisible(false);
                    post.setEnabled(false);
                    cancel.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    rw.text = text.getText().toString();
                    rw.user = ModelUser.instace.getUsername();
                    final Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                    final Context context = v.getContext();

                //*** A random number between 1 to 3000000, we re-randomise this number everytime if this number has already selected

                    Random rand = new Random();
                    int randomNum = 1 + rand.nextInt((3000000 - 1) + 1);

                    while (!ModelRowView.instace.checkID(randomNum)) {
                        randomNum = 1 + rand.nextInt((3000000 - 1) + 1);

                    }


                    if (imageBitmap != null) {
                        ModelRowView.instace.saveImage(imageBitmap, rw.user + String.valueOf(randomNum) + ".jpeg", new ModelRowView.SaveImageListener() {
                            @Override
                            public void complete(String url) {
                                rw.imageUrl = url;
                                ModelRowView.instace.editRow(rw);
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
                    } else {
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

    /*
    Create the user's option menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main,menu);
        delete=menu.findItem(R.id.deletePost);
        delete.setVisible(true);
        menu.findItem(R.id.main_add).setVisible(false);
        menu.findItem(R.id.main_logout).setVisible(false);
        return true;
    }


    // TODO: Rename method, update argument and hook method into UI eve



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int i);
    }


    //Open the camera to take picture
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(MainActivity.getMyContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageButton imageView=(ImageButton)findViewById(R.id.editimage);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    /*
    When the user had selected an item in the option menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.deletePost:{ //When the user wants to delete a spacific post that he has uploaded befor
                ModelRowView.instace.deletePost(rw);
                Intent myIntent = new Intent(this, MainActivity.class);
                startActivity(myIntent);
                finish();
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

