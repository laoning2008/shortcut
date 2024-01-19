package com.byd.shortcut.bridge;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Action implements Parcelable  {
    public long id;
    public int type;
    public String param;
    public String app;
    public String action;

    public Action(long id, int type, String param, String app, String action) {
        this.id = id;
        this.type = type;
        this.param = param;
        this.app = app;
        this.action = action;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(type);
        dest.writeString(param);
        dest.writeString(app);
        dest.writeString(action);
    }

    public static final Parcelable.Creator<Action> CREATOR = new Parcelable.Creator<Action>() {
        @Override
        public Action createFromParcel(Parcel source) {
            return new Action(source.readLong(), source.readInt(), source.readString(), source.readString(), source.readString());
        }

        @Override
        public Action[] newArray(int size) {
            return new Action[size];
        }
    };
}
