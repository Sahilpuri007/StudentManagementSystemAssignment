package com.system.studentmanagement.listener;

import android.os.Bundle;

import com.system.studentmanagement.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * interface OnFragmentInteractionListener
 * to provide interactions between fragments
 */
public interface OnFragmentInteractionListener {


    void onChangeTab();

    ArrayList<Student> onRefreshStudentList();

    void onEditData(Bundle bundle);

    void onAddData(Bundle bundle);

    void addStudent(Student student,String oldRollNo,int position);

}

