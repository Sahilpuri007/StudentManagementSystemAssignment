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
    private String name;
    private String rollNo;

    public Student(final String studentName,final  String studentRollNo) {
        this.name = studentName;
        this.rollNo = studentRollNo;
    }

    private Student(Parcel in) {
        name = in.readString();
        rollNo = in.readString();

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
        dest.writeString(name);
        dest.writeString(rollNo);

    }

    public String getName() {
        return name;
    }

    public String getRollNo() {
        return rollNo;
    }


}