package com.byd.shortcut.app.model;

public class ServiceMgr {
    private static ServiceMgr sInstance = new ServiceMgr();
    private ShortcutExecutorProxy executorProxy = new ShortcutExecutorProxy();

    public static ServiceMgr getInstance() {
        return sInstance;
    }

    public ShortcutExecutorProxy getExecutorProxy() {
        return executorProxy;
    }
}
