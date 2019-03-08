package com.system.studentmanagement.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.system.studentmanagement.R;
import com.system.studentmanagement.model.Student;

import java.util.ArrayList;

/*
 * @author Sahil Puri
 * StudentListAdapter
 * A custom adapter for the recyclerView used
 *
 */
public class StudentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Student> studentArrayList;
    private OnItemClickListener mListener;

    /*
     * Constructor for StudentListAdapter
     * @param ArrayList<Student> studentArrayList
     */
    public StudentListAdapter(final ArrayList<Student> studentArrayList) {
        this.studentArrayList = studentArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = (View) layoutInflater.inflate(R.layout.item_student_layout, viewGroup, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        StudentViewHolder studentViewHolder = (StudentViewHolder) viewHolder;
        studentViewHolder.textViewName.setText(studentArrayList.get(i).getStudentName());
        studentViewHolder.textViewRollNo.setText(studentArrayList.get(i).getStudentRollNo());


    }

    @Override
    public int getItemCount() {
        return studentArrayList.size();
    }
    /*
     * Method setOnItemClickListener
     * @param OnItemClickListener listener
     */
    public void setOnItemClickListener(final OnItemClickListener listener) {
        mListener = listener;
    }
    /*
     * Inner Class StudentViewHolder
     * To describes an item view within the RecyclerView.
     */
    class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewRollNo;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewRollNo = itemView.findViewById(R.id.textViewRollNo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }
    /*
     * Interface OnItemClickListener
     * To add a onClick on RecyclerView Items
     */
    public interface OnItemClickListener {
        void OnItemClick(final int position);
    }

}