package com.example.trsmis2.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trsmis2.R;
import com.example.trsmis2.databinding.ItemAtchmnflBinding;
import com.example.trsmis2.model.TrsmisAtchmnfl;
import com.example.trsmis2.ui.activity.TrsmisDetailActivity;
import com.example.trsmis2.ui.adapter.TrsmisDetailViewHolder.TrsmisDetailViewHolder;
import com.example.trsmis2.ui.listener.TeamWriteDialogClickListener;
import com.example.trsmis2.ui.listener.TrsmisAtchmnflItemClickListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class TrsmisDetailAdapter extends RecyclerView.Adapter<TrsmisDetailViewHolder>{

    public TrsmisDetailAdapter(ArrayList<TrsmisAtchmnfl> mFileList) {
        this.mFileList = mFileList;
    }

    private ArrayList<TrsmisAtchmnfl> mFileList;
    private TrsmisAtchmnflItemClickListener mListener;

    public void setItemClickListener(TrsmisAtchmnflItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public TrsmisDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAtchmnflBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_atchmnfl, parent, false);
        return new TrsmisDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrsmisDetailViewHolder holder, int position) {
        holder.onBindView(mFileList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        Logger.d(mFileList.size());
        return mFileList.size();
    }

}
