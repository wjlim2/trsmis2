package com.example.trsmis2.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trsmis2.R;
import com.example.trsmis2.databinding.ItemTeamWriteBinding;
import com.example.trsmis2.ui.listener.TeamWriteDialogClickListener;

import java.util.ArrayList;

/**
 * Created by sy-02 on 2018-04-05.
 */

public class TeamWriteAdapter extends RecyclerView.Adapter<TeamWriteAdapter.TeamWriteViewHolder> {

    private ArrayList<String> mEmpNmList;
    private TeamWriteDialogClickListener mListener;
    private int mSelectPos = 0;


    public TeamWriteAdapter(TeamWriteDialogClickListener listener, ArrayList<String> empNmList, int position) {
        mListener = listener;
        this.mEmpNmList = empNmList;
        mSelectPos = position;
    }

    @Override
    public TeamWriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTeamWriteBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_team_write, parent, false);
        return new TeamWriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamWriteViewHolder holder, int position) {
        holder.bindView(mEmpNmList.get(position), mListener);
        holder.mBinding.teamselectItemLayout.setSelected(mSelectPos == position);
    }

    @Override
    public int getItemCount() {
        return mEmpNmList.size();
    }

    class TeamWriteViewHolder extends RecyclerView.ViewHolder {

        private ItemTeamWriteBinding mBinding;

        TeamWriteViewHolder(ItemTeamWriteBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindView(String empNm, TeamWriteDialogClickListener listener) {
            mBinding.setString(empNm);
            mBinding.teamselectItemLayout.setOnClickListener(v -> {
                notifyItemChanged(mSelectPos);
                mSelectPos = getAdapterPosition();
                notifyItemChanged(getAdapterPosition());
                listener.TeamWriteDialogClicked(empNm);
            });
        }
    }

}
