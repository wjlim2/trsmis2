package com.example.trsmis2.ui.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trsmis2.model.Trsmis;
import com.example.trsmis2.ui.adapter.viewholder.TrsmisViewHolder;
import com.example.trsmis2.ui.listener.TrsmisItemClickListener;

import java.util.ArrayList;

public class TrsmisAdapter extends RecyclerView.Adapter<TrsmisViewHolder> {

    private ArrayList<Trsmis> mModels;
    private int mTrsmisCnt; // 카운트
    private TrsmisItemClickListener mListener;

    public void setItemClickListener(TrsmisItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public TrsmisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TrsmisViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void removeAllItem(){

    }

    public void addItem(ArrayList<Trsmis> models){

    }
}
