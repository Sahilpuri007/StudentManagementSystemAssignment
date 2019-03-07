package com.system.studentmanagement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;


import com.system.studentmanagement.R;
import com.system.studentmanagement.model.Student;



public class StudentActivity extends AppCompatActivity {


    EditText editTextName, editTextRollNo;
     TextView textViewTitle;
     Button btnAddStudent;
    public static final int VIEW_STUDENT_INFO = 102;
    private static final int EDIT_STUDENT_INFO = 103 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        editTextName = findViewById(R.id.editTextName);
        editTextRollNo = findViewById(R.id.editTextRollNo);
        btnAddStudent =  findViewById(R.id.btnAddStudent);
        textViewTitle = findViewById(R.id.title_screen);

        if(btnAddStudent.getText().equals("Add Student")) {
            btnAddStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validate()) {
                        Log.d("Validate", "Wrong");
                    } else {
                        addStudent();
                    }
                }
            });
        }

        Intent intent = getIntent();
        if(intent != null) {
            final Student student = intent.getParcelableExtra("studentObj");
            if (intent.getIntExtra("Option",404) == VIEW_STUDENT_INFO) {
                viewStudent(student);
            }
            if(intent.getIntExtra("Option",404) == EDIT_STUDENT_INFO){
                final Student updatedStudent = updateStudent(student);
                btnAddStudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("click","update"+ updatedStudent.getStudentName());
                        if (!validate()) {
                            Log.d("Validate", "Wrong");
                        } else {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("studentObj",updatedStudent);
                            setResult(EDIT_STUDENT_INFO,returnIntent);
                            finish();
                        }
                        }

                });

        }}

    }


    private void addStudent() {
        int counter=0;

        String studentName = editTextName.getText().toString().trim();
        String studentRollNo = editTextRollNo.getText().toString();

        Student student = new Student(studentName,studentRollNo);
        for(Student ItrStudent:ShowStudentsActivity.studentArrayList){
        if (student.getStudentRollNo().equals(ItrStudent.getStudentRollNo())) {
           editTextRollNo.setError("Not Unique Roll No.");
            counter++;
        }
      }
        Intent returnIntent = new Intent();
        if(counter==0){

        returnIntent.putExtra("studentObj",student);
            setResult(RESULT_OK,returnIntent);
            finish();}
    }

    private void viewStudent(Student student){

        editTextName.setFocusable(false);
        editTextRollNo.setFocusable(false);
        editTextName.setEnabled(false);
        editTextRollNo.setEnabled(false);
        editTextName.setText(student.getStudentName());
        editTextRollNo.setText(student.getStudentRollNo());
        btnAddStudent.setVisibility(View.INVISIBLE);
        textViewTitle.setText("Student Details");
    }

    private Student updateStudent(Student student) {
        int counter = 0;
        textViewTitle.setText("Update Student Details");
        btnAddStudent.setText("Update");
        editTextRollNo.setText(student.getStudentRollNo());
        editTextName.setText(student.getStudentName());

        /*String studentName = editTextName.getText().toString().trim();
        String studentRollNo = editTextRollNo.getText().toString();
        Student updatedstudent = new Student(studentName, studentRollNo);
        for (Student ItrStudent : ShowStudentsActivity.studentArrayList) {
            if (updatedstudent.getStudentRollNo().equals(ItrStudent.getStudentRollNo())) {
                editTextRollNo.setError("Not Unique Roll No.");
                counter++;
            }

*/


                return new Student(editTextName.getText().toString(),editTextRollNo.getEditableText().toString());


        }



    private boolean validate(){

        if(isName(editTextName)){
            editTextName.setError("Enter a Valid Name");
            if(isEmpty(editTextName)){
                editTextName.setError("Student Name is required");

            }
            return false;
        }



        if (isEmpty(editTextRollNo)) {
            editTextRollNo.setError("Student Roll.No is required");
            return false;
        }

        return true;



    }

    private boolean isName(EditText editTextName) {

            return !editTextName.getText().toString().trim().matches("^[\\p{L} .'-]+$");

    }

    private boolean isEmpty (EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);

    }



}