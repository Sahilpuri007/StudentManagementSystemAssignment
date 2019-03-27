package com.system.studentmanagement.backgroundhandler;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.system.studentmanagement.dbmanager.DatabaseHelper;
import com.system.studentmanagement.listener.OnFragmentInteractionListener;
import com.system.studentmanagement.model.Student;
import com.system.studentmanagement.util.Constants;

public class BackgroundService extends Service {

    public BackgroundService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DatabaseHelper dbHelper=new DatabaseHelper(this);
        int option = intent.getIntExtra(Constants.EXTRA_OPTION,Constants.ERROR_CODE);
        Student student = intent.getParcelableExtra(Constants.EXTRA_STUDENT_OBJECT);

        switch (option)
        {
            case Constants.ADD_STUDENT_INFO:
                Log.d( "Clicked btnService","add");
             //   Log.d("Checking", "addStudent: "+student);
                dbHelper.getWritableDatabase();
                dbHelper.addStudent(student);
                break;
            case Constants.EDIT_STUDENT_INFO:
                String oldRollNo = intent.getStringExtra(Constants.EXTRA_OLD_ROLL_NO);
                Log.d( "Clicked btnService","edit");
                //Log.d("Checking", "addStudent: "+student + " "+oldRollNo);
                dbHelper.getWritableDatabase();
                dbHelper.updateStudent(oldRollNo,student);
                break;

            case Constants.DELETE_STUDENT_INFO:

                if(student!=null){
                    dbHelper.getWritableDatabase();
                    dbHelper.deleteStudent(student);

                }
                else
                {
                    dbHelper.getWritableDatabase();
                    dbHelper.deleteAll();
                }



        }
        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
