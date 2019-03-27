package com.system.studentmanagement.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.system.studentmanagement.R;
import com.system.studentmanagement.model.Student;
import com.system.studentmanagement.touchlistener.RecyclerTouchListener;

import java.util.ArrayList;

/*
 * @author Sahil Puri
 * StudentListAdapter
 * A custom adapter for the recyclerView used
 *
 */
public class StudentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Student> studentArrayList;
    private RecyclerTouchListener mListener;

    /*
     * Constructor for StudentListAdapter
     * @param ArrayList<Student> studentArrayList
     */
    public StudentListAdapter(final ArrayList<Student> studentArrayList, final RecyclerTouchListener listener) {
        this.studentArrayList = studentArrayList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = (View) layoutInflater.inflate(R.layout.item_student_layout, viewGroup, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        StudentViewHolder studentViewHolder = (StudentViewHolder) viewHolder;
        studentViewHolder.tvName.setText(studentArrayList.get(i).getName());
        studentViewHolder.tvRollNo.setText(studentArrayList.get(i).getRollNo());

        studentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(viewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentArrayList.size();
    }

    /*
     * Inner Class StudentViewHolder
     * To describes an item view within the RecyclerView.
     */
    class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRollNo;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.itemTvName);
            tvRollNo = itemView.findViewById(R.id.itemTvRollNo);

        }
    }


}