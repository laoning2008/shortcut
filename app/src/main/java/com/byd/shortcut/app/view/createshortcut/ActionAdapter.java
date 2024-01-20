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

package com.byd.shortcut.app.view.createshortcut;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byd.shortcut.R;
import com.byd.shortcut.bridge.Action;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder> implements DraggableItemAdapter<ActionAdapter.ViewHolder> {
    private List<Action> data;
    private CreateShortcutViewModel viewModel;

    public static class ViewHolder extends AbstractDraggableItemViewHolder {
        public View mContainer;
        public View mDragHandle;

        public EditText mEditTextParam;
        public EditText mEditTextApp;
        public EditText mEditTextAction;
        public ImageView mImageRemove;

        public ViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.container);
            mEditTextParam = v.findViewById(R.id.et_param);
            mEditTextApp = v.findViewById(R.id.et_package);
            mEditTextAction = v.findViewById(R.id.et_action);
            mImageRemove = v.findViewById(R.id.im_delete);
        }
    }

    public ActionAdapter(List<Action> data, CreateShortcutViewModel viewModel) {
        this.data = data;
        this.viewModel = viewModel;

        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }


    public void setData(List<Action> data) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item_action, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Action action = data.get(position);

        // set text
        holder.mEditTextParam.setText(action.param);
        holder.mEditTextApp.setText(action.app);
        holder.mEditTextAction.setText(action.action);

        holder.mEditTextParam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setActionParam(action.id, holder.mEditTextParam.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        holder.mEditTextApp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setActionApp(action.id, holder.mEditTextApp.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        holder.mEditTextAction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setActionAction(action.id, holder.mEditTextAction.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        holder.mImageRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteAction(action);
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
//        // x, y --- relative from the itemView's top-left
//        final View containerView = holder.mContainer;
//        final View dragHandleView = holder.mDragHandle;
//
//        final int offsetX = containerView.getLeft() + (int) (containerView.getTranslationX() + 0.5f);
//        final int offsetY = containerView.getTop() + (int) (containerView.getTranslationY() + 0.5f);
//
//        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
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
