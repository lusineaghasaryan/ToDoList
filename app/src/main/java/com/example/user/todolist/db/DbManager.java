package com.example.user.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.user.todolist.db.TodoItemContract.TodoEntry;
import com.example.user.todolist.model.Task;

import java.util.ArrayList;

public final class DbManager {

    private DbHelper mDbHelper;

    public DbManager(Context context) {
        this.mDbHelper = new DbHelper(context);
    }

    public long insertTask(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoEntry.TITLE, task.getTitle());
        contentValues.put(TodoEntry.DESCRIPTION, task.getDescription());
        contentValues.put(TodoEntry.DATE, task.getDate().getTime());
        contentValues.put(TodoEntry.IS_REMINDER, task.getReminder());
        contentValues.put(TodoEntry.IS_REPEAT, task.getRepeat());
        contentValues.put(TodoEntry.REPEAT_TYPE, task.getRepeatType());
        contentValues.put(TodoEntry.PRIORITY, task.getPriority());
        return mDbHelper.getWritableDatabase().insert(TodoEntry.TABLE_NAME, null, contentValues);
    }

    public ArrayList<Task> getTasksList(){
        ArrayList<Task> mTasksList = new ArrayList<>();

        Cursor cursor = mDbHelper.getReadableDatabase()
                .query(TodoEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(TodoEntry._ID));
            String title = cursor.getString(cursor.getColumnIndex(TodoEntry.TITLE));
            String description = cursor.getString(cursor.getColumnIndex(TodoEntry.DESCRIPTION));
            long date = cursor.getLong(cursor.getColumnIndex(TodoEntry.DATE));
            int reminder = cursor.getColumnIndex(TodoEntry.IS_REMINDER);
            int repeat = cursor.getColumnIndex(TodoEntry.IS_REPEAT);
            int repeat_type = cursor.getColumnIndex(TodoEntry.REPEAT_TYPE);
            int priority = cursor.getColumnIndex(TodoEntry.PRIORITY);

            Task task = new Task();
            task.setId(id);
            task.setTitle(title);
            task.setDescription(description);
            //task.setDate(date);
            task.setReminder(reminder == 1);
            task.setRepeat(repeat == 1);
            task.setRepeatType(repeat_type);
            task.setPriority(priority);

            mTasksList.add(task);
        }
        cursor.close();
        return mTasksList;
    }

    public Task getTask(int id){
        Task mTask = new Task();

        String selection = TodoEntry._ID + "= ? ";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = mDbHelper.getReadableDatabase()
                .query(TodoEntry.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(TodoEntry.TITLE));
                String description = cursor.getString(cursor.getColumnIndex(TodoEntry.DESCRIPTION));
                long date = cursor.getLong(cursor.getColumnIndex(TodoEntry.DATE));
                int reminder = cursor.getColumnIndex(TodoEntry.IS_REMINDER);
                int repeat = cursor.getColumnIndex(TodoEntry.IS_REPEAT);
                int repeat_type = cursor.getColumnIndex(TodoEntry.REPEAT_TYPE);
                int priority = cursor.getColumnIndex(TodoEntry.PRIORITY);

                mTask.setId(id);
                mTask.setTitle(title);
                mTask.setDescription(description);
                //mTask.setDate(date);
                mTask.setReminder(reminder == 1);
                mTask.setRepeat(repeat == 1);
                mTask.setRepeatType(repeat_type);
                mTask.setPriority(priority);
            }
        }
        cursor.close();
        return mTask;
    }

    public int updateTask(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoEntry.TITLE, task.getTitle());
        contentValues.put(TodoEntry.DESCRIPTION, task.getDescription());
        contentValues.put(TodoEntry.DATE, task.getDate().getTime());
        contentValues.put(TodoEntry.IS_REMINDER, task.getReminder());
        contentValues.put(TodoEntry.IS_REPEAT, task.getRepeat());
        contentValues.put(TodoEntry.REPEAT_TYPE, task.getRepeatType());
        contentValues.put(TodoEntry.PRIORITY, task.getPriority());

        String where = TodoEntry._ID + "=" + task.getId();
        return mDbHelper.getWritableDatabase().update(TodoEntry.TABLE_NAME, contentValues, where, null);
    }


    public int removeTask(int id){
        String where = TodoEntry._ID + "=" + id;
        return mDbHelper.getWritableDatabase().delete(TodoEntry.TABLE_NAME, where, null);
    }

    public int getTasksCount(){
        String countQuery = "SELECT  * FROM " + TodoEntry.TABLE_NAME;
        Cursor cursor =  mDbHelper.getReadableDatabase().rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
