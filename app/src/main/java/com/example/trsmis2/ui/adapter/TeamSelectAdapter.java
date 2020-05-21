package com.example.trsmis2.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trsmis2.R;
import com.example.trsmis2.databinding.ItemTeamSelectBinding;
import com.example.trsmis2.ui.listener.TeamSelectDialogClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class TeamSelectAdapter extends RecyclerView.Adapter<TeamSelectAdapter.TeamSelectViewHolder> { //TODO TeamSelectAdapter 검토

    private ArrayList<String> mJobCdNmList;
    private TeamSelectDialogClickListener mListener;
    private int mSelectPos = 0;

    public TeamSelectAdapter(TeamSelectDialogClickListener listener, int position) {
        mListener = listener;
        if (mJobCdNmList == null) {
            mJobCdNmList = new ArrayList<>();
            mJobCdNmList.add("개발팀");
            mJobCdNmList.add("공고팀");
            mJobCdNmList.add("경영지원팀");
        }
        mSelectPos = position;
    }

    @NotNull
    @Override
    public TeamSelectViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ItemTeamSelectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_team_select, parent, false);
        return new TeamSelectViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TeamSelectViewHolder holder, int position) {
        holder.bindView(mJobCdNmList.get(position), mListener);
        holder.mBinding.teamselectItemLayout.setSelected(mSelectPos == position);
    }

    @Override
    public int getItemCount() {
        return mJobCdNmList.size();
    }

    public void setItem(ArrayList<String> models) {
        mJobCdNmList = models;
    }


    class TeamSelectViewHolder extends RecyclerView.ViewHolder {

        private ItemTeamSelectBinding mBinding;

        TeamSelectViewHolder(ItemTeamSelectBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindView(String jobCdNm, TeamSelectDialogClickListener listener) {
            mBinding.setString(jobCdNm);
            mBinding.teamselectItemLayout.setOnClickListener( v -> {
                mSelectPos = getAdapterPosition();
                notifyDataSetChanged();
                listener.TeamSelectDialogClicked(jobCdNm);
            });
        }
    }
}
