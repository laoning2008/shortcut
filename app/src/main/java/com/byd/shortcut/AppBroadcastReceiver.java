package com.byd.shortcut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.byd.shortcut.app.common.ServiceUtil;

public class AppBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            ServiceUtil.startShortcutService();
        }
    }




}