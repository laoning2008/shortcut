package com.byd.shortcut.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class ShortcutDao {
    @Query("SELECT * FROM shortcuts ORDER by ROWID")
    public abstract LiveData<List<DbShortcut>> getAll();

    @Query("SELECT * FROM shortcuts ORDER by ROWID")
    public abstract List<DbShortcut> getAllRaw();

    @Query("SELECT * FROM shortcuts WHERE id IN (:ids)")
    public abstract LiveData<List<DbShortcut>> loadAllByIds(long[] ids);

    @Query("SELECT * FROM shortcuts WHERE id = :shortcutId")
    public abstract LiveData<DbShortcut> findById(long shortcutId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(DbShortcut... shortcuts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll2(List<DbShortcut> shortcuts);

    @Delete
    public abstract void delete(DbShortcut shortcut);

    @Query("DELETE FROM shortcuts")
    public abstract void deleteAll();

    @Query("delete FROM shortcuts where id = :shortcutId")
    public abstract void deleteById(String shortcutId);

    @Transaction
    public void move(int fromPosition, int toPosition) {
        List<DbShortcut> shortcuts = getAllRaw();
        DbShortcut item = shortcuts.remove(fromPosition);
        shortcuts.add(toPosition, item);

        deleteAll();
        insertAll2(shortcuts);
    }
}
