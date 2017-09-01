package com.example.shemeshda.finalproject.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.shemeshda.finalproject.finalProject;

import java.util.LinkedList;
import java.util.List;



public class RowSql {
    static final String POSTS_TABLE = "posts";
    static final String POSTS_USER = "username";
    static final String POSTS_ID = "id";
    static final String POSTS_TEXT= "text";
    static final String POSTS_IMAGE = "image";
    static final String LAST_UPDATE_DATE="lastupdate";
    ;

    static List<RowVew> getAllRows(SQLiteDatabase db) {
        Cursor cursor = db.query(POSTS_TABLE, null, null, null, null, null, null);
        List<RowVew> list = new LinkedList<RowVew>();
        if (cursor.moveToFirst()) {
            int user = cursor.getColumnIndex(POSTS_USER);
            int text = cursor.getColumnIndex(POSTS_TEXT);
            int imageUrl = cursor.getColumnIndex(POSTS_IMAGE);
            int lastUpdate= cursor.getColumnIndex(LAST_UPDATE_DATE);
            int id=cursor.getColumnIndex(POSTS_ID);


            do {
                RowVew r = new RowVew();
                r.user = cursor.getString(user);
                r.id=cursor.getInt(id);
                r.text = cursor.getString(text);
                r.imageUrl =cursor.getString(imageUrl);
                r.lastUpdateDate=cursor.getDouble(lastUpdate);

                list.add(r);
            } while (cursor.moveToNext());
        }
        return list;
    }

    static void addRow(SQLiteDatabase db, RowVew r)
    {
        Log.d("tag","second update");
        ContentValues values = new ContentValues();
        values.put(POSTS_USER, r.user);
        values.put(POSTS_TEXT, r.text);
        values.put(POSTS_ID, r.id);
        values.put(POSTS_IMAGE, r.imageUrl);
        values.put(LAST_UPDATE_DATE,r.lastUpdateDate);
        db.insert(POSTS_TABLE, POSTS_ID, values);
    }

    static public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + POSTS_TABLE +
                " (" +
                POSTS_ID + " INT PRIMARY KEY, " +
                POSTS_USER + " TEXT, " +
                POSTS_TEXT + " TEXT, " +
                LAST_UPDATE_DATE+" DOUBLE, "+
               POSTS_IMAGE+ " TEXT);";
        db.execSQL(sql);


    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + POSTS_TABLE + ";");
        onCreate(db);
    }

   static public boolean checkID(SQLiteDatabase db,int id)
    {
        ModelSql modelSql =new ModelSql(finalProject.getMyContext());

        String table = POSTS_TABLE;
        String[] columns = {"id"};
        String selection = "id =?";
        String[] selectionArgs = {String.valueOf(id)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        Cursor cursor = modelSql.getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        if(cursor.getCount()==0)
            return true;
        else
            return false;
    }


    public static void editRow(SQLiteDatabase db, RowVew r)
    {

    }

    public static void deletePost(SQLiteDatabase db, RowVew rw)
    {

    }
}
