package com.system.studentmanagement.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.system.studentmanagement.R;
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
    private Button btnAddStudent;
    private ArrayList<Student> studentArrayList = new ArrayList<>();;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        initComponents();
        manageIntent();

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

        dbHelper = new DatabaseHelper(this);
        studentArrayList.addAll(dbHelper.getAllStudents());

    }

    /*
     * method manageIntent
     * To check intent and open activity in desired mode i.e. ADD , VIEW or EDIT
     */
    private void manageIntent() {
       Intent intent = getIntent();
        //studentArrayList = intent.getParcelableArrayListExtra(Constants.EXTRA_ARRAY_LIST);
        final Student student = intent.getParcelableExtra(Constants.EXTRA_STUDENT_OBJECT);
       if (intent.getIntExtra(Constants.EXTRA_OPTION, Constants.ERROR_CODE) == Constants.VIEW_STUDENT_INFO) {
            viewStudent(student);
        }
        else if(intent.getIntExtra(Constants.EXTRA_OPTION, Constants.ERROR_CODE) == Constants.EDIT_STUDENT_INFO) {
            int position = intent.getIntExtra(Constants.EXTRA_POSITION, -1);
            updateStudent(student, position, studentArrayList);
        }
       else{
          // Log.d("here ",studentArrayList.toString());
            addStudent();
        }

    }

    /*
     * method addStudent
     * to add new Student
     * @param ArrayList<Student> studentArrayList
     */
    private void addStudent() {
        dbHelper = new DatabaseHelper(this);
        etName.requestFocus();
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("inside OnClick", "yes");


                int counter = 0;
                String studentName = etName.getText().toString().trim();
                String studentRollNo = etRollNo.getText().toString();
                Student student = new Student(studentName, studentRollNo);
                Log.d("inside add", studentName + " " + studentRollNo + " " + student + " " + student.getName() + " " + student.getRollNo());

                if (validate()) {
                    Log.d("Validate", "Wrong");

                } else {
                    if(!(studentArrayList.size()==0)){
                   for (Student ItrStudent : studentArrayList) {
                       if (student.getRollNo().equals(ItrStudent.getRollNo())) {
                           etRollNo.setError(getString(R.string.error_roll_no));
                           counter = -1;
                       }
                   }
                    }
                    Intent returnIntent = new Intent();
                   if (counter != -1) {

                        //returnIntent.putExtra(Constants.EXTRA_STUDENT_OBJECT, student);
                        Log.d("inside add", studentName);
                        dbHelper.getWritableDatabase();
                        dbHelper.addStudent(student);
                        returnIntent.putExtra("id", studentRollNo);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                   }
                }
            }

        });

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
     * method updateStudent
     * to add new Student
     * @param Student student
     * @param int position
     * @param ArrayList<Student> studentArrayList
     */
    private void updateStudent(final Student oldstudent, final int position,
                               final ArrayList<Student> studentArrayList) {

        tvTitle.setText(getString(R.string.update_title));
        btnAddStudent.setText(getString(R.string.update));
        final String oldRollNo = oldstudent.getRollNo();
        etRollNo.setText(oldstudent.getRollNo());
        etName.setText(oldstudent.getName());
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
                        Log.d("here", "onClick: in Update");
                        dbHelper.getWritableDatabase();
                        dbHelper.updateStudent(oldRollNo,newstudent);
                        returnIntent.putExtra("id", studentRollNo);
                        returnIntent.putExtra(Constants.EXTRA_POSITION, position);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                }
            }
        });
    }

    /*
     * method validate
     * to validate input fields
     * @return boolean true or false according to input
     */
    private boolean validate(){

        Validator validator= new Validator();
        if (validator.isEmpty(etName)) {
            etName.setError(getString(R.string.error_name_requires));
            return true;
        }
        else if (validator.isValidName(etName)) {
            etName.setError(getString(R.string.error_name));
            return true;
        }

        else if (validator.isEmpty(etRollNo)) {
            etRollNo.setError(getString(R.string.error_rollno));
            return true;
        }
        else if(validator.isValidRollNo(etRollNo)){
            etRollNo.setError(getString(R.string.error_valid_roll));
            return true;
            }

        return false;
    }

}