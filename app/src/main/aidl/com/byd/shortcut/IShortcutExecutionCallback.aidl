package com.byd.shortcut;

interface IShortcutExecutionCallback {
    void onExecutionResult(long id, boolean success, String message);
}