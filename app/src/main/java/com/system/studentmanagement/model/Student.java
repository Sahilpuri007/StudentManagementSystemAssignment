package com.system.studentmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * @author Sahil Puri
 * Student class to hold details of the Student
 * Student will have a Name and a Roll number
 * Student class implements Parcelable interface
 */

public class Student implements Parcelable {
    private String studentName;
    private String studentRollNo;


    /*
     * This is Constructor of Student Class
     * @param String studentName
     * @param String studentRollNo
     */
    public Student(final String studentName,final  String studentRollNo) {
        this.studentName = studentName;
        this.studentRollNo = studentRollNo;
    }


    /*
     * This is Constructor of Student Class
     * @param Parcel in
     */
    private Student(Parcel in) {
        studentName = in.readString();
        studentRollNo = in.readString();

    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentName);
        dest.writeString(studentRollNo);

    }

    /**
     * This method used to get Name of Student
     *
     * @return String studentName
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * This method used to get RollNo of Student
     *
     * @return String studentRollNo
     */
    public String getStudentRollNo() {
        return studentRollNo;
    }


}