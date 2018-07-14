package com.example.user.todolist.db;

import android.provider.BaseColumns;

public class TodoItemContract {
    public TodoItemContract() {
    }

    public static class TodoEntry implements BaseColumns{
        public static final String TABLE_NAME = "todo_items";
        public static final String _ID = "id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String DATE = "date";
        public static final String IS_REMINDER = "is_reminder";
        public static final String IS_REPEAT = "is_repeat";
        public static final String REPEAT_TYPE = "repeat_type";
        public static final String PRIORITY = "priority";
    }
}
