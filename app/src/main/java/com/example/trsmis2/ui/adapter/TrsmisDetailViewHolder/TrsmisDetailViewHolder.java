package com.example.trsmis2.ui.adapter.TrsmisDetailViewHolder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trsmis2.databinding.ItemAtchmnflBinding;
import com.example.trsmis2.model.TrsmisAtchmnfl;
import com.example.trsmis2.ui.listener.TrsmisAtchmnflItemClickListener;

public class TrsmisDetailViewHolder extends RecyclerView.ViewHolder {
    private ItemAtchmnflBinding mBinding;

    public TrsmisDetailViewHolder(@NonNull ItemAtchmnflBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }

    public void onBindView(TrsmisAtchmnfl model, TrsmisAtchmnflItemClickListener listener) {
        mBinding.setModel(model);
        mBinding.setListener(listener);
    }
}


