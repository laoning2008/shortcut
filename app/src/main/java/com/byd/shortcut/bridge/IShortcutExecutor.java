package com.byd.shortcut.bridge;

import com.byd.shortcut.IShortcutExecutionCallback;

public interface IShortcutExecutor  {
    void registerShortcutExecutionCallback(IShortcutExecutionCallback callback);
    void unregisterShortcutExecutionCallback(IShortcutExecutionCallback callback);
    boolean runShortcut(long id);
}
