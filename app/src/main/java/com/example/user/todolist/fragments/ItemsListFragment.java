package com.example.user.todolist.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.todolist.R;
import com.example.user.todolist.adapter.TasksRecyclerAdapter;
import com.example.user.todolist.model.Task;

public class ItemsListFragment extends Fragment {

    public FragmentManager mFragmentManager;
    public FragmentTransaction mFragmentTransaction;
    public FloatingActionButton mFabBtn;
    public RecyclerView mTasksRV;
    public TasksRecyclerAdapter mTaskListAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    public AddEditItemFragment.IOnActionListener mIOnActionListener = new AddEditItemFragment.IOnActionListener() {
        @Override
        public void onTaskSaved(Task task) {
            if (task.getId() == -1) {
                task.setId(mTaskListAdapter.getItemCount() + 1);
                mTaskListAdapter.addOrUpdateTask(task);
            }
            else{
                mTaskListAdapter.editTask(task);
            }
        }

        @Override
        public void onTaskRemoved(int position) {
            mTaskListAdapter.removeTask(position);
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items_list, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
    }

    public void init(View view){
        mFabBtn = (FloatingActionButton) view.findViewById(R.id.fab_fragment);
        mTasksRV = (RecyclerView) view.findViewById(R.id.rv_tasks_list_fragment);

        mTasksRV.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mTasksRV.setLayoutManager(mLayoutManager);
        mTaskListAdapter = new TasksRecyclerAdapter(getActivity());
        mTasksRV.setAdapter(mTaskListAdapter);
        //mTaskListAdapter.setmOnItemSelectedListener(onItemSelectedListener);

        mFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddEditItemFragment(null);
            }
        });


        mTaskListAdapter.setmOnItemSelectedListener(new TasksRecyclerAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Task task, int position) {
                openAddEditItemFragment(task);
            }

            @Override
            public void onRemove(int position) {
                mTaskListAdapter.removeTask(position);
            }
        });
    }

    public void openAddEditItemFragment(Task task){
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        AddEditItemFragment mAddEditItemFragment = AddEditItemFragment.newInstance(task);
        mAddEditItemFragment.setmIOnActionListener(mIOnActionListener);
        mFragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        mFragmentTransaction.add(R.id.fragment_container, mAddEditItemFragment);
        mFragmentTransaction.addToBackStack("AddEditItemFragment");
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        mFragmentTransaction.commit();
    }




}
