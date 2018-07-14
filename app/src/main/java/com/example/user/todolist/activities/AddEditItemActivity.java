package com.example.user.todolist.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.user.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditItemActivity extends AppCompatActivity{

    private static final String TAG = "AddEditItemActivity";
    public static final String KEY_ADDED_TASK = "task";

    private Task mTask = new Task();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    private Button mSaveEditBtn;
    private EditText mItemTitleEditText, mItemDescEditText;
    private TextView mItemDateTextView, mItemTimeTextView, mItemPriorityCount;
    private CheckBox mItemReminderCheckBox, mItemRepeatCheckBox;
    private ImageView mMinusPriorityImageView, mPlusPriorityImageView;
    private RadioGroup mRepeatRadioGroup;
    private int mPriorityCount = 0;
    private boolean mReminder = false, mRepeat = false;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        mSaveEditBtn = (Button) findViewById(R.id.item_save_edit_btn);
        mItemTitleEditText = (EditText) findViewById(R.id.save_edit_item_title);
        mItemDescEditText = (EditText)findViewById(R.id.save_edit_item_description);
        mItemDateTextView = (TextView) findViewById(R.id.save_edit_item_date);
        mItemTimeTextView = (TextView) findViewById(R.id.save_edit_item_time);
        mItemReminderCheckBox = (CheckBox) findViewById(R.id.save_edit_item_reminder);
        mItemRepeatCheckBox = (CheckBox) findViewById(R.id.save_edit_item_repeat);
        mItemPriorityCount = (TextView) findViewById(R.id.save_edit_item_priority_count);
        mMinusPriorityImageView = (ImageView) findViewById(R.id.save_edit_item_counter_minus_iv);
        mPlusPriorityImageView = (ImageView) findViewById(R.id.save_edit_item_counter_plus_iv);
        mRepeatRadioGroup = (RadioGroup) findViewById(R.id.save_edit_item_repeat_radio_group);

        Intent intent = getIntent();
        Log.v(TAG, "tesnenq inch ekav = " + intent.getStringExtra("EDIT"));

        switch (intent.getStringExtra("ADDEDIT")){
            case "save":
                mSaveEditBtn.setText("Save");
                break;
            case "edit":
                mSaveEditBtn.setText("Edit");
                break;
        }

        mSaveEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSaveEditBtn.getText() == "Edit")
                    mSaveEditBtn.setText("Save");
                else{
                    setInfo();
                    Intent intent = new Intent();
                    intent.putExtra(KEY_ADDED_TASK, mTask);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
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

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mItemDateTextView.setText(dateFormat.format(myCalendar.getTime()));
            }

        };

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                mItemTimeTextView.setText(timeFormat.format(myCalendar.getTime()));
            }
        };

        mItemDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddEditItemActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    public void getInfo (Task task){
        mItemTitleEditText.setText(task.getTitle());
        mItemDescEditText.setText(task.getDescription());
        mItemReminderCheckBox.setChecked(task.getReminder());
        mItemRepeatCheckBox.setChecked(task.getRepeat());
        mRepeatRadioGroup.check(task.getRepeatType());
        mItemPriorityCount.setText(task.getPriority());
    }

    public void setInfo (){
        mTask.setTitle(mItemTitleEditText.getText().toString());
        mTask.setDescription(mItemDescEditText.getText().toString());
        mTask.setDate((myCalendar.getTime()));
        mTask.setReminder(mReminder);
        mTask.setRepeat(mRepeat);
        if (mItemRepeatCheckBox.isChecked()) {
            mTask.setRepeatType(mRepeatRadioGroup.getCheckedRadioButtonId());
        }
        mTask.setPriority(mPriorityCount);
    }

}
