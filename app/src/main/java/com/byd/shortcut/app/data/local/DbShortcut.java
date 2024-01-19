package com.byd.shortcut.app.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shortcuts")
public class DbShortcut {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    public Long modifiedTime = 0L;

    public Integer type = 0;

    public String title;

    public String tasks;

    public DbShortcut(long modifiedTime, int type, String title, String tasks) {
        this.modifiedTime = modifiedTime;
        this.type = type;
        this.title = title;
        this.tasks = tasks;
    }
}
