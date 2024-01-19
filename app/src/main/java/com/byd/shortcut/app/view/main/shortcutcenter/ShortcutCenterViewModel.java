package com.byd.shortcut.app.view.main.shortcutcenter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.byd.shortcut.app.data.ShortcutRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ShortcutCenterViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    @Inject
    public ShortcutCenterViewModel(ShortcutRepository repository) {
        mText = new MutableLiveData<>();
//        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}