package com.byd.shortcut.app.common;

import com.byd.shortcut.bridge.Action;
import com.byd.shortcut.bridge.Shortcut;
import com.byd.shortcut.app.data.local.DbShortcut;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {
    private static Type listType = new TypeToken<ArrayList<Action>>(){}.getType();
    private static Gson gson = new Gson();

    public static DbShortcut shortcutRpc2Db(Shortcut shortcut) {
        return new DbShortcut(shortcut.id, shortcut.modifiedTime, shortcut.type, shortcut.title, gson.toJson(shortcut.tasks));
    }

    public static List<DbShortcut> shortcutListRpc2Db(List<Shortcut> shortcuts) {
        if (shortcuts == null) {
            return null;
        }

        List<DbShortcut> results = new ArrayList<>();
        for (Shortcut shortcut : shortcuts) {
            results.add(shortcutRpc2Db(shortcut));
        }

        return results;
    }

    public static Shortcut shortcutDb2Rpc(DbShortcut shortcut) {
        ArrayList<Action> tasks = gson.fromJson(shortcut.tasks,  listType);
        return new Shortcut(shortcut.id, shortcut.modifiedTime, shortcut.type, shortcut.title, tasks);
    }

    public static List<Shortcut> shortcutListDb2Rpc(List<DbShortcut> shortcuts) {
        if (shortcuts == null) {
            return null;
        }

        List<Shortcut> results = new ArrayList<>();
        for (DbShortcut shortcut : shortcuts) {
            results.add(shortcutDb2Rpc(shortcut));
        }

        return results;
    }
}
