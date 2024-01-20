package com.byd.shortcut;

import android.app.Application;
import android.content.Context;

import com.byd.shortcut.app.common.ServiceUtil;
import com.byd.shortcut.app.service.ServiceMgr;
import com.byd.shortcut.app.service.ServiceBinder;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class App extends Application {
    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }
    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    private ServiceBinder serviceBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;

        ServiceUtil.startShortcutService();

        serviceBinder = new ServiceBinder();
        serviceBinder.addInterfaceChangedObserver(ServiceMgr.getInstance().getExecutorProxy());
        serviceBinder.start();
    }
}
