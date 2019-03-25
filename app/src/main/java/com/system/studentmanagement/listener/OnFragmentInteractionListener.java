package com.system.studentmanagement.listener;

import android.os.Bundle;

import com.system.studentmanagement.model.Student;

import java.util.List;

public interface OnFragmentInteractionListener {


        /**
         * onChangeTab move the tabs from one tab to another
         * If the Tab First is Selected in that case Tab 2 will be opened and vice-versa
         */
        void onChangeTab();


        boolean onStudentDelete(Student student);

        List<Student> onRefreshStudentList();

        void onEditData(Bundle bundle);

        void onAddData(Bundle bundle);



}

