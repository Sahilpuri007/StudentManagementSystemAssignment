package com.system.studentmanagement.dbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.system.studentmanagement.model.Student;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "studentDatabase";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("HERE","on create");
        db.execSQL(Student.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + Student.TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);

    }

    public void  addStudent(Student student) {
        Log.d("HERE","in add");
        ContentValues contentValues = new ContentValues();
        contentValues.put(Student.COLUMN_ROLL_NUMBER, student.getRollNo());
        contentValues.put(Student.COLUMN_NAME, student.getName());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Student.TABLE_NAME, null, contentValues);
    }

    public ArrayList<Student> getAllStudents()
    {
        ArrayList<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Student.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);


            while (cursor.moveToNext()){
                Student student = new Student();
                student.setName(cursor.getString(cursor.getColumnIndex(Student.COLUMN_NAME)));
                student.setRollNo(cursor.getString(cursor.getColumnIndex(Student.COLUMN_ROLL_NUMBER)));
                studentList.add(student);
            }

        cursor.close();
        db.close();
        return studentList;
    }
    public Student getStudent(String id) {
        Log.d("get cursor",id);
                // get readable database as we are not inserting anything
        String selectQuery = "SELECT * FROM " + Student.TABLE_NAME + " WHERE " + Student.COLUMN_ROLL_NUMBER + "= '" + id+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        Student student = new Student();
        while (cursor.moveToNext()){

            Log.d("get cursor", "getStudent: "+ cursor.getString(cursor.getColumnIndex(Student.COLUMN_ROLL_NUMBER)));

            student.setName(cursor.getString(cursor.getColumnIndex(Student.COLUMN_NAME)));
            student.setRollNo(cursor.getString(cursor.getColumnIndex(Student.COLUMN_ROLL_NUMBER)));
        }

        cursor.close();
        db.close();

        return student;

    }
    /*public int getStudentCount() {
        String countQuery = "SELECT  * FROM " + Student.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }*/
    public void updateStudent(String oldRollNo, Student student) {
        Log.d("HERE","in update " + oldRollNo);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Student.COLUMN_ROLL_NUMBER, student.getRollNo());
        values.put(Student.COLUMN_NAME, student.getName());
        String updateQuery = "UPDATE " + Student.TABLE_NAME + " SET " + Student.COLUMN_ROLL_NUMBER + " = " +" '"+student.getRollNo()+" ' , "+
                Student.COLUMN_NAME + " = " +"'"+student.getName()+"'"+ " WHERE "+ Student.COLUMN_ROLL_NUMBER+ " = '" + oldRollNo+"'" ;

        db.execSQL(updateQuery);
        db.close();
    }

    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Student.TABLE_NAME, Student.COLUMN_ROLL_NUMBER + " = ?",
                new String[]{String.valueOf(student.getRollNo())});

        db.close();
    }

    public void deleteAll() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Student.TABLE_NAME,null,null);
        db.close();
    }
}
