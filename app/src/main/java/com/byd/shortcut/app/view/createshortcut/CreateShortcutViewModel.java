package com.byd.shortcut.app.view.createshortcut;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.byd.shortcut.App;
import com.byd.shortcut.app.common.MiscUtils;
import com.byd.shortcut.app.data.ShortcutRepository;
import com.byd.shortcut.bridge.Action;
import com.byd.shortcut.bridge.Shortcut;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Inject;

public class CreateShortcutViewModel extends ViewModel {
    private ShortcutRepository repository;

    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Action>> actions = new MutableLiveData<>();
    private Shortcut shortcut = null;

    ThreadPoolExecutor threadPoolExecutor;

    static class ActionInfo {
        boolean withResult = false;
        String result = null;
    }

    public CreateShortcutViewModel(ShortcutRepository repository, Shortcut shortcut, ThreadPoolExecutor threadPoolExecutor) {
        this.repository = repository;
        this.threadPoolExecutor = threadPoolExecutor;

        if (shortcut != null) {
            this.shortcut = shortcut;
            title.setValue(shortcut.title);
            actions.setValue(shortcut.tasks);
        }

        if (actions.getValue() == null || actions.getValue().isEmpty()) {
            addNewAction();
        }
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public LiveData<ArrayList<Action>> getActions() {
        return actions;
    }

    public void addNewAction() {
        ArrayList<Action> newActions = actions.getValue();
        if (newActions == null) {
            newActions = new ArrayList<>();
        }

        long id =  MiscUtils.uniqueId();
        newActions.add(new Action(id, Action.ACTION_TYPE_CLIPBOARD, new String(), new String(), 0, new String(), new String()));
        actions.setValue(newActions);
    }

    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public void setActionParamType(long id, int type) {
        ArrayList<Action> newActions = actions.getValue();
        if (newActions == null) {
            return;
        }

        for (Action action : newActions) {
            if (action.id == id) {
                if (action.type == type) {
                    return;
                }
                action.type = type;
                break;
            }
        }

//        actions.setValue(newActions);
    }

    public void setActionParamValue(long id, String param) {
        ArrayList<Action> newActions = actions.getValue();
        if (newActions == null) {
            return;
        }

        for (Action action : newActions) {
            if (action.id == id) {
                if (TextUtils.equals(action.paramValue, param)) {
                    return;
                }
                action.paramValue = param;
                break;
            }
        }

//        actions.setValue(newActions);
    }

    public void setActionParamTitle(long id, String title) {
        ArrayList<Action> newActions = actions.getValue();
        if (newActions == null) {
            return;
        }

        for (Action action : newActions) {
            if (action.id == id) {
                if (TextUtils.equals(action.paramTitle, title)) {
                    return;
                }
                action.paramTitle = title;
                break;
            }
        }

//        actions.setValue(newActions);
    }

    public void setActionParamAction(long id, int actionIndex) {
        ArrayList<Action> newActions = actions.getValue();
        if (newActions == null) {
            return;
        }

        for (Action action : newActions) {
            if (action.id == id) {
                if (action.paramAction == actionIndex) {
                    return;
                }
                action.paramAction = actionIndex;
                break;
            }
        }

//        actions.setValue(newActions);
    }

    public void setActionApp(long id, String app) {
        ArrayList<Action> newActions = actions.getValue();
        if (newActions == null) {
            return;
        }

        for (Action action : newActions) {
            if (action.id == id) {
                if (TextUtils.equals(action.app, app)) {
                    return;
                }
                action.app = app;
            }
        }

//        actions.setValue(newActions);
    }

    public void setActionAction(long id, String a) {
        ArrayList<Action> newActions = actions.getValue();
        if (newActions == null) {
            return;
        }

        for (Action action : newActions) {
            if (action.id == id) {
                if (TextUtils.equals(action.action, a)) {
                    return;
                }
                action.action = a;
            }
        }

//        actions.setValue(newActions);
    }

    public void moveShortcut(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        ArrayList<Action> actionArrayList = actions.getValue();
        Action item = actionArrayList.remove(fromPosition);
        actionArrayList.add(toPosition, item);
        actions.setValue(actionArrayList);
    }

    public void deleteAction(Action action) {
        ArrayList<Action> actionArrayList = actions.getValue();
        actionArrayList.remove(action);
        actions.setValue(actionArrayList);
    }

    public void saveShortcut() {
        if (shortcut == null) {
            shortcut = new Shortcut(MiscUtils.uniqueIdString(), System.currentTimeMillis(), 0, title.getValue(), actions.getValue());
        } else {
            shortcut.title = title.getValue();
            shortcut.tasks = actions.getValue();
        }

        repository.insertOrUpdateShortcut(shortcut);
    }


    public void runShortcut(Context context, Lifecycle owner, ActivityResultRegistry registry) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Action> actionArrayList = actions.getValue();
                int size = actionArrayList.size();
                if (size == 0) {
                    return;
                }

                ActionInfo[] actionResults = new ActionInfo[size];
                for (int i = 0; i < size; ++i) {
                    actionResults[i] = new ActionInfo();
                }

                for (Action action : actionArrayList) {
                    if (action.type == Action.ACTION_TYPE_ACTION_RESULT) {
                        actionResults[action.paramAction].withResult = true;
                    }
                }

                for (int i = 0; i < size; ++i) {
                    Action action = actionArrayList.get(i);
                    String param = null;
                    if (action.type == Action.ACTION_TYPE_CLIPBOARD) {
                        param = getText(App.getContext());
                    } else if (action.type == Action.ACTION_TYPE_CONST_VALUE) {
                        param = action.paramValue;
                    } else if (action.type == Action.ACTION_TYPE_DYNAMIC_VALUE) {

                    } else if (action.type == Action.ACTION_TYPE_ACTION_RESULT) {
                        param = actionResults[action.paramAction].result;
                    }

                    if (actionResults[i].withResult) {
                        Pair<Boolean, String> result = runActionAndReturnResult(context, owner, registry, action, param);
                        if (!result.first) {
                            break;
                        } else {
                            actionResults[i].result = result.second;
                        }
                    } else {
                        if (!runAction(context, action, param)) {
                            break;
                        }
                    }
                }
            }
        });
    }

    private boolean runAction(Context context, Action action, String param) {
        Intent intent = new Intent(action.action);
//        intent.setPackage(action.app);
//        intent.putExtra("param", param);

        context.startActivity(intent);
        return true;
    }

    private Pair<Boolean, String> runActionAndReturnResult(Context context, Lifecycle owner, ActivityResultRegistry registry, Action action, String param) {
        Intent intent = new Intent(action.action);
        intent.setPackage(action.app);
        intent.putExtra("param", param);

        CompletableFuture<String> resultPromise = new CompletableFuture<>();

        ActivityResultLauncher<Intent> someActivityResultLauncher = registry.register(String.valueOf(action.id),
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();

                            String resultString = null;
                            if (intent != null) {
                                resultString = intent.getStringExtra("result");
                            }

                            resultPromise.complete(resultString);
                        } else {
                            resultPromise.complete(null);
                        }
                    }
                });

        someActivityResultLauncher.launch(intent);
        String result = null;
        Boolean success = true;
        try {
            result = resultPromise.get();
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }

        return new Pair<>(success, result);
    }

    private static String getText(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(context).toString();
        }
        return null;
    }



    public static class ViewModelFactory implements ViewModelProvider.Factory {
        private ShortcutRepository repository;
        private Shortcut shortcut;
        private ThreadPoolExecutor threadPoolExecutor;

        public ViewModelFactory(ShortcutRepository repository, Shortcut shortcut, ThreadPoolExecutor threadPoolExecutor) {
            this.repository = repository;
            this.shortcut = shortcut;
            this.threadPoolExecutor = threadPoolExecutor;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new CreateShortcutViewModel(repository, shortcut, threadPoolExecutor);
        }
    }
}