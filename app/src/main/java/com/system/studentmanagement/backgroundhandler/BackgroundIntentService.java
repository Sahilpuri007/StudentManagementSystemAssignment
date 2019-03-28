package com.system.studentmanagement.backgroundhandler;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.system.studentmanagement.dbmanager.DatabaseHelper;
import com.system.studentmanagement.model.Student;
import com.system.studentmanagement.util.Constants;


/**
 * Class to handle operations through IntentService
 */
public class BackgroundIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BackgroundIntentService(String name) {
        super(name);
    }

    public BackgroundIntentService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int option = intent.getIntExtra(Constants.EXTRA_OPTION, Constants.ERROR_CODE);
        Student student = intent.getParcelableExtra(Constants.EXTRA_STUDENT_OBJECT);


        switch (option) {
            case Constants.ADD_STUDENT_INFO:
                Log.d("Clicked btnService", "add");
                dbHelper.getWritableDatabase();
                dbHelper.addStudent(student);
                break;
            case Constants.EDIT_STUDENT_INFO:
                String oldRollNo = intent.getStringExtra(Constants.EXTRA_OLD_ROLL_NO);
                Log.d("Clicked btnService", "edit");
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
        intent.setAction("WORKING");
        String echoMessage = "Broadcast Reciever";
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent.putExtra("message",echoMessage));


    }
}
