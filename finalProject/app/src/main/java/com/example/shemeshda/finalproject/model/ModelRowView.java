package com.example.shemeshda.finalproject.model;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shemeshda.finalproject.MainActivity;
import com.example.shemeshda.finalproject.finalProject;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ModelRowView {
    public final static ModelRowView instace = new ModelRowView();

    private ModelSql modelSql;
    private ModelFirebase modelFB;

    private ModelRowView(){
        modelFB = new ModelFirebase();
        modelSql =new ModelSql(finalProject.getMyContext());
        synchStudentsDbAndregisterStudentsUpdates();
    }

    public void addRow( RowVew r){
          modelFB.addRow(r);
        modelSql.addRow(modelSql.getWritableDatabase(),r);
    }


    private void synchStudentsDbAndregisterStudentsUpdates() {
        //1. get local lastUpdateTade
        SharedPreferences pref = finalProject.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        final double lastUpdateDate = pref.getFloat("RowLastUpdateDate",0);
        Log.d("TAG","lastUpdateDate: " + lastUpdateDate);

        modelFB.registerStudentsUpdates(lastUpdateDate,new ModelFirebase.RegisterRowUpdatesCallback() {
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

                EventBus.getDefault().post(new UpdateStudentEvent(r));
            }
        });
    }

    public List<RowVew> getAllrows() {

        return RowSql.getAllRows(modelSql.getReadableDatabase());
    }

    public class UpdateStudentEvent {
        public final RowVew rb;
        public UpdateStudentEvent(RowVew r) {
            this.rb = r;
        }
    }

    public boolean checkID(int id)
    {
        return modelSql.checkID(modelSql.getReadableDatabase(),id);
    }


}
