package com.byd.shortcut.app.view.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.byd.shortcut.App;
import com.byd.shortcut.app.view.createshortcut.CreateShortcutActivity;
import com.byd.shortcut.bridge.Shortcut;

public class NavigationUtil {
    public static void navigateToShortcutDetail(Context context, Shortcut shortcut) {
        Intent intent = new Intent(context, CreateShortcutActivity.class);
        intent.putExtra(CreateShortcutActivity.PARAM_KEY_SHORTCUT, shortcut);

        Bundle b = new Bundle();
        b.putParcelable(CreateShortcutActivity.PARAM_KEY_SHORTCUT, shortcut);
        intent.putExtras(b);

        context.startActivity(intent);
    }
}
