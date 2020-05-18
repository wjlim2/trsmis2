package com.example.trsmis2.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trsmis2.R;
import com.example.trsmis2.databinding.ItemTeamSelectBinding;
import com.example.trsmis2.ui.listener.TeamSelectDialogClickListener;

import java.util.ArrayList;
import java.util.logging.Logger;

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

    @Override
    public TeamSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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


    public class TeamSelectViewHolder extends RecyclerView.ViewHolder {

        private ItemTeamSelectBinding mBinding;

        public TeamSelectViewHolder(ItemTeamSelectBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bindView(String jobCdNm, TeamSelectDialogClickListener listener) {
            mBinding.setString(jobCdNm);
            mBinding.teamselectItemLayout.setOnClickListener( v -> {
                mSelectPos = getAdapterPosition();
                notifyDataSetChanged();
                listener.TeamSelectDialogClicked(jobCdNm);
                Log.e(TeamSelectAdapter.class.getSimpleName(), "jobCdNm = " + jobCdNm);
            });
        }
    }
}
