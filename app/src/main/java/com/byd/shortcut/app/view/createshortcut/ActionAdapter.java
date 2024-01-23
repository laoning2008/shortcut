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

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byd.shortcut.App;
import com.byd.shortcut.R;
import com.byd.shortcut.bridge.Action;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder> implements DraggableItemAdapter<ActionAdapter.ViewHolder> {
    private List<Action> data;
    private CreateShortcutViewModel viewModel;
    private static final String[] PARAM_TYPE = {"剪贴板", "常量", "动态输入", "获取前第N个Action的结果"};
    private List<String> packages = new ArrayList<>();;

    public static class ViewHolder extends AbstractDraggableItemViewHolder {
        public View mContainer;
        public View mDragHandle;

        public Spinner mSpinnerParamType;
        public Spinner mSpinnerParamAction;
        public EditText mEditTextParam;
        public Spinner mSpinnerApp;
        public EditText mEditTextAction;
        public ImageView mImageRemove;

        public ViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.container);
            mSpinnerParamType = v.findViewById(R.id.spinner_param_type);
            mSpinnerParamAction = v.findViewById(R.id.spinner_param_action);
            mEditTextParam = v.findViewById(R.id.et_param_value);
            mSpinnerApp = v.findViewById(R.id.spinner_package);
            mEditTextAction = v.findViewById(R.id.et_action);
            mImageRemove = v.findViewById(R.id.im_delete);
        }
    }

    public ActionAdapter(List<Action> data, CreateShortcutViewModel viewModel) {
        this.data = data;
        this.viewModel = viewModel;

        getPackages();

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


    private void getPackages() {
        PackageManager pm = App.getContext().getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        for (PackageInfo info : apps) {
            if (info.activities == null) {
                continue;
            }

            packages.add(info.packageName);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Action action = data.get(position);

        // set text
        if (action.type == Action.ACTION_TYPE_CONST_VALUE) {
            holder.mEditTextParam.setText(action.paramValue);
        } else if (action.type == Action.ACTION_TYPE_DYNAMIC_VALUE) {
            holder.mEditTextParam.setText(action.paramTitle);
        }

        holder.mEditTextAction.setText(action.action);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.mSpinnerApp.getContext(), R.layout.spinner_row, packages);
        holder.mSpinnerApp.setAdapter(adapter);

        int selected = adapter.getPosition(action.app);
        selected = Math.max(selected, 0);
        holder.mSpinnerApp.setSelection(selected);

        ArrayAdapter<String> adapterParamType = new ArrayAdapter<String>(holder.mSpinnerParamType.getContext(), R.layout.spinner_row, PARAM_TYPE);
        holder.mSpinnerParamType.setAdapter(adapterParamType);
        holder.mSpinnerParamType.setSelection(action.type);

        List<String> actionList = new ArrayList<>();
        for (int i = 0; i < position; ++i) {
            actionList.add("第" + String.valueOf(i) + "个Action");
        }
        ArrayAdapter<String> adapterParamAction = new ArrayAdapter<String>(holder.mSpinnerParamType.getContext(), R.layout.spinner_row, actionList);
        holder.mSpinnerParamAction.setAdapter(adapterParamAction);
        if (action.type == Action.ACTION_TYPE_ACTION_RESULT) {
            int index = 0;
            if (action.paramAction < actionList.size()) {
                index = action.paramAction;
            }
            holder.mSpinnerParamAction.setSelection(index);
        }

        holder.mEditTextParam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (action.type == Action.ACTION_TYPE_CONST_VALUE) {
                    viewModel.setActionParamValue(action.id, holder.mEditTextParam.getText().toString());
                } else if (action.type == Action.ACTION_TYPE_DYNAMIC_VALUE) {
                    viewModel.setActionParamTitle(action.id, holder.mEditTextParam.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        holder.mSpinnerParamType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    holder.mEditTextParam.setVisibility(View.INVISIBLE);
                    holder.mSpinnerParamAction.setVisibility(View.INVISIBLE);
                } else if (id == 1) {
                    holder.mEditTextParam.setVisibility(View.VISIBLE);
                    holder.mSpinnerParamAction.setVisibility(View.INVISIBLE);
                    holder.mEditTextParam.setHint("输入参数");
                } else if (id == 2) {
                    holder.mEditTextParam.setVisibility(View.VISIBLE);
                    holder.mSpinnerParamAction.setVisibility(View.INVISIBLE);
                    holder.mEditTextParam.setHint("输入参数说明");
                } else if (id == 3) {
                    holder.mEditTextParam.setVisibility(View.INVISIBLE);
                    holder.mSpinnerParamAction.setVisibility(View.VISIBLE);
                } else {
                    return;
                }

                viewModel.setActionParamType(action.id, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        holder.mSpinnerParamAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (action.type == Action.ACTION_TYPE_ACTION_RESULT) {
                    viewModel.setActionParamAction(action.id, (int)id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        holder.mSpinnerApp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setActionApp(action.id, (String) holder.mSpinnerApp.getAdapter().getItem((int)id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
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
