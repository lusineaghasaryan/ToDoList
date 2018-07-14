package com.example.user.todolist.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.todolist.R;
import com.example.user.todolist.adapter.TasksRecyclerAdapter;
import com.example.user.todolist.model.Task;
import com.example.user.todolist.util.DateUtil;

import java.util.Calendar;

public class AddEditItemFragment extends Fragment {

    private static final String ARG_TASK = "arg.task";

    private Task mTask = new Task();
    private EditText mItemTitleEditText, mItemDescEditText;
    private TextView mItemDateTextView, mItemTimeTextView, mItemPriorityCount;
    private CheckBox mItemReminderCheckBox, mItemRepeatCheckBox;
    private ImageView mMinusPriorityImageView, mPlusPriorityImageView;
    private RadioGroup mRepeatRadioGroup;
    private int mPriorityCount = 0;
    private boolean mReminder = false, mRepeat = false;
    private Calendar mSelectedDate = Calendar.getInstance();
    private boolean mArguments;

    private Menu mMenu;
    private MenuItem menuItemEdit;
    private MenuItem menuItemSave;
    private MenuItem menuItemRemove;


    public IOnActionListener mIOnActionListener;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_edit_item, menu);
        if(mArguments){
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(false);
        } else{
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
        }

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menuItemSave = menu.getItem(0);
        menuItemEdit = menu.getItem(1);
        menuItemRemove = menu.getItem(2);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_edit_item_save:
                setInfo();
                return true;
            case R.id.menu_add_edit_item_edit:
                item.setVisible(false);
                menuItemSave.setVisible(true);
                menuItemRemove.setVisible(true);
                enableDisableRows(true);
                return true;
            case R.id.menu_add_edit_item_remove:
                //some function call
                openRemoveDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mSelectedDate.set(Calendar.YEAR, year);
            mSelectedDate.set(Calendar.MONTH, monthOfYear);
            mSelectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            openTimePicker();
        }
    };

    private void openDatePicker() {
        new DatePickerDialog(getActivity(), mOnDateSetListener, mSelectedDate.get(Calendar.YEAR),
                mSelectedDate.get(Calendar.MONTH),
                mSelectedDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void openTimePicker() {
        new TimePickerDialog(getActivity(), mOnTimeSetListener, mSelectedDate.get(Calendar.HOUR_OF_DAY),
                mSelectedDate.get(Calendar.MINUTE), true).show();
    }

    TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mSelectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mSelectedDate.set(Calendar.MINUTE, minute);

            updateDateLabel();
        }
    };


    public static AddEditItemFragment newInstance(Task task) {
        AddEditItemFragment mAddEditItemFragment = new AddEditItemFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_TASK, task);
        mAddEditItemFragment.setArguments(args);

        return mAddEditItemFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        //getActivity().setTitle("Add/Edit Item");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_item, container, false);
        inflater.inflate(R.layout.fragment_add_edit_item, container, false);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        updateDateLabel();
    }
    public void init(View view){
        mItemTitleEditText = view.findViewById(R.id.save_edit_item_title);
        mItemDescEditText = view.findViewById(R.id.save_edit_item_description);
        mItemDateTextView = view.findViewById(R.id.save_edit_item_date);
        mItemTimeTextView = view.findViewById(R.id.save_edit_item_time);
        mItemReminderCheckBox = view.findViewById(R.id.save_edit_item_reminder);
        mItemRepeatCheckBox = view.findViewById(R.id.save_edit_item_repeat);
        mItemPriorityCount = view.findViewById(R.id.save_edit_item_priority_count);
        mMinusPriorityImageView = view.findViewById(R.id.save_edit_item_counter_minus_iv);
        mPlusPriorityImageView = view.findViewById(R.id.save_edit_item_counter_plus_iv);
        mRepeatRadioGroup = view.findViewById(R.id.save_edit_item_repeat_radio_group);

        mItemTitleEditText.requestFocus();
        final InputMethodManager imm = (InputMethodManager) mItemTitleEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mItemTitleEditText, InputMethodManager.SHOW_IMPLICIT);

        mItemDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        mMinusPriorityImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPriorityCount > 0){
                    mPriorityCount--;
                    mItemPriorityCount.setText(mPriorityCount + "");
                }
                else mItemPriorityCount.setText("0");
            }
        });

        mPlusPriorityImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPriorityCount < 5){
                    mPriorityCount++;
                    mItemPriorityCount.setText(mPriorityCount + "");
                }
                else  mItemPriorityCount.setText("5");
            }
        });

        mItemRepeatCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    mRepeat = true;
                    mRepeatRadioGroup.setVisibility(View.VISIBLE);
                }else
                {
                    mRepeat = false;
                    mRepeatRadioGroup.setVisibility(View.GONE);
                }
            }
        });

        mItemReminderCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked())
                    mReminder = true;
                else
                    mReminder = false;
            }
        });
        initData();
    }

    private void initData() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_TASK) && args.getParcelable(ARG_TASK) != null) {
            mArguments = true;
            mTask = args.getParcelable(ARG_TASK);
            fillData();
            enableDisableRows(false);
        } else {
            mArguments = false;
            mTask = new Task();
        }
    }

    public void fillData(){
        mItemTitleEditText.setText(mTask.getTitle());
        mItemDescEditText.setText(mTask.getDescription());
        mItemReminderCheckBox.setChecked(mTask.getReminder());
        mItemRepeatCheckBox.setChecked(mTask.getRepeat());
        mRepeatRadioGroup.check(mTask.getRepeatType());
        mItemPriorityCount.setText(String.valueOf(mTask.getPriority()));
        mPriorityCount = mTask.getPriority();
    }

    public void setInfo (){
        if (mItemTitleEditText.getText().toString().equals("")){
            Toast toast = Toast.makeText(getContext(),
                    "Please set a Title!",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        else
            mTask.setTitle(mItemTitleEditText.getText().toString());

        if(mItemDescEditText.getText().toString().equals("")){
            Toast toast = Toast.makeText(getContext(),
                    "Please set a Description!",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        else
            mTask.setDescription(mItemDescEditText.getText().toString());

        mTask.setDate((mSelectedDate.getTime()));
        mTask.setReminder(mReminder);
        mTask.setRepeat(mRepeat);

        if (mItemRepeatCheckBox.isChecked()) {
            mTask.setRepeatType(mRepeatRadioGroup.getCheckedRadioButtonId());
        }

        mTask.setPriority(mPriorityCount);

        if (mIOnActionListener != null) {
            mIOnActionListener.onTaskSaved(mTask);
        }

        final InputMethodManager imm1 = (InputMethodManager) mItemTitleEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm1.hideSoftInputFromWindow(mItemTitleEditText.getWindowToken(), 0);
        getActivity().onBackPressed();
    }

    public void enableDisableRows(boolean enableDisable){
        mItemTitleEditText.setEnabled(enableDisable);
        mItemDescEditText.setEnabled(enableDisable);
        mItemDateTextView.setEnabled(enableDisable);
        mItemReminderCheckBox.setEnabled(enableDisable);
        mItemRepeatCheckBox.setEnabled(enableDisable);
        mRepeatRadioGroup.setEnabled(enableDisable);
        mItemPriorityCount.setEnabled(enableDisable);
    }

    private void updateDateLabel() {
        mItemDateTextView.setText(DateUtil.formatDateToLongStyle(mSelectedDate.getTime()));
    }


    public void openRemoveDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Remove Task");

        builder.setMessage("Are you want to remove this Task?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mIOnActionListener.onTaskRemoved(0);
                getActivity().onBackPressed();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public void setmIOnActionListener(IOnActionListener mIOnActionListener) {
        this.mIOnActionListener = mIOnActionListener;
    }

    public interface IOnActionListener {
        void onTaskSaved(Task task);
        void onTaskRemoved(int position);
    }
}
