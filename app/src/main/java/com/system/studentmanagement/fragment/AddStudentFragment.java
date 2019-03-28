package com.system.studentmanagement.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.system.studentmanagement.R;
import com.system.studentmanagement.backgroundhandler.BackgroundAsync;
import com.system.studentmanagement.backgroundhandler.BackgroundIntentService;
import com.system.studentmanagement.backgroundhandler.BackgroundService;
import com.system.studentmanagement.dbmanager.DatabaseHelper;
import com.system.studentmanagement.listener.OnFragmentInteractionListener;
import com.system.studentmanagement.model.Student;
import com.system.studentmanagement.util.Constants;
import com.system.studentmanagement.util.Validator;

import java.util.ArrayList;

public class AddStudentFragment extends Fragment {

    private EditText etName, etRollNo;
    private OnFragmentInteractionListener mListener;
    private Button btnAddStudent, btnAsync, btnService, btnIntentServ;
    private ArrayList<Student> studentArrayList = new ArrayList<>();
    private Context mContext;
    private DataReceiver dataReceiver = new DataReceiver();


    public AddStudentFragment() {
        // Required empty public constructor
    }

    public static AddStudentFragment newInstance() {

        return new AddStudentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter("WORKING");
        LocalBroadcastManager.getInstance(mContext).registerReceiver(dataReceiver,intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(dataReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student, container, false);

        initComponents(view);

        clearFields();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.mContext = context;
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * method refreshStudentList to refresh list
     */
    public void refreshStudentList() {

        studentArrayList = mListener.onRefreshStudentList();

    }

    /**
     * method clearFields to clear fields
     */
    public void clearFields() {
        etName.getText().clear();
        etRollNo.getText().clear();
    }


    /*
     * method manageMode
     * To check intent and open activity in desired mode i.e. ADD , VIEW or EDIT
     */
    public void manageMode(Bundle bundle) {


        //studentArrayList = bundle.getParcelableArrayList(Constants.EXTRA_ARRAY_LIST);
        final Student student = bundle.getParcelable(Constants.EXTRA_STUDENT_OBJECT);
        if (bundle.getInt(Constants.EXTRA_OPTION, Constants.ERROR_CODE) == Constants.ADD_STUDENT_INFO) {
            addMode();
        } else if (bundle.getInt(Constants.EXTRA_OPTION, Constants.ERROR_CODE) == Constants.EDIT_STUDENT_INFO) {
            int position = bundle.getInt(Constants.EXTRA_POSITION, -1);
            updateMode(student, position, studentArrayList);
        }

    }

    /*
     * method viewStudent
     * to view Student
     * @param Student student
     */
    public void viewStudent(final Student student) {

        etName.setFocusable(false);
        etRollNo.setFocusable(false);
        etName.setEnabled(false);
        etRollNo.setEnabled(false);
        etName.setText(student.getName());
        etRollNo.setText(student.getRollNo());
        btnAddStudent.setVisibility(View.INVISIBLE);
        // tvTitle.setText(getString(R.string.student_details));
    }

    /**
     *  method initComponents
     *  To Initialize all views
     * @param view view of fragment
     */
    private void initComponents(View view) {
        etName = view.findViewById(R.id.etName);
        etRollNo = view.findViewById(R.id.etRollNo);
        btnAddStudent = view.findViewById(R.id.btnAddStudent);
        studentArrayList = mListener.onRefreshStudentList();


    }


    /*
     * method addMode
     * to add new Student
     */
    private void addMode() {
        etName.requestFocus();
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int counter = 0;
                String studentName = etName.getText().toString().trim();
                String studentRollNo = etRollNo.getText().toString();
                Student student = new Student(studentName, studentRollNo);

                if (!validate()) {
                    Log.d("Validate", "Wrong");

                } else {
                    Log.d("here", "onClick: " + studentArrayList.size());
                    if (!(studentArrayList.size() == 0)) {
                        for (Student ItrStudent : studentArrayList) {
                            if (student.getRollNo().equals(ItrStudent.getRollNo())) {
                                etRollNo.setError(getString(R.string.error_roll_no));
                                counter = -1;
                            }
                        }
                    }


                    if (counter != -1) {
                        etName.setEnabled(false);
                        etRollNo.setEnabled(false);
                        openOptionsDialog(Constants.ADD_STUDENT_INFO, student, null, -1);
                        etName.setEnabled(true);
                        etRollNo.setEnabled(true);

                    }
                }
            }

        });

    }


    /*
     * method updateMode
     * to add new Student
     * @param Student student
     * @param int position
     * @param ArrayList<Student> studentArrayList
     */
    private void updateMode(final Student student, final int position,
                            final ArrayList<Student> studentArrayList) {
        final String oldRollNo = student.getRollNo();
        Log.d("update", "updateMode: old rolno " + oldRollNo);
        etRollNo.setText(student.getRollNo());
        etName.setText(student.getName());
        etName.requestFocus();

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("here", "onClick: update");
                int counter = 0;
                String studentName = etName.getText().toString().trim();
                String studentRollNo = etRollNo.getText().toString();
                Student newStudent;
                Log.d("here", "onClick: here " + studentName + " " + studentRollNo + validate());
                if (validate()) {

                    newStudent = new Student(studentName, studentRollNo);
                    Log.d("update", "updateMode: new " + newStudent.getName() + " " + newStudent.getRollNo());
                    for (Student ItrStudent : studentArrayList) {

                        if (counter == position) {

                            counter++;
                            continue;
                        } else if (newStudent.getRollNo().equals(ItrStudent.getRollNo())) {
                            etRollNo.setError(getString(R.string.error_roll_no));
                            counter = -1;

                            break;
                        }
                        counter++;
                    }


                    if (counter != -1) {
                        Log.d("here", "onClick;update");
                        etName.setEnabled(false);
                        etRollNo.setEnabled(false);
                        openOptionsDialog(Constants.EDIT_STUDENT_INFO, newStudent, oldRollNo, position);
                        etName.setEnabled(true);
                        etRollNo.setEnabled(true);

                    }
                } else {
                    Log.d("here", "onClick: validate wrong");
                }
            }
        });
    }

    /**
     * for options of database interactions
     *
     * @param option    type of operation
     * @param student   Student object
     * @param oldRollNo oldRollNo of student
     */
    private void openOptionsDialog(final int option, final Student student, final String oldRollNo, final int position) {
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_options, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(mView).create();
        initAlertDialogView(mView);
        btnAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskAsync(option, student, oldRollNo, position);
                dialog.dismiss();
            }
        });
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskService(option, student, oldRollNo, position);
                dialog.dismiss();
            }
        });
        btnIntentServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskIntentService(option, student, oldRollNo, position);
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    /**
     * to execute async operation
     *
     * @param option    type of operation
     * @param student   Student object
     * @param oldRollNo oldRollNo of student
     */
    private void taskAsync(int option, Student student, String oldRollNo, int position) {

        BackgroundAsync taskAsync = new BackgroundAsync(mContext);
        taskAsync.execute(option, student, oldRollNo);
        mListener.onChangeTab();
        mListener.addStudent(student, oldRollNo, position);

    }

    /**
     * to execute service operation
     *
     * @param option    type of operation
     * @param student   Student object
     * @param oldRollNo oldRollNo of student
     */
    private void taskService(int option, Student student, String oldRollNo, int position) {
        Log.d("INSIDE", "Service " + option);
        Intent service = new Intent(mContext, BackgroundService.class);
        service.putExtra(Constants.EXTRA_STUDENT_OBJECT, student);
        service.putExtra(Constants.EXTRA_OPTION, option);
        service.putExtra(Constants.EXTRA_OLD_ROLL_NO, oldRollNo);
        mContext.startService(service);
        mListener.onChangeTab();
        mListener.addStudent(student, oldRollNo, position);
        //finish();
    }

    /**
     * to execute intent service operation
     *
     * @param option    type of operation
     * @param student   Student object
     * @param oldRollNo oldRollNo of student
     */
    private void taskIntentService(int option, Student student, String oldRollNo, int position) {
        Log.d("INSIDE", "Intent Service " + option);
        Intent intentService = new Intent(mContext, BackgroundIntentService.class);
        intentService.putExtra(Constants.EXTRA_STUDENT_OBJECT, student);
        intentService.putExtra(Constants.EXTRA_OPTION, option);
        intentService.putExtra(Constants.EXTRA_OLD_ROLL_NO, oldRollNo);
        mContext.startService(intentService);
        mListener.onChangeTab();
        mListener.addStudent(student, oldRollNo, position);
        //finish();

    }

    /*
     * method initDialogView
     * To initialize views of the dialog
     * @param View mView
     */
    private void initAlertDialogView(final View mView) {
        btnAsync = mView.findViewById(R.id.add_dialog_btnAsync);
        btnService = mView.findViewById(R.id.add_dialog_btnService);
        btnIntentServ = mView.findViewById(R.id.add_dialog_btnIntentServ);
    }


    /*
     * method validate
     * to validate input fields
     * @return boolean true or false according to input
     */
    private boolean validate() {

        Validator validator = new Validator();
        if (validator.isEmpty(etName)) {
            etName.setError(getString(R.string.error_name_requires));
            return false;
        } else if (validator.isValidName(etName)) {
            etName.setError(getString(R.string.error_name));
            return false;
        } else if (validator.isEmpty(etRollNo)) {
            etRollNo.setError(getString(R.string.error_rollno));
            return false;
        } else if (validator.isValidRollNo(etRollNo)) {
            etRollNo.setError(getString(R.string.error_valid_roll));
            return false;
        }

        return true;
    }

    public class DataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"Broadcast Reciver",Toast.LENGTH_SHORT).show();

        }
    }


}

