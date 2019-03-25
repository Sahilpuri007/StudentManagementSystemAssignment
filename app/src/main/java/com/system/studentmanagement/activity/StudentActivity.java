package com.system.studentmanagement.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.system.studentmanagement.R;
import com.system.studentmanagement.backgroundhandler.BackgroundAsync;
import com.system.studentmanagement.backgroundhandler.BackgroundIntentService;
import com.system.studentmanagement.backgroundhandler.BackgroundService;
import com.system.studentmanagement.dbmanager.DatabaseHelper;
import com.system.studentmanagement.fragment.AddStudentFragment;
import com.system.studentmanagement.listener.OnFragmentInteractionListener;
import com.system.studentmanagement.model.Student;
import com.system.studentmanagement.util.Constants;
import com.system.studentmanagement.util.Validator;

import java.util.ArrayList;
import java.util.List;


/*
 * @author Sahil Puri
 * StudentActivity
 * Activity which is used to Add,View and Edit Student Info
 */

public class StudentActivity extends AppCompatActivity implements OnFragmentInteractionListener {


    private Bundle bundle;
    private Intent intent;
    private AddStudentFragment studentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        init();

    }


    @Override
    protected void onStart() {
        super.onStart();
        Student student = bundle.getParcelable(Constants.EXTRA_STUDENT_OBJECT);
        studentFragment.viewStudent(student);
    }

    private void init() {

        bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        studentFragment = new AddStudentFragment();
        fragmentTransaction.add(R.id.fragment_editor, studentFragment, "");
        fragmentTransaction.commit();

    }

    @Override
    public void onChangeTab() {

    }

    @Override
    public boolean onStudentDelete(Student student) {
        return false;
    }

    @Override
    public List<Student> onRefreshStudentList() {

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<Student> students = databaseHelper.getAllStudents();
        return students;
    }

    @Override
    public void onEditData(Bundle bundle) {

    }

    @Override
    public void onAddData(Bundle bundle) {

    }
}