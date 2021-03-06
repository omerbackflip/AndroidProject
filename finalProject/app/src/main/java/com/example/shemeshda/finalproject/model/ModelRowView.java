package com.example.shemeshda.finalproject.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.shemeshda.finalproject.finalProject;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

public class ModelRowView {
    public final static ModelRowView instace = new ModelRowView();

    private ModelSql modelSql;
    private ModelFirebase modelFB;

    /*
    When the User wants to delete a post.
    This delete action is LOGICAL so the post itself is not actually deleted
    change the is delete to true and update the row in FB
     */
    public void deletePost(RowVew rw) {
        rw.isDeleted = true;
        modelFB.addRow(rw);
    }

    public interface SaveImageListener {
        void complete(String url); //When save image sucsseded

        void fail(); //When save Image faild
    }

    public interface GetImageListener {
        void onSuccess(Bitmap image);

        void onFail();
    }

    interface RegisterRowUpdatesCallback {
        void onRowUpdate(RowVew r);
    }

    private ModelRowView() {
        modelFB = new ModelFirebase();
        modelSql = new ModelSql(finalProject.getMyContext());
        synchRowsDbAndregisterRowUpdates();
    }

    /*
    Add row into the FireBase
     */
    public void addRow(RowVew r) {
        modelFB.addRow(r);
    }

    /*
    Edit or Change row in the FireBase
     */
    public void editRow(RowVew r) {
        modelFB.addRow(r);
    }

    public interface GetRowCallback {
        void onComplete(RowVew rv);

        void onCancel();
    }

    /*
    This function listns to changes in the FB
     */
    private void synchRowsDbAndregisterRowUpdates() {
        //1. get local lastUpdateTade
        SharedPreferences pref = finalProject.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        final double lastUpdateDate = pref.getFloat("RowLastUpdateDate", 0);
        Log.d("TAG", "lastUpdateDate: " + lastUpdateDate);

        modelFB.postsUpdates(lastUpdateDate, new RegisterRowUpdatesCallback() {
            @Override
            public void onRowUpdate(RowVew r) {
                //3. update the local db;
                boolean i = !checkID(r.id); //Here - we check if we need to change row or edit row.
                //these achtins takes different time each - so we need to know which action to perform
                if (i)
                    modelSql.editRow(modelSql.getWritableDatabase(), r);
                else
                    modelSql.addRow(modelSql.getWritableDatabase(), r);
                //4. update the lastUpdateTade
                SharedPreferences pref = finalProject.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
                final double lastUpdateDate = pref.getFloat("RowLastUpdateDate", 0);
                if (lastUpdateDate < r.lastUpdateDate) {
                    SharedPreferences.Editor prefEd = finalProject.getMyContext().getSharedPreferences("TAG",
                            Context.MODE_PRIVATE).edit();
                    prefEd.putFloat("RowLastUpdateDate", (float) r.lastUpdateDate);
                    prefEd.commit();
                    Log.d("tag", "first update");
                    Log.d("TAG", "RowLastUpdateDate: " + r.lastUpdateDate);
                }

                EventBus.getDefault().post(new UpdatepostsEvent(r));
            }
        });
    }

    /*
    get row from FB
    each row of the FireBase Presents a Post of a certain user
     */
    public void getRow(String id, final GetRowCallback callback) {
        modelFB.getRow(id, callback);
    }

    /*
    return all rows in the sql db
    Means - get all of the post that save locally in the sql db
     */
    public List<RowVew> getAllrows() {

        return RowSql.getAllRows(modelSql.getReadableDatabase());
    }

    public class UpdatepostsEvent {
        public final RowVew rb;

        public UpdatepostsEvent(RowVew r) {
            this.rb = r;
        }
    }

    //checking if the id already exist in the sql db
    public boolean checkID(int id) {
        return modelSql.checkID(modelSql.getReadableDatabase(), id);
    }

    //get row by id from sql db
    public RowVew getRowbyIDsql(String id) {
        return modelSql.getRowbyIDsql(modelSql.getReadableDatabase(), id);
    }

    //Save the image in FB storage and then added in to local file storage
    public void saveImage(final Bitmap imageBmp, final String name, final SaveImageListener listener) {
        modelFB.saveImage(imageBmp, name, new SaveImageListener() {
            @Override
            public void complete(String url) {
                String fileName = URLUtil.guessFileName(url, null, null);
                saveImageToFile(imageBmp, fileName);
                listener.complete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });


    }

    //get the image from FB storage and checking if it saved in local file storage
    public void getImage(final String url, final GetImageListener listener) {
        //check if image exsist localy
        String fileName = URLUtil.guessFileName(url, null, null);
        Bitmap image = loadImageFromFile(fileName);

        if (image != null) {
            Log.d("TAG", "getImage from local success " + fileName);
            listener.onSuccess(image);
        } else {
            modelFB.getImage(url, new GetImageListener() {
                @Override
                public void onSuccess(Bitmap image) { //When get Image from the FireBase Sucsseded
                    String fileName = URLUtil.guessFileName(url, null, null);
                    Log.d("TAG", "getImage from FB success " + fileName);
                    saveImageToFile(image, fileName);
                    listener.onSuccess(image);
                }

                @Override
                public void onFail() { //When get Image from the FireBase Failed
                    Log.d("TAG", "getImage from FB fail ");
                    listener.onFail();
                }
            });

        }


    }

    /*
    save the image in local storage file
     */
    private void saveImageToFile(Bitmap imageBitmap, String imageFileName) {
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir, imageFileName);
            imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            addPicureToGallery(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //load image from local storage file
    private Bitmap loadImageFromFile(String imageFileName) {
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir, imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag", "got image from cache: " + imageFileName);
        } catch (FileNotFoundException e) { // If the file not found
            e.printStackTrace();
        }
        return bitmap;
    }

    /*
    Add the picture to user picture gallery
     */
    private void addPicureToGallery(File imageFile) {
        //add the picture to the gallery so we dont need to manage the cache size
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        finalProject.getMyContext().sendBroadcast(mediaScanIntent);
    }
    //*** A random number between 1 to 3000000, we re-randomise this number everytime if this number has already selected
    public int RandID()
    {
        Random rand=new Random();
        int  randomNum = 1 + rand.nextInt((3000000 - 1) + 1);

        while(!checkID(randomNum))
        {
            randomNum = 1 + rand.nextInt((3000000 - 1) + 1);

        }
        return randomNum;
    }
}
