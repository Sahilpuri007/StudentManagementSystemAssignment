package com.system.studentmanagement.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.system.studentmanagement.R;
import com.system.studentmanagement.adapter.ViewPagerAdapter;
import com.system.studentmanagement.dbmanager.DatabaseHelper;
import com.system.studentmanagement.fragment.AddStudentFragment;
import com.system.studentmanagement.fragment.ShowStudentsFragment;
import com.system.studentmanagement.listener.OnFragmentInteractionListener;
import com.system.studentmanagement.model.Student;
import com.system.studentmanagement.util.Constants;

import java.util.ArrayList;

/*
 * @author Sahil Puri
 * ShowStudents Activity
 * The first screen which shows the logo and name of the app
 * It contains A recycler view , a add button , and various other options for user
 */

public class ShowStudentsActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static final int SHOW_FRAGMENT = 0;
    private static final int ADD_FRAGMENT = 1;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        setPageListener();
    }

    @Override
    public ArrayList<Student> onRefreshStudentList() {

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<Student> students = databaseHelper.getAllStudents();
        return students;
    }

    @Override
    public void onEditData(Bundle bundle) {
        String tag = "android:switcher:" + R.id.vpDisplay + ":" + ADD_FRAGMENT;
        AddStudentFragment addStudentFragment = (AddStudentFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (addStudentFragment != null) {
            addStudentFragment.manageMode(bundle);
            addStudentFragment.refreshStudentList();
        }

    }

    @Override
    public void onAddData(Bundle bundle) {
        String tag = "android:switcher:" + R.id.vpDisplay + ":" + ADD_FRAGMENT;
        AddStudentFragment addStudentFragment = (AddStudentFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (addStudentFragment != null) {
            addStudentFragment.manageMode(bundle);
            addStudentFragment.refreshStudentList();
        }
    }

    @Override
    public void addStudent(Student student, String oldRollNo, int position) {
        String tag = "android:switcher:" + R.id.vpDisplay + ":" + SHOW_FRAGMENT;
        ShowStudentsFragment showStudentsFragment = (ShowStudentsFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (showStudentsFragment != null) {
            if (oldRollNo == null) {
                showStudentsFragment.addStudentToList(student);
            } else {
                showStudentsFragment.addUpdatedStudentToList(student, position);
            }
        }
    }

    @Override
    public void onChangeTab() {
        if (viewPager.getCurrentItem() == SHOW_FRAGMENT) {
            viewPager.setCurrentItem(ADD_FRAGMENT);
        } else {
            viewPager.setCurrentItem(SHOW_FRAGMENT);
        }
    }

    @Override
    public void onBackPressed() {

        if (viewPager.getCurrentItem() >= 1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressLint("ResourceType")
    /*
     * method initViews to initialize views
     */
    private void initViews() {

        viewPager = findViewById(R.id.vpDisplay);
        TabLayout tabLayout = findViewById(R.id.tlShow);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor(getString(R.color.colorBlack)), Color.parseColor(getString(R.color.colorWhite)));


    }

    /**
     * method setPageListener to manage page state
     */
    private void setPageListener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {

                if (viewPager.getCurrentItem() == ADD_FRAGMENT) {
                    Toolbar toolbar = findViewById(R.id.toolbar);
                    toolbar.findViewById(R.id.swLayout).setVisibility(View.INVISIBLE);
                    toolbar.findViewById(R.id.ibSort).setVisibility(View.INVISIBLE);
                    toolbar.findViewById(R.id.ibDeleteAll).setVisibility(View.INVISIBLE);
                } else {
                    Toolbar toolbar = findViewById(R.id.toolbar);
                    toolbar.findViewById(R.id.swLayout).setVisibility(View.VISIBLE);
                    toolbar.findViewById(R.id.ibSort).setVisibility(View.VISIBLE);
                    toolbar.findViewById(R.id.ibDeleteAll).setVisibility(View.VISIBLE);
                    String tag = "android:switcher:" + R.id.vpDisplay + ":" + 1;
                    AddStudentFragment addStudentFragment = (AddStudentFragment) getSupportFragmentManager().findFragmentByTag(tag);
                    if (addStudentFragment != null) {
                        addStudentFragment.clearFields();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.EXTRA_OPTION, Constants.ADD_STUDENT_INFO);
                    if (addStudentFragment != null) {
                        addStudentFragment.manageMode(bundle);
                    }

                }
            }
        });

    }

    /**
     * method setupViewPager to setup page listener
     * @param viewPager viewPager
     */
    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ShowStudentsFragment(), "Student Details");
        adapter.addFragment(new AddStudentFragment(), "Add Student");

        viewPager.setAdapter(adapter);
    }

}


