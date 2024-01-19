package com.byd.shortcut.bridge;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface IShortcutRepository {
    boolean createShortcut(Shortcut shortcut);

    boolean deleteShortcut(long id);

    boolean updateShortcut(Shortcut shortcut);

    LiveData<List<Shortcut>> getAllShortcut();
}
