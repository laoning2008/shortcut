package com.byd.shortcut.app.service;

import com.byd.shortcut.IShortcutExecutionCallback;
import com.byd.shortcut.IShortcutServiceApi;
import com.byd.shortcut.app.service.IInterfaceChanged;
import com.byd.shortcut.bridge.IShortcutExecutor;

public class ShortcutExecutorProxy implements IShortcutExecutor, IInterfaceChanged {
    private IShortcutServiceApi serviceApi;

    @Override
    public void registerShortcutExecutionCallback(IShortcutExecutionCallback callback) {

    }

    @Override
    public void unregisterShortcutExecutionCallback(IShortcutExecutionCallback callback) {

    }

    @Override
    public boolean runShortcut(long id) {
        if (serviceApi == null) {
            return false;
        }

        return false;
    }

    @Override
    public void onChanged(IShortcutServiceApi serviceApi) {
        this.serviceApi = serviceApi;
    }
}
