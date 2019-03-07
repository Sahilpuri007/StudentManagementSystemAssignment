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

public class StudentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

   private ArrayList<Student> studentArrayList;
    private OnItemClickListener mListener;

    public StudentListAdapter(ArrayList<Student> studentArrayList) {
        this.studentArrayList = studentArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = (View) layoutInflater.inflate(R.layout.student_item_layout,viewGroup,false);
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

    class StudentViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName, textViewRollNo;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewRollNo = (TextView) itemView.findViewById(R.id.textViewRollNo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
}