package com.system.studentmanagement.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.system.studentmanagement.adapters.StudentListAdapter;
import com.system.studentmanagement.R;
import com.system.studentmanagement.model.Student;
import com.system.studentmanagement.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * @author Sahil Puri
 * ShowStudents Activity
 * The first screen which shows the logo and name of the app
 * It contains A recycler view , a add button , and various other options for user
 */

public class ShowStudentsActivity extends AppCompatActivity {

    //Arraylist of Student Type to store Students
    private ArrayList<Student> studentArrayList = new ArrayList<Student>();
    private RecyclerView recyclerView;
    private Button btnAdd, btnView, btnEdit, btnDelete;
    private ImageButton ibSortMenu, ibDeleteAll;
    private Switch layoutSwitch;
    private RelativeLayout rlEmptyView;
    private StudentListAdapter studentListAdapter;
    private PopupMenu dropDownMenu;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);

        initComponents();
        buildRecyclerView();
        setAllListeners();

    }

    /*
     * method initComponents
     * To Initialize all views
     */
    private void initComponents() {
        btnAdd = findViewById(R.id.show_student_btnAdd);
        layoutSwitch = findViewById(R.id.show_student_layoutSwitch);
        rlEmptyView = findViewById(R.id.show_student_rlemptyView);
        ibSortMenu = findViewById(R.id.show_student_ibSort);
        ibDeleteAll = findViewById(R.id.show_student_ibDeleteAll);
        recyclerView = findViewById(R.id.show_student_recyclerView);
        dropDownMenu = new PopupMenu(this, ibSortMenu);
        dropDownMenu.getMenuInflater().inflate(R.menu.drop_down_sort_option, dropDownMenu.getMenu());
        mContext = this;
        studentListAdapter = new StudentListAdapter(studentArrayList);
    }


    /*
     * method buildRecyclerView
     * To set layout  and adapter on RecyclerView
     */
    private void buildRecyclerView() {
        recyclerView.setLayoutManager(switchManager(false));
        recyclerView.setAdapter(studentListAdapter);
    }
    /*
     * method setAllListeners
     * To set listeners to various components
     */

    private void setAllListeners() {
        studentListAdapter.setOnItemClickListener(new StudentListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(final int position) {
                studentDialog(position);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });
        layoutSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                recyclerView.setLayoutManager(switchManager(isChecked));

            }
        });
        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return menuManger(item.getItemId());
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
                deleteAllDialog();
            }
        });
    }

    /*
     * method studentDialog
     * To show Dialog Menu when clicked on student item
     */
    private void studentDialog(final int position) {
        View mView = getLayoutInflater().inflate(R.layout.dialog_student_options, null);
        initAlertDialogView(mView);
        final AlertDialog dialog = alertDialogBuilder(mView);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMode(position);
                dialog.dismiss();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode(position);
                dialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMode(position);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /*
     * method initAlertDialogView
     * To initialize views of the dialog
     * @param View mView
     */
    private void initAlertDialogView(final View mView) {
        btnView = mView.findViewById(R.id.student_dialog_btnView);
        btnEdit = mView.findViewById(R.id.student_dialog_btnEdit);
        btnDelete = mView.findViewById(R.id.student_dialog_btnDelete);
    }

    /*
     * method alertDialogBuilder
     * to setView and create alertDialog
     * @param View mView
     */
    private AlertDialog alertDialogBuilder(final View mView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(mView);
        return builder.create();
    }

    /*
     * method viewMode
     * to open StudentActivity in view mode
     * @param int position
     */
    private void viewMode(final int position) {
        Intent intent = new Intent(ShowStudentsActivity.this, StudentActivity.class);
        intent.putParcelableArrayListExtra(Constants.EXTRA_ARRAY_LIST, studentArrayList);
        intent.putExtra(Constants.EXTRA_STUDENT_OBJECT, studentArrayList.get(position));
        intent.putExtra(Constants.EXTRA_OPTION, Constants.VIEW_STUDENT_INFO);
        startActivity(intent);
    }

    /*
     * method editMode
     * to open StudentActivity in edit mode
     * @param int position
     */
    private void editMode(final int position) {
        Intent intent = new Intent(ShowStudentsActivity.this, StudentActivity.class);
        intent.putParcelableArrayListExtra(Constants.EXTRA_ARRAY_LIST, studentArrayList);
        intent.putExtra(Constants.EXTRA_STUDENT_OBJECT, studentArrayList.get(position));
        intent.putExtra(Constants.EXTRA_POSITION, position);
        intent.putExtra(Constants.EXTRA_OPTION, Constants.EDIT_STUDENT_INFO);
        startActivityForResult(intent, Constants.EDIT_STUDENT_INFO);
    }

    /*
     * method deleteMode
     * to open StudentActivity in edit mode
     * @param int position
     */
    private void deleteMode(final int position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(ShowStudentsActivity.this)
                .create();
        alertDialog.setTitle(getString(R.string.delete_dialog_title));
        alertDialog.setMessage(getString(R.string.delete_dialog_message));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.confirm), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                studentArrayList.remove(position);
                studentListAdapter.notifyDataSetChanged();
                if (studentArrayList.size() == 0) {
                    rlEmptyView.setVisibility(View.VISIBLE);
                }
                alertDialog.cancel();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.back), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }
    /*
     * method addStudent
     * to open StudentActivity in add mode
     */

    private void addStudent() {
        Intent intent = new Intent(ShowStudentsActivity.this, StudentActivity.class);
        intent.putParcelableArrayListExtra(Constants.EXTRA_ARRAY_LIST, studentArrayList);
        intent.putExtra(Constants.EXTRA_OPTION, Constants.ADD_STUDENT_INFO);
        startActivityForResult(intent, Constants.ADD_STUDENT_INFO);
    }

    /*
     * method switchManager
     * To return Layout for recyclerView
     * @param boolean isChecked - from switch
     * @return layout for RecyclerView
     */
    private RecyclerView.LayoutManager switchManager(final boolean isChecked) {
        if (isChecked) {
            return  new GridLayoutManager(this, 2);
        } else {
            return  new LinearLayoutManager(this);
        }
    }

    /*
     * method menuManger
     * To get user choice of sort from menu
     * @param int itemId -  from dropdown menu
     * @return choice selected by user
     */
    private boolean menuManger(final int itemId) {
        switch (itemId) {
            case R.id.sort_name:
                sortByName();
                return true;
            case R.id.sort_roll_no:
                sortByRollNo();
                return true;
            default:
                return false;
        }
    }
    /*
     * method sortByName
     * To sort recyclerView items by Student Name
     */
    private void sortByName() {
        Collections.sort(studentArrayList, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        studentListAdapter.notifyDataSetChanged();

    }
    /*
     * method sortByRollNo
     * To sort recyclerView items by Student Roll No
     */
    private void sortByRollNo() {
        Collections.sort(studentArrayList, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return (Integer.parseInt(o1.getRollNo())) -
                        (Integer.parseInt(o2.getRollNo()));
            }
        });
        studentListAdapter.notifyDataSetChanged();
    }
    /*
     * method deleteAllDialog
     * To delete all items in Recycler View
     */
    private void deleteAllDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(ShowStudentsActivity.this)
                .create();
        alertDialog.setTitle(getString(R.string.dialog_title));
        if(studentArrayList.size()==0){
            alertDialog.setMessage(getString(R.string.dialog_empty_msg));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface
                    .OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });
        }
        else {
            alertDialog.setMessage(getString(R.string.dialog_msg));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface
                    .OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    studentArrayList.clear();
                    studentListAdapter.notifyDataSetChanged();
                    rlEmptyView.setVisibility(View.VISIBLE);
                    alertDialog.cancel();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface
                    .OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });
        }
        alertDialog.show();
    }

    /*
     * method onActivityResult
     * To delete all items in Recycler View
     * @param int requestCode
     * @param int resultCode
     * @param Intent intent
     */
    protected void onActivityResult(final int requestCode,final  int resultCode, final Intent intent) {

        if (studentArrayList.size() > 0) {
            rlEmptyView.setVisibility(View.INVISIBLE);
        }
        Student student;
        if (resultCode == RESULT_OK && requestCode == Constants.ADD_STUDENT_INFO) {
            student = intent.getParcelableExtra(Constants.EXTRA_STUDENT_OBJECT);
            addStudentToList(student);
        }
        if (resultCode == Constants.EDIT_STUDENT_INFO) {
            student = intent.getParcelableExtra(Constants.EXTRA_STUDENT_OBJECT);
            addUpdatedStudentToList(student,intent);


        }
    }
    /*
     * method addStudentToList
     * to add new Student to array list
     */
    private void addStudentToList(final Student student) {
        studentArrayList.add(student);
        studentListAdapter.notifyDataSetChanged();
        rlEmptyView.setVisibility(View.INVISIBLE);
    }
    /*
     * method addUpdatedStudentToList
     * to  add updated Student to array list
     */
    private void addUpdatedStudentToList(final Student student,final Intent intent) {
        int position = intent.getIntExtra(Constants.EXTRA_POSITION, -1);
        studentArrayList.remove(position);
        studentArrayList.add(position, student);
        studentListAdapter.notifyDataSetChanged();
    }




}
