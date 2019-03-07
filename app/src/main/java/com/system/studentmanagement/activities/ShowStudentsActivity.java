package com.system.studentmanagement.activities;

import android.content.Context;
import android.content.DialogInterface;
import
        android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;

import android.widget.Toast;
import android.widget.Toolbar;

import com.system.studentmanagement.adapters.StudentListAdapter;
import com.system.studentmanagement.R;
import com.system.studentmanagement.model.Student;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ShowStudentsActivity extends AppCompatActivity {

    public static final int ADD_STUDENT_INFO = 101;
    public static final int VIEW_STUDENT_INFO = 102;
    public static final int EDIT_STUDENT_INFO = 103;
    static ArrayList<Student> studentArrayList = new ArrayList<Student>();
    RecyclerView recyclerView;
    Button addButton;
    ImageButton sortMenu,deleteAll;
    Switch layoutSwitch;
    RelativeLayout emptyView;
    Toolbar toolbar;
    StudentListAdapter studentListAdapter = new StudentListAdapter(studentArrayList);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
    PopupMenu dropDownMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);
        buildRecyclerView();
        addButton =  findViewById(R.id.addButton);
        layoutSwitch = findViewById(R.id.layoutSwitch);
        emptyView = findViewById(R.id.emptyView);
        toolbar =findViewById(R.id.toolbar_add);
        sortMenu = findViewById(R.id.btnSort);
        deleteAll = findViewById(R.id.btnDeleteAll);
        dropDownMenu = new PopupMenu(this, sortMenu);
        dropDownMenu.getMenuInflater().inflate(R.menu.option_menu,dropDownMenu.getMenu());

    }


    private void buildRecyclerView() {
        recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(studentListAdapter);

        studentListAdapter.setOnItemClickListener(new StudentListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowStudentsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_menu,null );
                Button btnView = mView.findViewById(R.id.btnView);
                Button btnEdit = mView.findViewById(R.id.btnEdit);
                final Button btnDelete = mView.findViewById(R.id.btnDelete);
                builder.setView(mView);
               final AlertDialog dialog = builder.create();

                btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       //Toast.makeText(ShowStudentsActivity.this,"Clicked View  "+studentArrayList.get(position).getStudentName() + " "+ studentArrayList.get(position).getStudentRollNo(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ShowStudentsActivity.this,StudentActivity.class);
                        intent.putExtra("studentObj",studentArrayList.get(position));
                        intent.putExtra("Option",VIEW_STUDENT_INFO);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(ShowStudentsActivity.this,"Clicked Edit",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ShowStudentsActivity.this,StudentActivity.class);
                        intent.putExtra("studentObj",studentArrayList.get(position));
                        intent.putExtra("position",position);
                        intent.putExtra("Option",EDIT_STUDENT_INFO);
                        startActivityForResult(intent,EDIT_STUDENT_INFO);
                        dialog.dismiss();
                    }
                });
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(ShowStudentsActivity.this,"Deleted!!!",Toast.LENGTH_SHORT).show();
                        final AlertDialog alertDialog = new AlertDialog.Builder(ShowStudentsActivity.this).create();
                        alertDialog.setTitle("Delete Student Info!");
                        alertDialog.setMessage("Are you sure you want to delete this student");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                studentArrayList.remove(position);
                                studentListAdapter.notifyDataSetChanged();
                                if(studentArrayList.size()==0) {
                                    emptyView.setVisibility(View.VISIBLE);
                                }
                                alertDialog.cancel();
                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.cancel();
                            }
                        });
                        alertDialog.show();
                        dialog.cancel();


                    }
                });
                dialog.show();




            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudentActivity();
            }
        });
        layoutSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    recyclerView.setLayoutManager(gridLayoutManager);
            }
            else {
                    recyclerView.setLayoutManager(linearLayoutManager);
                }

            }
        });
        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sort_name:
                       sortByName();
                        //Toast.makeText(ShowStudentsActivity.this,"Sort name",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.sort_roll_no:
                        //Toast.makeText(ShowStudentsActivity.this,"Sort Roll",Toast.LENGTH_SHORT).show();
                        sortByRollNo();
                        return true;
                }return false;
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
                final AlertDialog alertDialog = new AlertDialog.Builder(ShowStudentsActivity.this).create();
                alertDialog.setTitle("Delete Student Info!");
                alertDialog.setMessage("Are you sure you want to delete all students");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        studentArrayList.clear();
                        studentListAdapter.notifyDataSetChanged();
                        emptyView.setVisibility(View.VISIBLE);
                        alertDialog.cancel();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });
                alertDialog.show();

            }
        });


    }




    private void addStudentActivity() {
        Intent intent = new Intent(ShowStudentsActivity.this, StudentActivity.class);
        intent.putExtra("Option", ADD_STUDENT_INFO);
        startActivityForResult(intent,ADD_STUDENT_INFO);
    }


    protected  void onActivityResult(int requestCode,int resultCode,Intent intent){

        RelativeLayout emptyView = findViewById(R.id.emptyView);
        if(resultCode == RESULT_OK && requestCode == ADD_STUDENT_INFO){


         Student student = intent.getParcelableExtra("studentObj");

         studentArrayList.add(student);


            if(studentArrayList.size()!=0) {
                emptyView.setVisibility(View.INVISIBLE);
            }
        }
        if (resultCode == EDIT_STUDENT_INFO){
            Student student = intent.getParcelableExtra("studentObj");
            int position = intent.getIntExtra("position",-1);
            studentArrayList.remove(position);
            studentArrayList.add(position,student);
            studentListAdapter.notifyDataSetChanged();

        }
    }

    private void sortByName(){
        Collections.sort(studentArrayList, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getStudentName().compareToIgnoreCase(o2.getStudentName());
            }
        });
        studentListAdapter.notifyDataSetChanged();

    }

    private void sortByRollNo(){
        Collections.sort(studentArrayList, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return (Integer.parseInt(o1.getStudentRollNo()))-(Integer.parseInt(o2.getStudentRollNo()));
            }
        });
        studentListAdapter.notifyDataSetChanged();


    }
}
