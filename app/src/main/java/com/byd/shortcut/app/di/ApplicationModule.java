package com.byd.shortcut.app.di;

import android.app.Application;

import androidx.room.Room;


import com.byd.shortcut.app.data.ShortcutRepository;
import com.byd.shortcut.app.data.local.ShortcutDao;
import com.byd.shortcut.app.data.local.ShortcutDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ApplicationModule {

    @Provides
    @Singleton
    public static ShortcutDatabase database(Application application){
         return Room.databaseBuilder(application, ShortcutDatabase.class, ShortcutDatabase.DATABASE_NAME)
                 .fallbackToDestructiveMigration()
                 .allowMainThreadQueries()
                 .build();
    }

    @Provides
    @Singleton
    public static ShortcutDao shortcutDao(ShortcutDatabase database){
        return database.shortcutDao();
    }
}
