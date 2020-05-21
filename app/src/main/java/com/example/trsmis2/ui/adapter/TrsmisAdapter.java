package com.example.trsmis2.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trsmis2.R;
import com.example.trsmis2.databinding.ItemTrsmisBinding;
import com.example.trsmis2.model.TrsmisFormatModel;
import com.example.trsmis2.ui.adapter.TrsmisDetailViewHolder.TrsmisViewHolder;
import com.example.trsmis2.ui.listener.TrsmisItemClickListener;

import java.util.ArrayList;

public class TrsmisAdapter extends RecyclerView.Adapter<TrsmisViewHolder> {

    private ArrayList<TrsmisFormatModel> mModel = new ArrayList<>();
    private TrsmisItemClickListener mListener;

    public void setItemClickListener(TrsmisItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public TrsmisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTrsmisBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_trsmis, parent, false);
        return new TrsmisViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrsmisViewHolder holder, int position) {
        holder.onBindView(mModel.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }

    public void removeAllItem(){
        mModel.clear();
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<TrsmisFormatModel> models){
        mModel.addAll(models);
        notifyItemRangeChanged(mModel.size() - 1, models.size());
    }
}
