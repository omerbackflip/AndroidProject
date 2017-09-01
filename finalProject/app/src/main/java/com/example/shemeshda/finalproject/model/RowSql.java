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
    static final String POST_DELTE ="isDeleted";
    static final String LAST_UPDATE_DATE="lastupdate";

    public static List<RowVew> getAllRows(SQLiteDatabase db) {
        Cursor cursor = db.query(POSTS_TABLE, null, null, null, null, null, null);
        List<RowVew> list = new LinkedList<RowVew>();
        if (cursor.moveToFirst()) {
            int user = cursor.getColumnIndex(POSTS_USER);
            int text = cursor.getColumnIndex(POSTS_TEXT);
            int imageUrl = cursor.getColumnIndex(POSTS_IMAGE);
            int lastUpdate= cursor.getColumnIndex(LAST_UPDATE_DATE);
            int id=cursor.getColumnIndex(POSTS_ID);
            int delete=cursor.getColumnIndex(POST_DELTE);

            do {
                RowVew r = new RowVew();
                if(cursor.getInt(delete)!=1) {
                    r.user = cursor.getString(user);
                    r.id = cursor.getInt(id);
                    r.text = cursor.getString(text);
                    r.isDeleted = false;
                    r.imageUrl = cursor.getString(imageUrl);
                    r.lastUpdateDate = cursor.getDouble(lastUpdate);

                    list.add(r);
                }
            } while (cursor.moveToNext());
        }
        return list;
    }
    public static int getRowID(SQLiteDatabase db, String id) {
        int id3=0;
        String table = POSTS_TABLE;
        String[] columns = {"id"};
        String selection = "id =?";
        String[] selectionArgs = {String.valueOf(id)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            int id2 = cursor.getColumnIndex(POSTS_ID);

        if(cursor.moveToFirst())
            id3 = cursor.getInt(id2);

        return id3;
    }

    public static void addRow(SQLiteDatabase db, RowVew r)
    {
        Log.d("tag","second update");
        ContentValues values = new ContentValues();
        values.put(POSTS_USER, r.user);
        values.put(POSTS_TEXT, r.text);
        values.put(POSTS_ID, r.id);

        if(r.isDeleted)
            values.put(POST_DELTE, 1);
        else
            values.put(POST_DELTE, 0);

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
                POST_DELTE + " INT, " +
                LAST_UPDATE_DATE+" DOUBLE, "+
               POSTS_IMAGE+ " TEXT" + ");";
        db.execSQL(sql);


    }

    public static  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + POSTS_TABLE );
        onCreate(db);
    }

    public static  boolean checkID(SQLiteDatabase db,int id)
    {

        String table = POSTS_TABLE;
        String[] columns = {"id"};
        String selection = "id =?";
        String[] selectionArgs = {String.valueOf(id)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        Cursor cursor =db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        if(cursor.getCount()==0)
            return true;
        else
            return false;
    }


    public static void editRow(SQLiteDatabase db, RowVew r)
    {
        ContentValues values = new ContentValues();
        values.put(POSTS_USER, r.user);
        values.put(POSTS_TEXT, r.text);
        values.put(POSTS_ID, r.id);

        if(r.isDeleted)
            values.put(POST_DELTE, 1);
        else
            values.put(POST_DELTE, 0);

        values.put(POSTS_IMAGE, r.imageUrl);
        values.put(LAST_UPDATE_DATE,r.lastUpdateDate);
        db.update(POSTS_TABLE,values,"id=?",new String[]{String.valueOf(r.id)});
    }

    public static void deletePost(SQLiteDatabase db, RowVew r)
    {
        ContentValues values = new ContentValues();
        values.put(POSTS_USER, r.user);
        values.put(POSTS_TEXT, r.text);
        values.put(POSTS_ID, r.id);
        values.put(POST_DELTE, 1);
        db.update(POSTS_TABLE,values,"id=?",new String[]{String.valueOf(r.id)});
    }
}
