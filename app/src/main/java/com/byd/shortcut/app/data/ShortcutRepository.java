package com.byd.shortcut.app.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.byd.shortcut.bridge.IShortcutRepository;
import com.byd.shortcut.bridge.Shortcut;
import com.byd.shortcut.app.data.local.DbShortcut;
import com.byd.shortcut.app.data.local.ShortcutDao;
import com.byd.shortcut.app.common.ConvertUtil;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ShortcutRepository implements IShortcutRepository {
    private ShortcutDao shortcutDao;
    private LiveData<List<DbShortcut>> allShortcutDb;
    private MutableLiveData<List<Shortcut>> allShortcut = new MutableLiveData<>();

    @Inject
    public ShortcutRepository(ShortcutDao shortcutDao) {
        this.shortcutDao = shortcutDao;
        allShortcutDb = shortcutDao.getAll();
        allShortcutDb.observeForever(new Observer<List<DbShortcut>>() {
            @Override
            public void onChanged(List<DbShortcut> dbShortcuts) {
                allShortcut.setValue(ConvertUtil.shortcutListDb2Rpc(dbShortcuts));
            }
        });
    }

    public boolean createShortcut(Shortcut shortcut) {
        DbShortcut dbShortcut = ConvertUtil.shortcutRpc2Db(shortcut);
        shortcutDao.insertAll(dbShortcut);
        return true;
    }

    public boolean deleteShortcut(String id) {
        shortcutDao.deleteById(id);
        return true;
    }

    public boolean updateShortcut(Shortcut shortcut) {
        DbShortcut dbShortcut = ConvertUtil.shortcutRpc2Db(shortcut);
        shortcutDao.update(dbShortcut);
        return true;
    }

    public LiveData<List<Shortcut>> getAllShortcut() {
        return allShortcut;
    }

    public boolean moveShortcut(int fromPosition, int toPosition) {
        shortcutDao.move(fromPosition, toPosition);
        return true;
    }
}
