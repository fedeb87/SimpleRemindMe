package com.federicoberon.simpleremindme.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.federicoberon.simpleremindme.R;
import com.federicoberon.simpleremindme.databinding.ItemMilestoneBinding;
import com.federicoberon.simpleremindme.model.MilestoneXType;
import com.federicoberon.simpleremindme.ui.milestoneDetail.MilestoneDetailFragment;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MilestoneAdapter extends RecyclerView.Adapter<MilestoneAdapter.MilestoneViewHolder>{

    private List<MilestoneXType> milestones;
    private final View parentView;

    public MilestoneAdapter(View root) {
        this.milestones = Collections.emptyList();
        parentView = root;
    }

    @NonNull
    @Override
    public MilestoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMilestoneBinding itemMilestoneBinding = ItemMilestoneBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new MilestoneViewHolder(itemMilestoneBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MilestoneViewHolder holder, int position) {
        holder.onBind(position);

        holder.itemView.setOnClickListener(view -> {
            long milestoneId = (long) view.getTag();
            Bundle args = new Bundle();
            args.putLong(MilestoneDetailFragment.KEY_MILESTONE_ID, milestoneId);

            View itemDetailFragmentContainer = parentView!=null?parentView.findViewById(R.id.item_detail_nav_container):null;
            if (itemDetailFragmentContainer != null) {
                Navigation.findNavController(itemDetailFragmentContainer)
                        .navigate(R.id.fragment_item_detail, args);
            } else {
                Navigation.findNavController(view).navigate(R.id.show_milestone_detail, args);
            }
        });
    }

    public void setMilestones(List<MilestoneXType> milestones) {
        this.milestones = milestones;
        final MilestoneDiffCallback diffCallback = new MilestoneDiffCallback(this.milestones, milestones);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);
    }


    @Override
    public int getItemCount() {
        return milestones.size();
    }

    public class MilestoneViewHolder extends RecyclerView.ViewHolder {
        private final ItemMilestoneBinding mBinding;

        public MilestoneViewHolder(ItemMilestoneBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        public void onBind(int position) {
            MilestoneXType milestone = milestones.get(position);
            mBinding.textCardTitle.setText(milestone.getTitle());
            mBinding.textCardDescription.setText(milestone.getDesc());

            itemView.setTag(milestone.getId());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(milestone.getMilestoneDate());
            mBinding.textDate.setText(String.format("%s %s\n%s",
                    DateFormat.format("dd", calendar),
                    DateFormat.format("MMM", calendar),
                    DateFormat.format("yyyy", calendar)));

            mBinding.textType.setText(String.valueOf(milestone.getTypeName()));

            mBinding.textCardTitle.setTextColor(Color.parseColor(milestone.getColor()));
        }
    }
}
