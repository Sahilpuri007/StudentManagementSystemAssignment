package com.system.studentmanagement.dbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.system.studentmanagement.model.Student;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "studentDatabase";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + Student.TABLE_NAME + "("
                + Student.COLUMN_ROLL_NUMBER + " TEXT PRIMARY KEY,"
                + Student.COLUMN_NAME + " TEXT)";
        Log.d("HERE", "on create");
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + Student.TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);

    }

    /**
     * Saves Student Data to Database
     *
     * @param student Student Object
     */
    public void addStudent(Student student) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(Student.COLUMN_ROLL_NUMBER, student.getRollNo());
        contentValues.put(Student.COLUMN_NAME, student.getName());
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(Student.TABLE_NAME, null, contentValues);
    }

    /**
     * Get All Students from Database
     *
     * @return students list ArrayList
     */
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> studentList = new ArrayList<>();
        String[] projection = {
                Student.COLUMN_NAME,
                Student.COLUMN_ROLL_NUMBER
        };
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Student.TABLE_NAME, projection,
                null, null,
                null, null, null);


        while (cursor.moveToNext()) {
            Student student = new Student();
            student.setName(cursor.getString(cursor.getColumnIndex(Student.COLUMN_NAME)));
            student.setRollNo(cursor.getString(cursor.getColumnIndex(Student.COLUMN_ROLL_NUMBER)));
            studentList.add(student);
        }

        cursor.close();
        db.close();
        return studentList;
    }

    /**
     * Get Student from Database with id
     *
     * @param id of Student
     * @return student Student
     */
    public Student getStudent(String id) {
        //Log.d("get cursor", id);
        // get readable database as we are not inserting anything
        String[] projection = {
                Student.COLUMN_NAME,
                Student.COLUMN_ROLL_NUMBER
        };
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Student.TABLE_NAME, projection,
                Student.COLUMN_ROLL_NUMBER + "=?", new String[]{id},
                null, null, null);


        Student student = new Student();
        while (cursor.moveToNext()) {

            //Log.d("get cursor", "getStudent: " + cursor.getString(cursor.getColumnIndex(Student.COLUMN_ROLL_NUMBER)));
            student.setName(cursor.getString(cursor.getColumnIndex(Student.COLUMN_NAME)));
            student.setRollNo(cursor.getString(cursor.getColumnIndex(Student.COLUMN_ROLL_NUMBER)));
        }

        cursor.close();
        db.close();

        return student;

    }

    /**
     * Update Student in Database
     *
     * @param oldRollNo of Student
     * @param student   new Student object
     */
    public void updateStudent(String oldRollNo, Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Student.COLUMN_ROLL_NUMBER, student.getRollNo());
        values.put(Student.COLUMN_NAME, student.getName());
        db.update(Student.TABLE_NAME, values, Student.COLUMN_ROLL_NUMBER + "=?", new String[]{oldRollNo});
        db.close();
    }

    /**
     * Delete Student in Database
     *
     * @param student Student object
     */
    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Student.TABLE_NAME, Student.COLUMN_ROLL_NUMBER + " = ?",
                new String[]{String.valueOf(student.getRollNo())});

        db.close();
    }

    /**
     * Delete all Students in Database
     */
    public void deleteAll() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Student.TABLE_NAME, null, null);
        db.close();
    }
}
