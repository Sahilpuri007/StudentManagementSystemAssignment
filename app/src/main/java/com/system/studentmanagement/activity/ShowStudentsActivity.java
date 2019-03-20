package com.system.studentmanagement.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.system.studentmanagement.adapter.StudentListAdapter;
import com.system.studentmanagement.R;
import com.system.studentmanagement.adapter.ViewPagerAdapter;
import com.system.studentmanagement.backgroundhandler.BackgroundAsync;
import com.system.studentmanagement.backgroundhandler.BackgroundIntentService;
import com.system.studentmanagement.backgroundhandler.BackgroundService;
import com.system.studentmanagement.dbmanager.DatabaseHelper;
import com.system.studentmanagement.fragment.AddStudentFragment;
import com.system.studentmanagement.fragment.ShowStudentsFragment;
import com.system.studentmanagement.model.Student;
import com.system.studentmanagement.touchlistener.RecyclerTouchListener;
import com.system.studentmanagement.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;

/*
 * @author Sahil Puri
 * ShowStudents Activity
 * The first screen which shows the logo and name of the app
 * It contains A recycler view , a add button , and various other options for user
 */

public class ShowStudentsActivity extends AppCompatActivity  {


    public ArrayList<Student> studentArrayList = new ArrayList<>();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageButton ibSortMenu, ibDeleteAll;
    private Switch swLayout;
    private PopupMenu dropDownMenu;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);

        swLayout = findViewById(R.id.swLayout);
        ibSortMenu = findViewById(R.id.ibSort);
        ibDeleteAll = findViewById(R.id.ibDeleteAll);
        dropDownMenu = new PopupMenu(this, ibSortMenu);
        dropDownMenu.getMenuInflater().inflate(R.menu.drop_down_sort_option, dropDownMenu.getMenu());
        viewPager = findViewById(R.id.vpDisplay);
        tabLayout = findViewById(R.id.tlShow);
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


        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor(getString(R.color.colorBlack)),Color.parseColor(getString(R.color.colorWhite)));

        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //return menuManger(item.getItemId());
                return false;
            }
        });
        ibSortMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.show();
            }
        });
        ibDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //deleteAllDialog();
            }
        });

        swLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // rvStudentList.setLayoutManager(switchManager(isChecked));

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
    }


    public void addDataToShow() {

        getSupportFragmentManager().findFragmentByTag("Add Student");
    }
}
