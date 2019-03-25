package com.system.studentmanagement.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.system.studentmanagement.R;
import com.system.studentmanagement.activity.ShowStudentsActivity;
import com.system.studentmanagement.activity.StudentActivity;
import com.system.studentmanagement.adapter.StudentListAdapter;
import com.system.studentmanagement.backgroundhandler.BackgroundAsync;
import com.system.studentmanagement.backgroundhandler.BackgroundIntentService;
import com.system.studentmanagement.backgroundhandler.BackgroundService;
import com.system.studentmanagement.dbmanager.DatabaseHelper;
import com.system.studentmanagement.listener.OnFragmentInteractionListener;
import com.system.studentmanagement.model.Student;
import com.system.studentmanagement.listener.RecyclerTouchListener;
import com.system.studentmanagement.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ShowStudentsFragment extends Fragment implements RecyclerTouchListener {

    //Arraylist of Student Type to store Students
    private ArrayList<Student> studentArrayList = new ArrayList<Student>();
    private RecyclerView rvStudentList;
    private Button btnAdd, btnView, btnEdit, btnDelete, btnAsync, btnService, btnIntentServ;
    private RelativeLayout rlEmptyView;
    private OnFragmentInteractionListener mListener;
    private StudentListAdapter studentListAdapter;
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private ImageButton ibSortMenu, ibDeleteAll;
    private Switch swLayout;
    private PopupMenu dropDownMenu;


    public ShowStudentsFragment() {

    }

    private static ShowStudentsFragment newInstance() {
        ShowStudentsFragment fragment = new ShowStudentsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        View view = inflater.inflate(R.layout.fragment_show_students, container, false);

        initComponents(view);
        buildRecyclerView();
        setAllListeners();

        refreshStudentList();
        return view;

    }

    public void refreshStudentList() {
        List<Student> students = mListener.onRefreshStudentList();
        studentArrayList.clear();

        for (int i = 0; i < students.size(); i++) {
            Student student = new Student(students.get(i).getName(), students.get(i).getRollNo());
            studentArrayList.add(student);
        }
        if (studentArrayList.size() == 0) {
            rlEmptyView.setVisibility(View.VISIBLE);
        } else {
            rlEmptyView.setVisibility(View.GONE);
        }

        studentListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(int position) {

        studentDialog(position);

    }


    /*
     * method initComponents
     * To Initialize all views
     */
    private void initComponents(View view) {
        btnAdd = view.findViewById(R.id.btnAdd);
        swLayout = getActivity().findViewById(R.id.swLayout);
        rlEmptyView = view.findViewById(R.id.rlEmptyView);
        ibSortMenu = getActivity().findViewById(R.id.ibSort);
        ibDeleteAll = getActivity().findViewById(R.id.ibDeleteAll);
        rvStudentList = view.findViewById(R.id.rvStudentList);
        dropDownMenu = new PopupMenu(getActivity(), ibSortMenu);
        dropDownMenu.getMenuInflater().inflate(R.menu.drop_down_sort_option, dropDownMenu.getMenu());
        studentListAdapter = new StudentListAdapter(studentArrayList, this);
        databaseHelper = new DatabaseHelper(mContext);
        studentArrayList.addAll(databaseHelper.getAllStudents());
        if (studentArrayList.size() > 0) {
            rlEmptyView.setVisibility(View.INVISIBLE);
        }

    }


    /*
     * method buildRecyclerView
     * To set layout  and adapter on RecyclerView
     */
    private void buildRecyclerView() {
        rvStudentList.setLayoutManager(switchManager(false));
        rvStudentList.setAdapter(studentListAdapter);
    }




    /*
     * method setAllListeners
     * To set listeners to various components
     */

    private void setAllListeners() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
                mListener.onChangeTab();

            }
        });
        swLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rvStudentList.setLayoutManager(switchManager(isChecked));

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
        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(mView).create();
        initAlertDialogView(mView);


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
     * method viewMode
     * to open StudentActivity in view mode
     * @param int position
     */
    private void viewMode(final int position) {
        Intent intent = new Intent(mContext, StudentActivity.class);
        intent.putExtra(Constants.EXTRA_STUDENT_OBJECT, studentArrayList.get(position));
        startActivity(intent);
    }

    /*
     * method editMode
     * to open StudentActivity in edit mode
     * @param int position
     */
    private void editMode(final int position) {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.EXTRA_ARRAY_LIST, studentArrayList);
        bundle.putParcelable(Constants.EXTRA_STUDENT_OBJECT, studentArrayList.get(position));
        bundle.putInt(Constants.EXTRA_POSITION, position);
        bundle.putInt(Constants.EXTRA_OPTION, Constants.EDIT_STUDENT_INFO);
        mListener.onEditData(bundle);
        mListener.onChangeTab();
    }

    /*
     * method deleteMode
     * to open StudentActivity in edit mode
     * @param int position
     */
    private void deleteMode(final int position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .create();
        alertDialog.setTitle(getString(R.string.delete_dialog_title));
        alertDialog.setMessage(getString(R.string.delete_dialog_message));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.confirm), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteHandler(studentArrayList.get(position));

