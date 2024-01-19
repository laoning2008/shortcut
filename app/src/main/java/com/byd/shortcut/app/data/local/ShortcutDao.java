package com.byd.shortcut.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShortcutDao {
    @Query("SELECT * FROM shortcuts")
    LiveData<List<DbShortcut>> getAll();

    @Query("SELECT * FROM shortcuts WHERE id IN (:ids)")
    LiveData<List<DbShortcut>> loadAllByIds(long[] ids);

    @Query("SELECT * FROM shortcuts WHERE id = :shortcutId")
    LiveData<DbShortcut> findById(long shortcutId);

    @Insert
    void insertAll(DbShortcut... users);

    @Update
    int update(DbShortcut shortcut);

    @Delete
    void delete(DbShortcut user);

    @Query("delete FROM shortcuts where id = :shortcutId")
    void deleteById(long shortcutId);
}
