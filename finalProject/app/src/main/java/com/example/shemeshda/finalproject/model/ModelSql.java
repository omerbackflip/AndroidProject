package com.example.shemeshda.finalproject.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class ModelSql extends SQLiteOpenHelper {
    ModelSql(Context context) {
        super(context, "database2.db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        RowSql.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        RowSql.onUpgrade(db, oldVersion, newVersion);
    }

    public void addRow(SQLiteDatabase db, RowVew r)
    {
        RowSql.addRow(db,r);
    }


    public boolean checkID(SQLiteDatabase db, int id)
    {
       return RowSql.checkID(db,id);
    }

    public void editRow(SQLiteDatabase db, RowVew r) {
        RowSql.editRow(db,r);
    }

    public void deletePost(SQLiteDatabase db, RowVew rw)
    {
        RowSql.deletePost(db,rw);
    }

    public RowVew getRowbyIDsql(SQLiteDatabase db, String id)
    {
        return RowSql.getRowbyIDsql(db,id);
    }
}
