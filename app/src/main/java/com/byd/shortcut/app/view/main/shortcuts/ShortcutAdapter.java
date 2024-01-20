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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byd.shortcut.R;
import com.byd.shortcut.app.common.MiscUtils;
import com.byd.shortcut.app.view.navigation.NavigationUtil;
import com.byd.shortcut.bridge.Shortcut;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.util.List;

public class ShortcutAdapter extends RecyclerView.Adapter<ShortcutAdapter.ViewHolder> implements DraggableItemAdapter<ShortcutAdapter.ViewHolder> {
    private List<Shortcut> data;
    private AllShortcutViewModel viewModel;

    public static class ViewHolder extends AbstractDraggableItemViewHolder {
        public View mContainer;
        public View mDragHandle;

        public TextView mTitle;
        public ImageView mImageRemove;

        public ViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.container);
            mTitle = v.findViewById(R.id.tv_title);
            mImageRemove = v.findViewById(R.id.im_delete);
        }
    }

    public ShortcutAdapter(List<Shortcut> data, AllShortcutViewModel viewModel) {
        this.data = data;
        this.viewModel = viewModel;

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
        return MiscUtils.uniqueIdFromString(data.get(position).id);
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item_shortcut, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Shortcut shortcut = data.get(position);
        holder.mTitle.setText(shortcut.title);

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtil.navigateToShortcutDetail(holder.mContainer.getContext(), shortcut);
            }
        });

        holder.mImageRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteShortcut(shortcut);
            }
        });
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

        viewModel.moveShortcut(fromPosition, toPosition);
    }

    @Override
    public boolean onCheckCanStartDrag(@NonNull ViewHolder holder, int position, int x, int y) {
        return true;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(@NonNull ViewHolder holder, int position) {
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
