package com.byd.shortcut.app.view.main.shortcuts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.byd.shortcut.app.data.ShortcutRepository;
import com.byd.shortcut.bridge.Shortcut;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AllShortcutViewModel extends ViewModel {

    private final MutableLiveData<List<Shortcut>> shortcuts;
    private ShortcutRepository repository;

    @Inject
    public AllShortcutViewModel(ShortcutRepository repository) {
        this.repository = repository;
        shortcuts = new MutableLiveData<>();
    }

    public LiveData<List<Shortcut>> getShortcuts() {
        return repository.getAllShortcut();
    }

    public void moveShortcut(int fromPosition, int toPosition) {
        repository.moveShortcut(fromPosition, toPosition);
    }

    public void deleteShortcut(Shortcut shortcut) {
        repository.deleteShortcut(shortcut.id);
    }
}