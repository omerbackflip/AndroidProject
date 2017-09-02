package com.example.shemeshda.finalproject.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.Keyboard;
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

    /*
    add row to the firebase
    if the row exists just update it
    The row saved in the fire base which contains all of the post, and details
     of all of the Users of the App
     */
    public void addRow(RowVew rv) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rows");


        Map<String, Object> value = new HashMap<>();
        value.put("user", rv.user);
        value.put("id", rv.id);
        value.put("text", rv.text);
        value.put("imageUrl", rv.imageUrl);
        value.put("isDeleted",rv.isDeleted);
        value.put("lastUpdateDate", ServerValue.TIMESTAMP);
        myRef.child(String.valueOf(rv.id)).setValue(value);
    }


    /*
    get rowView from firebase By the id
     */
    public void getRow(String id, final ModelRowView.GetRowCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rows");
        myRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
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

/*
listener to changes in FB rows
Every time that there is a change in the FB this listener runs
 */
    public void postsUpdates(final double lastUpdateDate, final ModelRowView.RegisterRowUpdatesCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rows");
        myRef.orderByChild("lastUpdateDate").startAt(lastUpdateDate);
        ChildEventListener listener = myRef.orderByChild("lastUpdateDate").startAt(lastUpdateDate).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        RowVew r = dataSnapshot.getValue(RowVew.class);
                        callback.onRowUpdate(r);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        RowVew r = dataSnapshot.getValue(RowVew.class);
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


    /*
    Save image in the FB storage
    Every picture the user uploads to the App,
    Saved in the FB
     */
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

    /*
        Get image from the firebase storage
     */
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
