//              databaseHelper.deleteStudent(studentArrayList.get(position));
                studentListAdapter.notifyDataSetChanged();
                if (studentArrayList.size() == 0) {
                    rlEmptyView.setVisibility(View.VISIBLE);
                }

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

    @Override
    public void onPause() {
        super.onPause();
        Log.d("asasasasa", "paause");
    }

    /**
     * to delete student(s) from database
     * three choices are given
     * 1.Async
     * 2.Service
     * 3.Intent Service
     *
     * @param student
     */


    private void deleteHandler(final Student student) {
        View mViewDialog = getLayoutInflater().inflate(R.layout.dialog_add_options, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(mViewDialog).create();
        initDialogView(mViewDialog);

        btnAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundAsync taskAsync = new BackgroundAsync(mContext);
                taskAsync.execute(Constants.DELETE_STUDENT_INFO, student);
                if (student != null) {
                    studentArrayList.remove(student);
                    if (studentArrayList.size() == 0) {
                        rlEmptyView.setVisibility(View.VISIBLE);
                    }

                } else {
                    studentArrayList.clear();
                    rlEmptyView.setVisibility(View.VISIBLE);
                }
                refreshStudentList();
                studentListAdapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent(mContext, BackgroundService.class);
                service.putExtra(Constants.EXTRA_STUDENT_OBJECT, student);
                service.putExtra(Constants.EXTRA_OPTION, Constants.DELETE_STUDENT_INFO);
                mContext.startService(service);
                if (student != null) {
                    studentArrayList.remove(student);
                    if (studentArrayList.size() == 0) {
                        rlEmptyView.setVisibility(View.VISIBLE);
                    }
                } else {
                    studentArrayList.clear();
                    rlEmptyView.setVisibility(View.VISIBLE);
                }
                refreshStudentList();
                studentListAdapter.notifyDataSetChanged();
                dialog.dismiss();


            }
        });
        btnIntentServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentService = new Intent(mContext, BackgroundIntentService.class);
                intentService.putExtra(Constants.EXTRA_STUDENT_OBJECT, student);
                intentService.putExtra(Constants.EXTRA_OPTION, Constants.DELETE_STUDENT_INFO);
                mContext.startService(intentService);
                if (student != null) {
                    studentArrayList.remove(student);
                    if (studentArrayList.size() == 0) {
                        rlEmptyView.setVisibility(View.VISIBLE);
                    }
                } else {
                    studentArrayList.clear();
                    rlEmptyView.setVisibility(View.VISIBLE);

                }
                refreshStudentList();
                studentListAdapter.notifyDataSetChanged();
                dialog.dismiss();


            }
        });

        dialog.show();

    }

    /*
     * method initDialogView
     * To initialize views of the dialog
     * @param View mView
     */
    private void initDialogView(final View mView) {
        btnAsync = mView.findViewById(R.id.add_dialog_btnAsync);
        btnService = mView.findViewById(R.id.add_dialog_btnService);
        btnIntentServ = mView.findViewById(R.id.add_dialog_btnIntentServ);
    }


    /*
     * method switchManager
     * To return Layout for rvStudentList
     * @param boolean isChecked - from switch
     * @return layout for RecyclerView
     */
    private RecyclerView.LayoutManager switchManager(final boolean isChecked) {
        if (isChecked) {
            return new GridLayoutManager(mContext, 2);
        } else {
            return new LinearLayoutManager(mContext);
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
     * To sort rvStudentList items by Student Name
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
     * To sort rvStudentList items by Student Roll No
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
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .create();
        alertDialog.setTitle(getString(R.string.dialog_title));
        if (studentArrayList.size() == 0) {
            alertDialog.setMessage(getString(R.string.dialog_empty_msg));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface
                    .OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });
        } else {
            alertDialog.setMessage(getString(R.string.dialog_msg));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface
                    .OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    deleteHandler(null);

                    /*studentArrayList.clear();
                    databaseHelper.getWritableDatabase();
                    databaseHelper.deleteAll();*/
                    refreshStudentList();
                    studentListAdapter.notifyDataSetChanged();

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
     * method addStudent
     * to open StudentActivity in add mode
     */

    private void addStudent() {
        final Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_OPTION, Constants.ADD_STUDENT_INFO);
        mListener.onAddData(bundle);
    }

    /**
     * method onActivityResult
     * To delete all items in Recycler View
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {


        Student student;
        if (resultCode == RESULT_OK && requestCode == Constants.ADD_STUDENT_INFO) {
            String id = intent.getStringExtra("id");
            databaseHelper.getWritableDatabase();
            Log.d("id", "" + id);
            student = databaseHelper.getStudent(id);
            addStudentToList(student);
        }
        if (resultCode == Constants.EDIT_STUDENT_INFO) {
            String id = intent.getStringExtra("updated");
            databaseHelper.getWritableDatabase();
            Log.d("updated", "" + id);
            student = databaseHelper.getStudent(id);
            addUpdatedStudentToList(student, intent);


        }
    }

    /**
     * method addStudentToList
     * to add new Student to array list
     *
     * @param student
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
    private void addUpdatedStudentToList(final Student student, final Intent intent) {
        int position = intent.getIntExtra(Constants.EXTRA_POSITION, -1);
        studentArrayList.remove(position);
        studentArrayList.add(position, student);
        studentListAdapter.notifyDataSetChanged();
    }


}
