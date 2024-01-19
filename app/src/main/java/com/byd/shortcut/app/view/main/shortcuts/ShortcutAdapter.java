/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.byd.shortcut.app.view.main.shortcuts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byd.shortcut.R;
import com.byd.shortcut.app.common.ViewUtils;
import com.byd.shortcut.bridge.Shortcut;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.util.List;

public class ShortcutAdapter extends RecyclerView.Adapter<ShortcutAdapter.MyViewHolder> implements DraggableItemAdapter<ShortcutAdapter.MyViewHolder> {
    private List<Shortcut> data;

    public static class MyViewHolder extends AbstractDraggableItemViewHolder {
        public View mContainer;
        public View mDragHandle;

        public TextView mTitle;

        public MyViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.container);
            mTitle = v.findViewById(R.id.tv_title);
        }
    }

    public ShortcutAdapter(List<Shortcut> data) {
        this.data = data;

        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }


    public void setData(List<Shortcut> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).id;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @NonNull
    @Override
    public ShortcutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item_shortcut, parent, false);
        return new ShortcutAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortcutAdapter.MyViewHolder holder, int position) {
        final Shortcut item = data.get(position);
        holder.mTitle.setText(item.title);
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }

        return data.size();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        Shortcut item = data.remove(fromPosition);
        data.add(toPosition, item);
    }

    @Override
    public boolean onCheckCanStartDrag(@NonNull ShortcutAdapter.MyViewHolder holder, int position, int x, int y) {
        return true;
//        // x, y --- relative from the itemView's top-left
//        final View containerView = holder.mContainer;
//        final View dragHandleView = holder.mDragHandle;
//
//        final int offsetX = containerView.getLeft() + (int) (containerView.getTranslationX() + 0.5f);
//
//        final int offsetY = containerView.getTop() + (int) (containerView.getTranslationY() + 0.5f);
//
//        ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(@NonNull ShortcutAdapter.MyViewHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public void onItemDragStarted(int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        notifyDataSetChanged();
    }
}
