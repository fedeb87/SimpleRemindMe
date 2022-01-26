package com.federicoberon.simpleremindme.ui.milestoneDetail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.federicoberon.simpleremindme.R;
import com.federicoberon.simpleremindme.ViewModelFactory;
import com.federicoberon.simpleremindme.databinding.FragmentMilestoneDetailBinding;
import com.federicoberon.simpleremindme.model.MilestoneXType;
import com.federicoberon.simpleremindme.ui.home.HomeViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MilestoneDetailFragment extends Fragment {

    private static String LOG_TAG = "MilestoneDetailFragment";
    public static final String KEY_MILESTONE_ID = "milestone_id";
    private HomeViewModel viewModel;
    private FragmentMilestoneDetailBinding binding;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, ViewModelFactory.
                getInstance(requireActivity().getApplication())).get(HomeViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMilestoneDetailBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        assert getArguments() != null;
        if (getArguments().containsKey(KEY_MILESTONE_ID)) {

            mDisposable.add(viewModel.getMilestoneById(getArguments().getLong(KEY_MILESTONE_ID))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::populateUI,
                            throwable -> Log.e(LOG_TAG, "Unable to load milestones: ", throwable)));
        }

        return binding.getRoot();
    }

    private void populateUI(MilestoneXType selectedMilestone) {
        if (!(selectedMilestone == null)) {
            binding.textViewTitle.setText(selectedMilestone.getTitle());
            binding.textViewDescription.setText(selectedMilestone.getDesc());
            binding.textViewType.setText(selectedMilestone.getTypeName());
            binding.textViewDate.setText(String.format("%s - %s",
                    DateFormat.getDateFormat(requireActivity().getApplicationContext()).format(selectedMilestone.getMilestoneDate()),
                    DateFormat.getTimeFormat(requireContext()).format(selectedMilestone.getMilestoneDate())));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.milestone_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemEdit:
                // TODO Edit milestone are not current developed
                return true;
            case R.id.itemDelete:
                showConfirmDelete();
                return true;
            default:
                return false;
        }
    }

    private void showConfirmDelete() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(requireActivity().getResources().getString(
                        R.string.delete_milestone_title))
                .setMessage(requireActivity().getResources().getString(
                        R.string.delete_milestone_msg))
                .setPositiveButton(requireActivity().getResources().getString(
                        R.string.ok_button), (dialog, which) -> mDisposable.add(viewModel.deleteMilestone()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> requireActivity().onBackPressed(),
                                        throwable -> Log.e(LOG_TAG, "Unable to update username", throwable))))
                .setNegativeButton(requireActivity().getResources().getString(
                        R.string.cancel_button), null)
                .show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mDisposable.clear();
    }

}