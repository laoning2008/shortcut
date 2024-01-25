package com.byd.shortcut.bridge;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface IShortcutRepository {
    boolean deleteShortcut(String id);

    boolean insertOrUpdateShortcut(Shortcut shortcut);

    LiveData<List<Shortcut>> getAllShortcut();

    boolean moveShortcut(int fromPosition, int toPosition);
}
