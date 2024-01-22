package com.byd.shortcut.app.view.createshortcut;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.byd.shortcut.app.common.MiscUtils;
import com.byd.shortcut.app.data.ShortcutRepository;
import com.byd.shortcut.bridge.Action;
import com.byd.shortcut.bridge.Shortcut;

import java.util.ArrayList;

public class CreateShortcutViewModel extends ViewModel {
    private ShortcutRepository repository;
    private Shortcut shortcut;

    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Action>> actions = new MutableLiveData<>();

    public CreateShortcutViewModel(ShortcutRepository repository, Shortcut shortcut) {
        this.repository = repository;
        this.shortcut = shortcut;

        if (shortcut != null) {
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
        newActions.add(new Action(id, 0, new String(), new String(), 0, new String(), new String()));
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
            Shortcut newShortcut = new Shortcut(MiscUtils.uniqueIdString(), System.currentTimeMillis(), 0, title.getValue(), actions.getValue());
            repository.createShortcut(newShortcut);
        } else {
            shortcut.title = title.getValue();
            shortcut.tasks = actions.getValue();
            repository.updateShortcut(shortcut);
        }
    }


    public void runShortcut() {

    }


    public static class ViewModelFactory implements ViewModelProvider.Factory {
        private ShortcutRepository repository;
        private Shortcut shortcut;

        public ViewModelFactory(ShortcutRepository repository, Shortcut shortcut) {
            this.repository = repository;
            this.shortcut = shortcut;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new CreateShortcutViewModel(repository, shortcut);
        }
    }
}