package com.byd.shortcut.bridge;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface IShortcutRepository {
    boolean createShortcut(Shortcut shortcut);

    boolean deleteShortcut(String id);

    boolean updateShortcut(Shortcut shortcut);

    LiveData<List<Shortcut>> getAllShortcut();

    boolean moveShortcut(int fromPosition, int toPosition);
}
