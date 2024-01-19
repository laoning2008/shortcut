package com.byd.shortcut.app.model.service;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import com.byd.shortcut.IShortcutServiceApi;
import com.byd.shortcut.app.common.ServiceUtil;

import java.util.ArrayList;
import java.util.List;

public class ServiceBinder {
    private IShortcutServiceApi serviceApi;
    private boolean hasBind = false;
    private List<IInterfaceChanged> interfaceChangedCallbackList = new ArrayList<>();

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serviceApi = IShortcutServiceApi.Stub.asInterface(iBinder);
            fireInterfaceChangedCallback();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            unbindService();
            fireInterfaceChangedCallback();

            //should be careful----need to bind again instantly, need to control bind times?
            bindService();
        }
    };

    public void addInterfaceChangedObserver(IInterfaceChanged observer) {
        interfaceChangedCallbackList.add(observer);
    }

    public void removeInterfaceChangedObserver(IInterfaceChanged observer) {
        interfaceChangedCallbackList.remove(observer);
    }

    public void start() {
        bindService();
    }

    public void stop() {
        interfaceChangedCallbackList.clear();
        unbindService();
    }

    private void bindService() {
        if (hasBind) {
            return;
        }

        boolean result = ServiceUtil.bindShortcutService(mServiceConnection);
        if (result) {
            hasBind = true;
        } else {
            ServiceUtil.unbindShortcutService(mServiceConnection);
        }
    }

    private void unbindService() {
        if (!hasBind) {
            return;
        }

        hasBind = false;
        serviceApi = null;

        //always unbindService
        ServiceUtil.unbindShortcutService(mServiceConnection);
    }

    private void fireInterfaceChangedCallback() {
        for (IInterfaceChanged callback : interfaceChangedCallbackList) {
            callback.onChanged(serviceApi);
        }
    }
}
