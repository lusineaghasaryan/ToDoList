package com.example.user.todolist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.todolist.R;
import com.example.user.todolist.db.DbManager;
import com.example.user.todolist.model.Task;
import com.example.user.todolist.viewHolder.TaskItemViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.zip.Inflater;

public class TasksRecyclerAdapter extends RecyclerView.Adapter<TaskItemViewHolder> {
    private static final String TAG = "TasksAdapter";
    public ArrayList<Task> mTasksList = new ArrayList<>();
    public  Context mContext;
    private OnItemSelectedListener mOnItemSelectedListener;
    public int mSelectedItemPosition, mSelectedItemId;
    private DbManager mDbManager;

    public TasksRecyclerAdapter(Context context) {
        super();
        mContext = context;
        mDbManager = new DbManager(mContext);
        mTasksList.addAll(mDbManager.getTasksList());
    }

    @NonNull
    @Override
    public TaskItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate( R.layout.task_item_rv, parent, false);
        TaskItemViewHolder viewHolder = new TaskItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskItemViewHolder holder, final int position) {
        final Task mTask = mTasksList.get(position);
        holder.mItemTitle.setText(mTask.getTitle());
        holder.mItemDescription.setText(mTask.getDescription());
        Log.v(TAG, "ggggggg = " + mTask.getDate().toString());
        String myFormat = "dd.MM.yyyy" + "  " + "hh:ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        holder.mItemDate.setText(sdf.format(mTask.getDate().getTime()));

        holder.setmIOnClickListener(new TaskItemViewHolder.IOnClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(mTask, position);
                    mSelectedItemPosition = position;
                    mSelectedItemId = mTask.getId();
                }
            }
        });

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuItem edit = menu.add("Edit");
                MenuItem remove = menu.add("Remove");

            }
        });

/*
        holder.setmIOnClickListener(new TaskItemViewHolder.IOnClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(mTask, position);
                    mSelectedItemPosition = position;
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mOnItemSelectedListener.onRemove(holder.getAdapterPosition());
                return false;
            }
        });
*/
    }



    @Override
    public int getItemCount() {
        return mDbManager.getTasksCount();
        //return mTasksList.size();
    }

    public void addOrUpdateTask(Task task) {
        // Database  insert new task
        mDbManager.insertTask(task);

        mTasksList.add(task);
        notifyItemInserted(mTasksList.size() - 1);
    }

    public void editTask(Task task){
        // Database update task
        mDbManager.updateTask(task);

        mTasksList.set(mSelectedItemPosition, task);
        notifyItemChanged(mSelectedItemPosition);
    }

    public void removeTask(int position){
        // Database  remove task
        mDbManager.removeTask(mSelectedItemId);
        mTasksList.remove(mSelectedItemPosition);
        notifyDataSetChanged();
    }

    public void setmOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener){
        mOnItemSelectedListener = onItemSelectedListener;
    }


    public interface OnItemSelectedListener  {
        void onItemSelected(Task task, int position);
        void onRemove(int position);

    }
}
