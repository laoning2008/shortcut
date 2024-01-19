package com.byd.shortcut.app.view.createshortcut;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.byd.shortcut.app.common.MiscUtils;
import com.byd.shortcut.app.data.ShortcutRepository;
import com.byd.shortcut.app.model.ServiceMgr;
import com.byd.shortcut.bridge.Action;
import com.byd.shortcut.bridge.Shortcut;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CreateShortcutViewModel extends ViewModel {
    private ShortcutRepository repository;

    private final MutableLiveData<String> title;
    private final MutableLiveData<ArrayList<Action>> actions;

    @Inject
    public CreateShortcutViewModel(ShortcutRepository repository) {
        this.repository = repository;
        title = new MutableLiveData<>();
        actions = new MutableLiveData<>();
        if (actions.getValue() == null || actions.getValue().isEmpty()) {
            addNewAction();
        }
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public LiveData<ArrayList<Action>> getActons() {
        return actions;
    }

    public void addNewAction() {
        ArrayList<Action> newActions = actions.getValue();
        if (newActions == null) {
            newActions = new ArrayList<>();
        }

        long id =  MiscUtils.uniqueId();
        Log.d("shortcut", "addNewAction, id = " + id);

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

    public void saveShortcut() {
        Shortcut shortcut = new Shortcut(0, System.currentTimeMillis(), 0, title.getValue(), actions.getValue());
        repository.createShortcut(shortcut);
    }
}