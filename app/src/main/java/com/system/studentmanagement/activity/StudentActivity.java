package com.system.studentmanagement.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.system.studentmanagement.R;
import com.system.studentmanagement.backgroundhandler.BackgroundAsync;
import com.system.studentmanagement.backgroundhandler.BackgroundIntentService;
import com.system.studentmanagement.backgroundhandler.BackgroundService;
import com.system.studentmanagement.dbmanager.DatabaseHelper;
import com.system.studentmanagement.model.Student;
import com.system.studentmanagement.util.Constants;
import com.system.studentmanagement.util.Validator;

import java.util.ArrayList;


/*
 * @author Sahil Puri
 * StudentActivity
 * Activity which is used to Add,View and Edit Student Info
 */

public class StudentActivity extends AppCompatActivity {

    private EditText etName, etRollNo;
    private TextView tvTitle;
    private Button btnAddStudent, btnAsync, btnService, btnIntentServ;
    private ArrayList<Student> studentArrayList = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private Context mContext;
    private BackgroundAsync taskAsync;
    private DataReceiver dataReceiver = new DataReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        initComponents();
        manageIntent();

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("WORKING");
        LocalBroadcastManager.getInstance(mContext).registerReceiver(dataReceiver,intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(dataReceiver);
    }

    /*
     * method initComponents
     * To Initialize all views
     */
    private void initComponents() {
        etName = findViewById(R.id.etName);
        etRollNo = findViewById(R.id.etRollNo);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        tvTitle = findViewById(R.id.tvTitleScreen);
        mContext = this;
        dbHelper = new DatabaseHelper(this);
        studentArrayList.addAll(dbHelper.getAllStudents());
        taskAsync = new BackgroundAsync(mContext);

    }

    /*
     * method manageIntent
     * To check intent and open activity in desired mode i.e. ADD , VIEW or EDIT
     */
    private void manageIntent() {
        Intent intent = getIntent();
        final Student student = intent.getParcelableExtra(Constants.EXTRA_STUDENT_OBJECT);
        if (intent.getIntExtra(Constants.EXTRA_OPTION, Constants.ERROR_CODE) == Constants.VIEW_STUDENT_INFO) {
            viewStudent(student);
        } else if (intent.getIntExtra(Constants.EXTRA_OPTION, Constants.ERROR_CODE) == Constants.EDIT_STUDENT_INFO) {
            int position = intent.getIntExtra(Constants.EXTRA_POSITION, -1);
            updateMode(student, position, studentArrayList);
        } else {
            addMode();
        }

    }

    /*
     * method viewStudent
     * to view Student
     * @param Student student
     */
    private void viewStudent(final Student student) {

        etName.setFocusable(false);
        etRollNo.setFocusable(false);
        etName.setEnabled(false);
        etRollNo.setEnabled(false);
        etName.setText(student.getName());
        etRollNo.setText(student.getRollNo());
        btnAddStudent.setVisibility(View.INVISIBLE);
        tvTitle.setText(getString(R.string.student_details));
    }


