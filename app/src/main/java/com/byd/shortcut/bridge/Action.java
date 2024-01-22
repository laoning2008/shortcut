package com.byd.shortcut.bridge;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Action implements Parcelable  {
    public static final int ACTION_TYPE_CLIPBOARD = 0;
    public static final int ACTION_TYPE_CONST_VALUE = 1;
    public static final int ACTION_TYPE_DYNAMIC_VALUE = 2;
    public static final int ACTION_TYPE_ACTION_RESULT = 3;

    public long id;
    public int type;
    public String paramValue;
    public String paramTitle;
    public int paramAction = 0;
    public String app;
    public String action;

    public Action(long id, int type, String paramValue, String paramTitle, int paramAction, String app, String action) {
        this.id = id;
        this.type = type;
        this.paramValue = paramValue;
        this.paramTitle = paramTitle;
        this.paramAction = paramAction;
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
        dest.writeString(paramValue);
        dest.writeString(paramTitle);
        dest.writeInt(paramAction);
        dest.writeString(app);
        dest.writeString(action);
    }

    public static final Parcelable.Creator<Action> CREATOR = new Parcelable.Creator<Action>() {
        @Override
        public Action createFromParcel(Parcel source) {
            return new Action(source.readLong(), source.readInt(), source.readString(), source.readString(), source.readInt(), source.readString(), source.readString());
        }

        @Override
        public Action[] newArray(int size) {
            return new Action[size];
        }
    };
}
