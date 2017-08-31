package com.example.shemeshda.finalproject.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ModelFirebase {
    List<ChildEventListener> listeners = new LinkedList<ChildEventListener>();

    public void addRow(RowVew rv) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rows");

        Random rand=new Random();
        int  randomNum = 1 + rand.nextInt((3000000 - 1) + 1);

        while(!ModelRowView.instace.checkID(randomNum))
        {
            randomNum = 1 + rand.nextInt((3000000 - 1) + 1);

        }

        rv.id=randomNum;

        Map<String, Object> value = new HashMap<>();
        value.put("user", rv.user);
        value.put("id", rv.id);
        value.put("text", rv.text);
        value.put("imageUrl", rv.imageUrl);
        value.put("lastUpdateDate", ServerValue.TIMESTAMP);
        myRef.child(String.valueOf(rv.id)).setValue(value);
    }

    interface GetRowCallback {
        void onComplete(RowVew rv);

        void onCancel();
    }

    public void getRow(String user, final GetRowCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rows");
        myRef.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RowVew r = dataSnapshot.getValue(RowVew.class);
                callback.onComplete(r);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }


    interface RegisterRowUpdatesCallback{
        void onRowUpdate(RowVew r);
    }

    public void registerStudentsUpdates(final double lastUpdateDate, final RegisterRowUpdatesCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rows");
        myRef.orderByChild("lastUpdateDate").startAt(lastUpdateDate);
        ChildEventListener listener = myRef.orderByChild("lastUpdateDate").startAt(lastUpdateDate).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("TAG","onChildAdded called");
                        Log.d("tag","first update");
                        RowVew r = dataSnapshot.getValue(RowVew.class);
                        callback.onRowUpdate(r);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        RowVew r = dataSnapshot.getValue(RowVew.class);
                        Log.d("tag","checkkk update");
                        callback.onRowUpdate(r);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        RowVew r = dataSnapshot.getValue(RowVew.class);
                        callback.onRowUpdate(r);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        RowVew r = dataSnapshot.getValue(RowVew.class);
                        callback.onRowUpdate(r);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        listeners.add(listener);
    }

    public void saveImage(Bitmap imageBmp, String name, final ModelRowView.SaveImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference imagesRef = storage.getReference().child("images").child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.complete(downloadUrl.toString());
            }
        });
    }


    public void getImage(String url, final ModelRowView.GetImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                listener.onSuccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.d("TAG",exception.getMessage());
                listener.onFail();
            }
        });
    }

}
















