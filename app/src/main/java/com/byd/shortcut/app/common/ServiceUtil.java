package com.byd.shortcut.app.common;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.Intent;
import android.content.ServiceConnection;

import com.byd.shortcut.App;

public class ServiceUtil {
    public static void startShortcutService() {
        Intent intent = new Intent("com.byd.shortcut.service");
        intent.setPackage("com.byd.shortcut");
        App.getContext().startService(intent);
    }

    public static void stopShortcutService() {
        Intent intent = new Intent("com.byd.shortcut.service");
        intent.setPackage("com.byd.shortcut");
        App.getContext().startService(intent);
    }

    public static boolean bindShortcutService(ServiceConnection connection) {
        Intent intent = new Intent("com.byd.shortcut.service");
        intent.setPackage("com.byd.shortcut");
        return App.getContext().bindService(intent, connection, BIND_AUTO_CREATE);
    }

    public static void unbindShortcutService(ServiceConnection connection) {
        Intent intent = new Intent("com.byd.shortcut.service");
        intent.setPackage("com.byd.shortcut");
        App.getContext().unbindService(connection);
    }
}
