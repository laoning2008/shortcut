package com.byd.shortcut.app.view.main.shortcutcenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.byd.shortcut.databinding.FragmentShortcutCenterBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ShortcutCenterFragment extends Fragment {

    private FragmentShortcutCenterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ShortcutCenterViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ShortcutCenterViewModel.class);

        binding = FragmentShortcutCenterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}