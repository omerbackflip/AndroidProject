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

public class ModelRowView {
    public final static ModelRowView instace = new ModelRowView();

    private ModelSql modelSql;
    private ModelFirebase modelFB;

    public void deletePost(RowVew rw)
    {
        modelFB.deletePost(rw);
        modelSql.deletePost(modelSql.getWritableDatabase(),rw);
    }

    public interface SaveImageListener {
        void complete(String url);
        void fail();
    }

    public interface GetImageListener{
        void onSuccess(Bitmap image);
        void onFail();
    }

    interface RegisterRowUpdatesCallback{
        void onRowUpdate(RowVew r);
    }

    private ModelRowView(){
        modelFB = new ModelFirebase();
        modelSql =new ModelSql(finalProject.getMyContext());
        synchRowsDbAndregisterRowUpdates();
    }

    public void addRow( RowVew r){
          modelFB.addRow(r);
    }

    public void editRow( RowVew r){
        modelFB.addRow(r);
        modelSql.editRow(modelSql.getWritableDatabase(),r);
    }
   public  interface GetRowCallback {
        void onComplete(RowVew rv);

        void onCancel();
    }

    private void synchRowsDbAndregisterRowUpdates() {
        //1. get local lastUpdateTade
        SharedPreferences pref = finalProject.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        final double lastUpdateDate = pref.getFloat("RowLastUpdateDate",0);
        Log.d("TAG","lastUpdateDate: " + lastUpdateDate);

        modelFB.postsUpdates(lastUpdateDate,new RegisterRowUpdatesCallback() {
            @Override
            public void onRowUpdate(RowVew r) {
                //3. update the local db
                Log.d("tag","check update");
                RowSql.addRow(modelSql.getWritableDatabase(),r);
                //4. update the lastUpdateTade
                SharedPreferences pref = finalProject.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
                final double lastUpdateDate = pref.getFloat("RowLastUpdateDate",0);
                if (lastUpdateDate <r.lastUpdateDate){
                    SharedPreferences.Editor prefEd = finalProject.getMyContext().getSharedPreferences("TAG",
                            Context.MODE_PRIVATE).edit();
                    prefEd.putFloat("RowLastUpdateDate", (float) r.lastUpdateDate);
                    prefEd.commit();
                    Log.d("tag","first update");
                    Log.d("TAG","RowLastUpdateDate: " + r.lastUpdateDate);
                }

                EventBus.getDefault().post(new UpdatepostsEvent(r));
            }
        });
    }

    public void getRow(String id,final GetRowCallback callback)
    {
        modelFB.getRow(id,callback);
    }

    public List<RowVew> getAllrows() {

        return RowSql.getAllRows(modelSql.getReadableDatabase());
    }

    public class UpdatepostsEvent {
        public final RowVew rb;
        public UpdatepostsEvent(RowVew r) {
            this.rb = r;
        }
    }

    public boolean checkID(int id)
    {
        return modelSql.checkID(modelSql.getReadableDatabase(),id);
    }


    public void saveImage(final Bitmap imageBmp, final String name, final SaveImageListener listener) {
        modelFB.saveImage(imageBmp, name, new SaveImageListener() {
            @Override
            public void complete(String url) {
                String fileName = URLUtil.guessFileName(url, null, null);
                saveImageToFile(imageBmp,fileName);
                listener.complete(url);
            }
            @Override
            public void fail() {
                listener.fail();
            }
        });


    }

    public void getImage(final String url, final GetImageListener listener) {
        //check if image exsist localy
        String fileName = URLUtil.guessFileName(url, null, null);
        Bitmap image = loadImageFromFile(fileName);

        if (image != null){
            Log.d("TAG","getImage from local success " + fileName);
            listener.onSuccess(image);
        }else {
            modelFB.getImage(url, new GetImageListener() {
                @Override
                public void onSuccess(Bitmap image) {
                    String fileName = URLUtil.guessFileName(url, null, null);
                    Log.d("TAG","getImage from FB success " + fileName);
                    saveImageToFile(image,fileName);
                    listener.onSuccess(image);
                }

                @Override
                public void onFail() {
                    Log.d("TAG","getImage from FB fail ");
                    listener.onFail();
                }
            });

        }


    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
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

    private Bitmap loadImageFromFile(String imageFileName){
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void addPicureToGallery(File imageFile){
        //add the picture to the gallery so we dont need to manage the cache size
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        finalProject.getMyContext().sendBroadcast(mediaScanIntent);
    }
}
