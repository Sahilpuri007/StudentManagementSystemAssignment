package com.system.studentmanagement.backgroundhandler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.system.studentmanagement.dbmanager.DatabaseHelper;
import com.system.studentmanagement.model.Student;
import com.system.studentmanagement.util.Constants;

/**
 * Class to handle operations through Service
 */
public class BackgroundService extends Service {

    public BackgroundService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int option = intent.getIntExtra(Constants.EXTRA_OPTION, Constants.ERROR_CODE);
        Student student = intent.getParcelableExtra(Constants.EXTRA_STUDENT_OBJECT);


        switch (option) {
            case Constants.ADD_STUDENT_INFO:
                Log.d("Clicked btnService", "add");
                //   Log.d("Checking", "addStudent: "+student);
                dbHelper.getWritableDatabase();
                dbHelper.addStudent(student);
                break;
            case Constants.EDIT_STUDENT_INFO:
                String oldRollNo = intent.getStringExtra(Constants.EXTRA_OLD_ROLL_NO);
                Log.d("Clicked btnService", "edit");
                //Log.d("Checking", "addStudent: "+student + " "+oldRollNo);
                dbHelper.getWritableDatabase();
                dbHelper.updateStudent(oldRollNo, student);
                break;

            case Constants.DELETE_STUDENT_INFO:

                if (student != null) {
                    dbHelper.getWritableDatabase();
                    dbHelper.deleteStudent(student);

                } else {
                    dbHelper.getWritableDatabase();
                    dbHelper.deleteAll();
                }


        }
        intent.setAction(Constants.BROADCAST_ACTION);
        String echoMessage = Constants.BROADCAST_MESSAGE;
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent.putExtra(Constants.EXTRA_MESSAGE,echoMessage));
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
