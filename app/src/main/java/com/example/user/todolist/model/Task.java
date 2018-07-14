package com.example.user.todolist.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;

import java.util.Calendar;
import java.util.Date;

public class Task implements Parcelable {

    private int id, repeatType;
    private String title, description;
    private long date;
    private Boolean reminder, repeat;
    private int priority;

    protected Task(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        date = in.readLong();
        byte tmpReminder = in.readByte();
        reminder = tmpReminder == 0 ? null : tmpReminder == 1;
        byte tmpRepeat = in.readByte();
        repeat = tmpRepeat == 0 ? null : tmpRepeat == 1;
        repeatType = in.readInt();
        priority = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeSerializable(date);
        dest.writeByte((byte) (reminder == null ? 0 : reminder ? 1 : 2));
        dest.writeByte((byte) (repeat == null ? 0 : repeat ? 1 : 2));
        dest.writeInt(repeatType);
        dest.writeInt(priority);
    }

    public Task() {
        id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return new Date(date);
    }

    public void setDate(Date date) {
        this.date = date.getTime();
    }

    public Boolean getReminder() {
        return reminder;
    }

    public void setReminder(Boolean reminder) {
        this.reminder = reminder;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public int getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(int repeatType) {
        this.repeatType = repeatType;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
