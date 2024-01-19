package com.byd.shortcut;
import com.byd.shortcut.IShortcutExecutionCallback;


interface IShortcutServiceApi {
    void registerShortcutExecutionCallback(IShortcutExecutionCallback callback);
    void unregisterShortcutExecutionCallback(IShortcutExecutionCallback callback);
    boolean runShortcut(long id);
}