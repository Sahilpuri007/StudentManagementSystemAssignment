package com.system.studentmanagement.activity;

import android.annotation.SuppressLint;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.RelativeLayout;

import com.system.studentmanagement.R;
import com.system.studentmanagement.adapter.ViewPagerAdapter;
import com.system.studentmanagement.dbmanager.DatabaseHelper;
import com.system.studentmanagement.fragment.AddStudentFragment;
import com.system.studentmanagement.fragment.ShowStudentsFragment;
import com.system.studentmanagement.listener.OnFragmentInteractionListener;
import com.system.studentmanagement.model.Student;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Sahil Puri
 * ShowStudents Activity
 * The first screen which shows the logo and name of the app
 * It contains A recycler view , a add button , and various other options for user
 */

public class ShowStudentsActivity extends AppCompatActivity implements OnFragmentInteractionListener {


    private ArrayList<Student> studentArrayList;
    private RelativeLayout rlEmptyView;
    private ViewPager viewPager;
    private DatabaseHelper dbHelper;
    private Toolbar toolbar;



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        rlEmptyView = findViewById(R.id.rlEmptyView);

        viewPager = findViewById(R.id.vpDisplay);

        TabLayout tabLayout = findViewById(R.id.tlShow);

        setupViewPager(viewPager);


        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(Color.parseColor(getString(R.color.colorBlack)),Color.parseColor(getString(R.color.colorWhite)));

        studentArrayList = new ArrayList<>();


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
 }

            @Override
            public void onPageScrollStateChanged(int i) {

                if(viewPager.getCurrentItem()==1) {
                    Toolbar toolbar = findViewById(R.id.toolbar);
                    toolbar.findViewById(R.id.swLayout).setVisibility(View.INVISIBLE);
                    toolbar.findViewById(R.id.ibSort).setVisibility(View.INVISIBLE);
                    toolbar.findViewById(R.id.ibDeleteAll).setVisibility(View.INVISIBLE);
                }
                else{
                    Toolbar toolbar = findViewById(R.id.toolbar);
                    toolbar.findViewById(R.id.swLayout).setVisibility(View.VISIBLE);
                    toolbar.findViewById(R.id.ibSort).setVisibility(View.VISIBLE);
                    toolbar.findViewById(R.id.ibDeleteAll).setVisibility(View.VISIBLE);
                }
            }
        });

    }
    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ShowStudentsFragment(),"Student Details");
        adapter.addFragment(new AddStudentFragment(),"Add Student");

        viewPager.setAdapter(adapter);



    }

    public void changeTab(){

        if(viewPager.getCurrentItem()==0) {
            viewPager.setCurrentItem(1);
        }
        else if(viewPager.getCurrentItem()==1) {
            viewPager.setCurrentItem(0);

        }
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
        String tag = "android:switcher:" + R.id.vpDisplay + ":" + 1;
        AddStudentFragment addStudentFragment = (AddStudentFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (addStudentFragment != null) {
            addStudentFragment.manageMode(bundle);
        }

    }

    @Override
    public void onAddData(Bundle bundle) {
        String tag = "android:switcher:" + R.id.vpDisplay + ":" + 1;
        AddStudentFragment addStudentFragment = (AddStudentFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (addStudentFragment != null) {
            addStudentFragment.manageMode(bundle);
        }
    }


    @Override
    public void onChangeTab() {
        if (viewPager.getCurrentItem() == 0) {
            viewPager.setCurrentItem(1);
        } else {
            viewPager.setCurrentItem(0);
        }
    }
}

