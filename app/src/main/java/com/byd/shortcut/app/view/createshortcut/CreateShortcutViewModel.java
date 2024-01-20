package com.byd.shortcut.app.view.createshortcut;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.byd.shortcut.app.common.MiscUtils;
import com.byd.shortcut.app.data.ShortcutRepository;
import com.byd.shortcut.bridge.Action;
import com.byd.shortcut.bridge.Shortcut;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

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
        newActions.add(new Action(id, 0, new String(), new String(), new String()));
        actions.setValue(newActions);
    }

    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public void setActionParam(long id, String param) {
        List<Action> newActions = actions.getValue();
        if (newActions == null) {
            return;
        }

        for (Action action : newActions) {
            if (action.id == id) {
                action.param = param;
            }
        }
    }

    public void setActionApp(long id, String app) {
        List<Action> newActions = actions.getValue();
        if (newActions == null) {
            return;
        }

        for (Action action : newActions) {
            if (action.id == id) {
                action.app = app;
            }
        }
    }

    public void setActionAction(long id, String a) {
        List<Action> newActions = actions.getValue();
        if (newActions == null) {
            return;
        }

        for (Action action : newActions) {
            if (action.id == id) {
                action.action = a;
            }
        }
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