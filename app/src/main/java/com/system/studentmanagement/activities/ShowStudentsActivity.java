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

import android.widget.Toolbar;

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
    private ImageButton sortMenu, deleteAll;
    private Switch layoutSwitch;
    private RelativeLayout emptyView;
    private Toolbar toolbar;
    private StudentListAdapter studentListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private PopupMenu dropDownMenu;
    private Context mContext;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);
        initComponents();
        manageIntent();
        buildRecyclerView();
        setAllListeners();
    }

    /*
     * method initComponents
     * To Initialize all views
     */
    private void initComponents() {
        btnAdd = findViewById(R.id.addButton);
        layoutSwitch = findViewById(R.id.layoutSwitch);
        emptyView = findViewById(R.id.emptyView);
        toolbar = findViewById(R.id.toolbar_add);
        sortMenu = findViewById(R.id.btnSort);
        deleteAll = findViewById(R.id.btnDeleteAll);
        emptyView = findViewById(R.id.emptyView);
        recyclerView = findViewById(R.id.recyclerView);
        dropDownMenu = new PopupMenu(this, sortMenu);
        dropDownMenu.getMenuInflater().inflate(R.menu.drop_down_sort_option, dropDownMenu.getMenu());
        mContext = this;
        studentListAdapter = new StudentListAdapter(studentArrayList);
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2);
    }

    /*
     * method manageIntent
     * To create and put extra
     */
    private void manageIntent() {
        intent = new Intent(ShowStudentsActivity.this, StudentActivity.class);
        intent.putParcelableArrayListExtra("arraylist", studentArrayList);
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
        sortMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.show();
            }
        });
        deleteAll.setOnClickListener(new View.OnClickListener() {
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
        btnView = mView.findViewById(R.id.btnView);
        btnEdit = mView.findViewById(R.id.btnEdit);
        btnDelete = mView.findViewById(R.id.btnDelete);

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
        intent.putExtra("studentObj", studentArrayList.get(position));
        intent.putExtra("Option", Constants.VIEW_STUDENT_INFO);
        startActivity(intent);
    }

    /*
     * method editMode
     * to open StudentActivity in edit mode
     * @param int position
     */
    private void editMode(final int position) {
        intent.putExtra("studentObj", studentArrayList.get(position));
        intent.putExtra("position", position);
        intent.putExtra("Option", Constants.EDIT_STUDENT_INFO);
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
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                studentArrayList.remove(position);
                studentListAdapter.notifyDataSetChanged();
                if (studentArrayList.size() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                }
                alertDialog.cancel();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Back", new DialogInterface
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
        intent.putExtra("Option", Constants.ADD_STUDENT_INFO);
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
            return gridLayoutManager;
        } else {
            return linearLayoutManager;
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
        }
        return false;
    }
    /*
     * method sortByName
     * To sort recyclerView items by Student Name
     */
    private void sortByName() {
        Collections.sort(studentArrayList, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getStudentName().compareToIgnoreCase(o2.getStudentName());
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
                return (Integer.parseInt(o1.getStudentRollNo())) -
                        (Integer.parseInt(o2.getStudentRollNo()));
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
        if(studentArrayList.size()==0){
            alertDialog.setTitle("Delete Student Info!");
            alertDialog.setMessage("No Students Found to Delete!");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface
                    .OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });
        }
        else {
            alertDialog.setTitle("Delete Student Info!");
            alertDialog.setMessage("Are you sure you want to delete all students");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface
                    .OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    studentArrayList.clear();
                    studentListAdapter.notifyDataSetChanged();
                    emptyView.setVisibility(View.VISIBLE);
                    alertDialog.cancel();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface
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

        Student student;
        if (resultCode == RESULT_OK && requestCode == Constants.ADD_STUDENT_INFO) {
            student = intent.getParcelableExtra("studentObj");
            addStudentToList(student);
        }
        if (resultCode == Constants.EDIT_STUDENT_INFO) {
            student = intent.getParcelableExtra("studentObj");
            addUpdatedStudentToList(student);


        }
    }
    /*
     * method addStudentToList
     * to add new Student to array list
     */
    private void addStudentToList(final Student student) {
        studentArrayList.add(student);
        if (studentArrayList.size() != 0) {
            emptyView.setVisibility(View.INVISIBLE);
        }
    }
    /*
     * method addUpdatedStudentToList
     * to  add updated Student to array list
     */
    private void addUpdatedStudentToList(final Student student) {
        int position = intent.getIntExtra("position", -1);
        studentArrayList.remove(position);
        studentArrayList.add(position, student);
        studentListAdapter.notifyDataSetChanged();
    }




}
