package com.byd.shortcut.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.byd.shortcut.IShortcutExecutionCallback;
import com.byd.shortcut.IShortcutServiceApi;
import com.byd.shortcut.service.executor.ShortcutExecutor;

public class ShortcutService extends Service {
    public static final String CHANNEL_ID = "ShortcutService_Channel_Id";
    public static final String CHANNEL_NAME = "shortcut";
    public static final String NOTIFICATION_TITLE = "shortcut";
    public static final String NOTIFICATION_CONTENT = "shortcut";

    private ShortcutExecutor shortcutExecutor;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("shortcut", "Service.onCreate");

        createNotificationChannel();
        startForeground(1, buildForegroundNotification());

        shortcutExecutor = new ShortcutExecutor();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("shortcut", "Service.onDestroy");
        shortcutExecutor = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
        }
    }

    private Notification buildForegroundNotification() {
        return new Notification.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(NOTIFICATION_CONTENT)
                .setSmallIcon(android.R.drawable.star_big_on)
                .build();
    }


    private IShortcutServiceApi.Stub mBinder = new IShortcutServiceApi.Stub() {
        @Override
        public void registerShortcutExecutionCallback(IShortcutExecutionCallback callback) throws RemoteException {
            shortcutExecutor.registerShortcutExecutionCallback(callback);
        }

        @Override
        public void unregisterShortcutExecutionCallback(IShortcutExecutionCallback callback) throws RemoteException {
            shortcutExecutor.unregisterShortcutExecutionCallback(callback);
        }

        @Override
        public boolean runShortcut(long id) throws RemoteException {
            return shortcutExecutor.runShortcut(id);
        }
    };
}