package com.byd.shortcut.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shortcuts")
public class DbShortcut {
    @PrimaryKey
    @NonNull
    public String id;

    public Long modifiedTime = 0L;

    public Integer type = 0;

    public String title;

    public String tasks;

    public DbShortcut(String id, long modifiedTime, int type, String title, String tasks) {
        this.id = id;
        this.modifiedTime = modifiedTime;
        this.type = type;
        this.title = title;
        this.tasks = tasks;
    }
}
