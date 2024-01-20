package com.byd.shortcut.bridge;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class Shortcut implements Parcelable {
    public String id;
    public long modifiedTime = 0L;
    public int type = 0;
    public String title;
    public ArrayList<Action> tasks;

    public Shortcut(String id, long modifiedTime, int type, String title, ArrayList<Action> tasks) {
        this.id = id;
        this.modifiedTime = modifiedTime;
        this.type = type;
        this.title = title;
        this.tasks = tasks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(modifiedTime);
        dest.writeInt(type);
        dest.writeString(title);
        int size = (tasks == null) ? 0 : tasks.size();
        dest.writeInt(size);
        for (Action action : tasks) {
            dest.writeValue(action);
        }
    }

    public static final Parcelable.Creator<Shortcut> CREATOR = new Parcelable.Creator<Shortcut>() {
        @Override
        public Shortcut createFromParcel(Parcel source) {
            String id = source.readString();
            long modifiedTime = source.readLong();
            int type = source.readInt();
            String title = source.readString();
            ArrayList<Action> actions = new ArrayList<Action>();
            int size = source.readInt();
            for (int i = 0; i < size; ++i) {
                Action action = (Action)source.readValue(Action.class.getClassLoader());
                actions.add(action);
            }
            return new Shortcut(id, modifiedTime, type, title, actions);
        }

        @Override
        public Shortcut[] newArray(int size) {
            return new Shortcut[size];
        }
    };
}