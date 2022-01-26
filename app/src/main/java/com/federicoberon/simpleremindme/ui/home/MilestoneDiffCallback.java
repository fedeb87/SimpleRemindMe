package com.federicoberon.simpleremindme.ui.home;

import androidx.recyclerview.widget.DiffUtil;

import com.federicoberon.simpleremindme.model.MilestoneXType;

import java.util.List;

public class MilestoneDiffCallback extends DiffUtil.Callback{
    private final List<MilestoneXType> mOldMilestoneList;
    private final List<MilestoneXType> mNewMilestoneList;

    public MilestoneDiffCallback(List<MilestoneXType> oldEmployeeList, List<MilestoneXType> newEmployeeList) {
        this.mOldMilestoneList = oldEmployeeList;
        this.mNewMilestoneList = newEmployeeList;
    }

    @Override
    public int getOldListSize() {
        return mOldMilestoneList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewMilestoneList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldMilestoneList.get(oldItemPosition).getId() == mNewMilestoneList.get(
                newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final MilestoneXType oldMilestone = mOldMilestoneList.get(oldItemPosition);
        final MilestoneXType newMilestone = mNewMilestoneList.get(newItemPosition);

        return oldMilestone.equals(newMilestone);
    }
}

