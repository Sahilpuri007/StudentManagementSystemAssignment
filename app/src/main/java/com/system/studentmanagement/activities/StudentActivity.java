package com.system.studentmanagement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.system.studentmanagement.R;
import com.system.studentmanagement.model.Student;
import com.system.studentmanagement.util.Constants;

import java.util.ArrayList;


/*
 * @author Sahil Puri
 * StudentActivity
 * Activity which is used to Add,View and Edit Student Info
 */

public class StudentActivity extends AppCompatActivity {

    private static final String NAME_REGEX = "^[\\p{L} .'-]+$";
    EditText editTextName, editTextRollNo;
    TextView textViewTitle;
    Button btnAddStudent;
    ArrayList<Student> studentArrayList;
    Intent intent;


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
        editTextName = findViewById(R.id.editTextName);
        editTextRollNo = findViewById(R.id.editTextRollNo);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        textViewTitle = findViewById(R.id.title_screen);
    }

    /*
     * method manageIntent
     * To check intent and open activity in desired mode i.e. ADD , VIEW or EDIT
     */
    private void manageIntent() {
        intent = getIntent();
        studentArrayList = intent.getParcelableArrayListExtra(getString(R.string.extra_arraylist));
        final Student student = intent.getParcelableExtra(getString(R.string.extra_student));
        String code = Integer.toString(intent.getIntExtra(getString(R.string.extra_option), 404));
        if (intent.getIntExtra(getString(R.string.extra_option), Constants.ERROR_CODE) == Constants.ADD_STUDENT_INFO) {
            addStudent(studentArrayList);
        }
        if (intent.getIntExtra(getString(R.string.extra_option), Constants.ERROR_CODE) == Constants.VIEW_STUDENT_INFO) {
            viewStudent(student);
        }
        if (intent.getIntExtra(getString(R.string.extra_option), Constants.ERROR_CODE) == Constants.EDIT_STUDENT_INFO) {
            int position = intent.getIntExtra(getString(R.string.extra_position), -1);
            updateStudent(student, position, studentArrayList);
        }
    }

    /*
     * method addStudent
     * to add new Student
     * @param ArrayList<Student> studentArrayList
     */
    private void addStudent(final ArrayList<Student> studentArrayList) {

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("inside OnClick", "yes");

                int counter = 0;
                String studentName = editTextName.getText().toString().trim();
                String studentRollNo = editTextRollNo.getText().toString();
                Student student = new Student(studentName, studentRollNo);
                Log.d("inside add", studentName + " "+ studentRollNo + " "+student +" " + student.getStudentName() + " "+ student.getStudentRollNo());

                if (!validate()) {
                    Log.d("Validate", "Wrong");

                } else {
                    for (Student ItrStudent : studentArrayList) {
                        if (student.getStudentRollNo().equals(ItrStudent.getStudentRollNo())) {
                            editTextRollNo.setError(getString(R.string.error_roll_no));
                            counter = -1;
                        }
                    }
                    Intent returnIntent = new Intent();
                    if (counter != -1) {

                        returnIntent.putExtra(getString(R.string.extra_student), student);
                        Log.d("inside add", returnIntent.toString());
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

        editTextName.setFocusable(false);
        editTextRollNo.setFocusable(false);
        editTextName.setEnabled(false);
        editTextRollNo.setEnabled(false);
        editTextName.setText(student.getStudentName());
        editTextRollNo.setText(student.getStudentRollNo());
        btnAddStudent.setVisibility(View.INVISIBLE);
        textViewTitle.setText(getString(R.string.student_details));
    }

    /*
     * method updateStudent
     * to add new Student
     * @param Student student
     * @param int position
     * @param ArrayList<Student> studentArrayList
     */
    private void updateStudent(final Student student, final int position,
                               final ArrayList<Student> studentArrayList) {

        textViewTitle.setText(getString(R.string.update_title));
        btnAddStudent.setText(getString(R.string.update));
        editTextRollNo.setText(student.getStudentRollNo());
        editTextName.setText(student.getStudentName());

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = 0;
                String studentName = editTextName.getText().toString().trim();
                String studentRollNo = editTextRollNo.getText().toString();
                if (!validate()) {
                    Log.d("Validate", "Wrong");
                } else {
                    Student student = new Student(studentName, studentRollNo);
                    for (Student ItrStudent : studentArrayList) {

                        if (counter == position) {

                            counter++;
                            continue;
                        } else if (student.getStudentRollNo().equals(ItrStudent.getStudentRollNo())) {
                            editTextRollNo.setError(getString(R.string.error_roll_no));
                            counter = -1;

                            break;
                        }
                        counter++;
                    }
                    Intent returnIntent = new Intent();
                    if (counter != -1) {

                        returnIntent.putExtra(getString(R.string.extra_student), student);
                        returnIntent.putExtra(getString(R.string.extra_position), position);
                        setResult(Constants.EDIT_STUDENT_INFO, returnIntent);
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
    private boolean validate() {

        if (isValidName(editTextName)) {
            editTextName.setError(getString(R.string.error_name));
            if (isEmpty(editTextName)) {
                editTextName.setError(getString(R.string.error_name_requires));
            }
            return false;
        }

        if (isEmpty(editTextRollNo)) {
            editTextRollNo.setError(getString(R.string.error_rollno));
            return false;
        }
        return true;
    }

    /*
     * Method isEmpty - to check if input is null
     * @param EditText editText - input from fields
     * @return boolean true or false accordingly
     */
    public boolean isEmpty(final EditText editText) {
        CharSequence str = editText.getText().toString();
        return TextUtils.isEmpty(str);
    }

    /*
     * Method isValidName - to check if input is a valid name
     * @param EditText editText - input from field
     * @return boolean true or false accordingly
     */
    public boolean isValidName(final EditText editText) {
        return !editText.getText().toString().trim().matches(NAME_REGEX);
    }


}