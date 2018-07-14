package com.example.user.todolist.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.todolist.R;

public class TaskItemViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "TasksAdapter";
    public TextView mItemTitle, mItemDescription, mItemDate;
    public IOnClickListener mIOnClickListener;

    public TaskItemViewHolder(final View itemView) {
        super(itemView);

        mItemTitle = itemView.findViewById(R.id.task_item_rv_title);
        mItemDescription = itemView.findViewById(R.id.task_item_rv_description);
        mItemDate = itemView.findViewById(R.id.task_item_rv_date);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIOnClickListener != null){
                    mIOnClickListener.onItemClick(getAdapterPosition());
                }
            }
        });
    }


    public interface IOnClickListener{
        public void onItemClick(int position);
    }

    public void setmIOnClickListener(IOnClickListener mIOnClickListener) {
        this.mIOnClickListener = mIOnClickListener;
    }
}
