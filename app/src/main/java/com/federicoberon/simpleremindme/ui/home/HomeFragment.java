package com.federicoberon.simpleremindme.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.federicoberon.simpleremindme.ViewModelFactory;
import com.federicoberon.simpleremindme.databinding.FragmentHomeBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    private static final String LOG_TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private MilestoneAdapter adapter;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this, ViewModelFactory.
                getInstance(requireActivity().getApplication())).get(HomeViewModel.class);

        binding.buttonSearch.setOnClickListener(v -> {
            Editable query = binding.editTextSearch.getText();
            if (!query.toString().isEmpty()) filterMilestones(query.toString());
        });

        binding.buttonCancelSearch.setOnClickListener(v -> cleanMilestoneFilter());

        setupRecyclerView(binding.milestonesRecyclerView);
    }

    private void cleanMilestoneFilter() {
        binding.buttonSearch.setVisibility(View.VISIBLE);
        binding.buttonCancelSearch.setVisibility(View.GONE);
        binding.editTextSearch.setText("");
        binding.editTextSearch.clearFocus();
        showAllMilestones();
    }

    /**
     * Get all milestones without filters
     */
    private void showAllMilestones() {
        mDisposable.add(homeViewModel.getMilestones("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(milestones -> adapter.setMilestones(milestones),
                        throwable -> Log.e(LOG_TAG, "Unable to get milestones: ", throwable)));
    }

    private void filterMilestones(String query) {
        mDisposable.add(homeViewModel.getMilestones(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(milestones -> {
                            hideSoftKeyboard();
                            showCancelButton();
                            adapter.setMilestones(milestones);
                        },
                        throwable -> Log.e(LOG_TAG, "Unable to load milestones: ", throwable)));
    }

    private void showCancelButton() {
        binding.buttonSearch.setVisibility(View.GONE);
        binding.buttonCancelSearch.setVisibility(View.VISIBLE);

    }

    @Override
    public void onStart() {
        super.onStart();
        cleanMilestoneFilter();
    }

    private void setupRecyclerView(
            RecyclerView recyclerView) {
        adapter = new MilestoneAdapter(
                binding.getRoot());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // clear all the subscriptions
        mDisposable.clear();
        binding = null;
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.editTextSearch.getWindowToken(), 0);
    }
}