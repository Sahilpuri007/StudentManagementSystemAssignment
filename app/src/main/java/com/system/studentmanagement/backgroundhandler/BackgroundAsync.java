package com.system.studentmanagement.backgroundhandler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.system.studentmanagement.dbmanager.DatabaseHelper;
import com.system.studentmanagement.model.Student;
import com.system.studentmanagement.util.Constants;

import javax.security.auth.callback.Callback;

public class BackgroundAsync extends AsyncTask<Object,Void,Void> {

    private Context context;

    public BackgroundAsync(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Object... objects) {
        int option =Integer.parseInt(objects[0].toString());
        Student student = (Student) objects[1];
        DatabaseHelper dbHelper=new DatabaseHelper(context);
        switch (option) {

            case Constants.ADD_STUDENT_INFO:
                dbHelper.getWritableDatabase();
                dbHelper.addStudent(student);
                break;


            case Constants.EDIT_STUDENT_INFO:
                String oldRollNo = objects[2].toString();
                dbHelper.getWritableDatabase();
                dbHelper.updateStudent(oldRollNo, student);
                break;
            case Constants.DELETE_STUDENT_INFO:

                if(student != null){
                    dbHelper.getWritableDatabase();
                    dbHelper.deleteStudent(student);

                }
                else
                {
                    dbHelper.getWritableDatabase();
                    dbHelper.deleteAll();
                }


        }

        return null;
    }






}
