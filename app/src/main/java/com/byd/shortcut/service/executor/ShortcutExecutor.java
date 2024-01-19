package com.byd.shortcut.service.executor;

import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.byd.shortcut.IShortcutExecutionCallback;
import com.byd.shortcut.bridge.IShortcutExecutor;


public class ShortcutExecutor implements IShortcutExecutor {
    private RemoteCallbackList<IShortcutExecutionCallback> mListenerList = new RemoteCallbackList<>();

    public ShortcutExecutor() {
    }

    public void registerShortcutExecutionCallback(IShortcutExecutionCallback callback)  {
        mListenerList.register(callback);
    }

    public void unregisterShortcutExecutionCallback(IShortcutExecutionCallback callback)  {
        mListenerList.unregister(callback);
    }

    public boolean runShortcut(long id) {
        return false;
    }

    private void onResult(long id, boolean success, String message) {
        int callbackCount = mListenerList.beginBroadcast();

        for (int i  = 0; i < callbackCount; ++i) {
            try {
                mListenerList.getBroadcastItem(i).onExecutionResult(id, success, message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        mListenerList.finishBroadcast();
    }
}
