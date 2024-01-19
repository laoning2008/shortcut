package com.byd.shortcut.app.view.createshortcut;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.byd.shortcut.R;
import com.byd.shortcut.app.common.BaseActivity;
import com.byd.shortcut.app.common.ConnectorListDividerDecorator;
import com.byd.shortcut.bridge.Action;
import com.byd.shortcut.databinding.ActivityCreateShortcutBinding;
import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateShortcutActivity extends BaseActivity {

    private ActivityCreateShortcutBinding binding;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private CreateShortcutViewModel viewModel;
    private ActionAdapter actionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CreateShortcutViewModel.class);

        binding = ActivityCreateShortcutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(R.string.title_create_shortcut);

        binding.etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setTitle(binding.etTitle.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        viewModel.getTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.equals(binding.etTitle.getText().toString())) {
                    binding.etTitle.setText(s);
                }
            }
        });

        mRecyclerView = binding.recyclerViewCreateShortcut;
        mLayoutManager = new LinearLayoutManager(this);

        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setInitiateOnLongPress(true);
        mRecyclerViewDragDropManager.setInitiateOnMove(false);

        //adapter
        actionAdapter = new ActionAdapter(viewModel.getActons().getValue(), viewModel);
        viewModel.getActons().observe(this, new Observer<List<Action>>() {
            @Override
            public void onChanged(List<Action> actions) {
                actionAdapter.setData(actions);
            }
        });

        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(actionAdapter);

        final GeneralItemAnimator animator = new DraggableItemAnimator();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        mRecyclerView.addItemDecoration(new ConnectorListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider_h)));

        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
    }

    @Override
    public void onPause() {
        mRecyclerViewDragDropManager.cancelDrag();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        actionAdapter = null;
        mLayoutManager = null;

        binding = null;
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_shortcut_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.toolbar_menu_create_shortcut_cancel) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.toolbar_menu_create_shortcut_done) {
            viewModel.saveShortcut();
            finish();
            return true;
        } else if (item.getItemId() == R.id.toolbar_menu_create_shortcut_add_action) {
            addNewAction();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNewAction() {
        viewModel.addNewAction();
    }
}