package com.federicoberon.simpleremindme.ui.addMilestone;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.federicoberon.simpleremindme.databinding.ItemTypesBinding;
import com.federicoberon.simpleremindme.model.MilestoneType;

import java.util.Collections;
import java.util.List;

public class MilestoneTypeAdapter extends RecyclerView.Adapter<MilestoneTypeAdapter.MilestoneTypeViewHolder>{

    private List<MilestoneType> types;
    private int selectedPosition;

    public MilestoneTypeAdapter() {
        types = Collections.emptyList();
        selectedPosition = -1;
    }

    @NonNull
    @Override
    public MilestoneTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTypesBinding itemMilestoneBinding = ItemTypesBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MilestoneTypeViewHolder(itemMilestoneBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MilestoneTypeViewHolder holder, int position) {
        final int _position = position;
        holder.onBind(position);

        holder.itemView.setOnClickListener(view -> {
            long milestoneId = (long) view.getTag();
            Bundle args = new Bundle();
            args.putLong(AddMilestoneActivity.KEY_TYPE_ID, milestoneId);
            selectedPosition = _position;
            notifyItemRangeChanged(0, getItemCount());
        });

        if(selectedPosition == _position){
            setItemSelected(holder.itemView, types.get(_position).getColor(), holder.mBinding.textViewTypeTitle);
        } else {
            holder.mBinding.textViewTypeTitle.setTextColor(Color.parseColor("#808080"));
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    private void setItemSelected(View view, String colorType, TextView textViewTypeTitle) {
        textViewTypeTitle.setTextColor(Color.WHITE);
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] { Color.parseColor(colorType), Color.WHITE });
        g.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        g.setGradientRadius(400.0f);
        g.setGradientCenter(0.5f, 0.45f);
        view.setBackground(g);
    }

    public MilestoneType getItemSelected(){
        return selectedPosition == -1? types.get(selectedPosition): types.get(0);
    }


    @Override
    public int getItemCount() {
        return types == null? 0: types.size();
    }

    public void setTypes(List<MilestoneType> types) {
        this.types = types;
        notifyItemRangeChanged(0, getItemCount());
    }

    public class MilestoneTypeViewHolder extends RecyclerView.ViewHolder {
        private final ItemTypesBinding mBinding;

        public MilestoneTypeViewHolder(ItemTypesBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            // Make this view clickable
            itemView.setClickable(true);
        }

        public void onBind(int position) {
            MilestoneType type = types.get(position);
            itemView.setTag(type.getId());
            mBinding.textViewTypeTitle.setText(type.getTitle());
            itemView.setTag(type.getId());
        }
    }
}
