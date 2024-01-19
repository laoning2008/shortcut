package com.byd.shortcut.app.common;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import com.byd.shortcut.App;
import com.byd.shortcut.app.view.createshortcut.CreateShortcutActivity;

public class NavigationUtil {
    public static void startCreateShortcutActivity(Context context) {
        Intent intent = new Intent(context, CreateShortcutActivity.class);
        context.startActivity(intent);
    }
}
