package com.federicoberon.simpleremindme.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.federicoberon.simpleremindme.ViewModelFactory;
import com.federicoberon.simpleremindme.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // TODO this fragment is not developed

        AboutViewModel aboutViewModel = new ViewModelProvider(this, ViewModelFactory.
                getInstance(requireActivity().getApplication())).get(AboutViewModel.class);

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        aboutViewModel.getText().observe(getViewLifecycleOwner(), s -> binding.textAbout.setText(s));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}