    /*
     * method addMode
     * to add new Student
     */
    private void addMode() {
        dbHelper = new DatabaseHelper(this);
        etName.requestFocus();
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int counter = 0;
                String studentName = etName.getText().toString().trim();
                String studentRollNo = etRollNo.getText().toString();
                Student student = new Student(studentName, studentRollNo);

                if (validate()) {
                    Log.d("Validate", "Wrong");

                } else {
                    if (!(studentArrayList.size() == 0)) {
                        for (Student ItrStudent : studentArrayList) {
                            if (student.getRollNo().equals(ItrStudent.getRollNo())) {
                                etRollNo.setError(getString(R.string.error_roll_no));
                                counter = -1;
                            }
                        }
                    }

                    Intent returnIntent = new Intent();
                    if (counter != -1) {
                        etName.setEnabled(false);
                        etRollNo.setEnabled(false);
                        openOptionsDialog(Constants.ADD_STUDENT_INFO, student, null);
                        etName.setEnabled(true);
                        etRollNo.setEnabled(true);
                        returnIntent.putExtra("id", studentRollNo);
                        setResult(RESULT_OK, returnIntent);

                    }
                }
            }

        });

    }


    /*
     * method updateMode
     * to add new Student
     * @param Student student
     * @param int position
     * @param ArrayList<Student> studentArrayList
     */
    private void updateMode(final Student student, final int position,
                            final ArrayList<Student> studentArrayList) {
        dbHelper = new DatabaseHelper(this);
        tvTitle.setText(getString(R.string.update_title));
        btnAddStudent.setText(getString(R.string.update));
        final String oldRollNo = student.getRollNo();
        Log.d("update", "updateMode: old rolno " + oldRollNo);
        etRollNo.setText(student.getRollNo());
        etName.setText(student.getName());
        etName.requestFocus();

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int counter = 0;
                String studentName = etName.getText().toString().trim();
                String studentRollNo = etRollNo.getText().toString();
                Student newstudent;
                if (validate()) {
                    Log.d("Validate", "Wrong");
                } else {
                    newstudent = new Student(studentName, studentRollNo);
                    Log.d("update", "updateMode: new " + newstudent.getName() + " " + newstudent.getRollNo());
                    for (Student ItrStudent : studentArrayList) {

                        if (counter == position) {

                            counter++;
                            continue;
                        } else if (newstudent.getRollNo().equals(ItrStudent.getRollNo())) {
                            etRollNo.setError(getString(R.string.error_roll_no));
                            counter = -1;

                            break;
                        }
                        counter++;
                    }

                    Intent returnIntent = new Intent();
                    if (counter != -1) {
                        Log.d("here", "onClick;update");
                        etName.setEnabled(false);
                        etRollNo.setEnabled(false);
                        openOptionsDialog(Constants.EDIT_STUDENT_INFO, newstudent, oldRollNo);
                        etName.setEnabled(true);
                        etRollNo.setEnabled(true);
                        returnIntent.putExtra("updated", studentRollNo);
                        returnIntent.putExtra(Constants.EXTRA_POSITION, position);
                        setResult(Constants.EDIT_STUDENT_INFO, returnIntent);

                    }
                }
            }
        });
    }

    /**
     * for options of database interactions
     *
     * @param option    type of operation
     * @param student   Student object
     * @param oldRollNo oldRollNo of student
     */
    private void openOptionsDialog(final int option, final Student student, final String oldRollNo) {
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_options, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(mView).create();
        initAlertDialogView(mView);
        btnAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskAsync(option, student, oldRollNo);
                dialog.dismiss();
            }
        });
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskService(option, student, oldRollNo);
                dialog.dismiss();
            }
        });
        btnIntentServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskIntentService(option, student, oldRollNo);
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    /**
     * to execute async operation
     *
     * @param option    type of operation
     * @param student   Student object
     * @param oldRollNo oldRollNo of student
     */
    private void taskAsync(int option, Student student, String oldRollNo) {

        taskAsync.execute(option, student, oldRollNo);
        finish();
    }

    /**
     * to execute service operation
     *
     * @param option    type of operation
     * @param student   Student object
     * @param oldRollNo oldRollNo of student
     */
    private void taskService(int option, Student student, String oldRollNo) {
        Log.d("INSIDE", "Service " + option);
        Intent service = new Intent(mContext, BackgroundService.class);
        service.putExtra(Constants.EXTRA_STUDENT_OBJECT, student);
        service.putExtra(Constants.EXTRA_OPTION, option);
        service.putExtra(Constants.EXTRA_OLD_ROLL_NO, oldRollNo);
        startService(service);
        finish();
    }

    /**
     * to execute intent service operation
     *
     * @param option    type of operation
     * @param student   Student object
     * @param oldRollNo oldRollNo of student
     */
    private void taskIntentService(int option, Student student, String oldRollNo) {
        Log.d("INSIDE", "Intent Service " + option);
        Intent intentService = new Intent(mContext, BackgroundIntentService.class);
        intentService.putExtra(Constants.EXTRA_STUDENT_OBJECT, student);
        intentService.putExtra(Constants.EXTRA_OPTION, option);
        intentService.putExtra(Constants.EXTRA_OLD_ROLL_NO, oldRollNo);
        startService(intentService);
        finish();

    }

    /*
     * method initDialogView
     * To initialize views of the dialog
     * @param View mView
     */
    private void initAlertDialogView(final View mView) {
        btnAsync = mView.findViewById(R.id.add_dialog_btnAsync);
        btnService = mView.findViewById(R.id.add_dialog_btnService);
        btnIntentServ = mView.findViewById(R.id.add_dialog_btnIntentServ);
    }


    /*
     * method validate
     * to validate input fields
     * @return boolean true or false according to input
     */
    private boolean validate() {

        Validator validator = new Validator();
        if (validator.isEmpty(etName)) {
            etName.setError(getString(R.string.error_name_requires));
            return true;
        } else if (validator.isValidName(etName)) {
            etName.setError(getString(R.string.error_name));
            return true;
        } else if (validator.isEmpty(etRollNo)) {
            etRollNo.setError(getString(R.string.error_rollno));
            return true;
        } else if (validator.isValidRollNo(etRollNo)) {
            etRollNo.setError(getString(R.string.error_valid_roll));
            return true;
        }

        return false;
    }
    public class DataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"Broadcast Reciver",Toast.LENGTH_SHORT).show();

        }
    }

}