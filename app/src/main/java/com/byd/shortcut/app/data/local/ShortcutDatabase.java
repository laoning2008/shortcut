package com.byd.shortcut.app.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DbShortcut.class}, version = 1, exportSchema = false)
public abstract class ShortcutDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "shortcut";
    public abstract ShortcutDao shortcutDao();
}
