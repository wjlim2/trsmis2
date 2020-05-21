package com.example.trsmis2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.trsmis2.R;
import com.example.trsmis2.databinding.DialogTeamSelectBinding;
import com.example.trsmis2.ui.adapter.TeamSelectAdapter;
import com.example.trsmis2.ui.listener.TeamSelectDialogClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;



public class TeamSelectDialogFragment extends BottomSheetDialogFragment implements TeamSelectDialogClickListener {

    private DialogTeamSelectBinding mBinding;
    private TeamSelectAdapter mAdapter;
    private String mJobCd;
    private String mJobCdNm;
    private ResultListener mListener;

    public static TeamSelectDialogFragment getInstance(String jobCd, String jobCdNm) {
        TeamSelectDialogFragment fragment = new TeamSelectDialogFragment();
        Bundle args = new Bundle();
        args.putString("mJobCd", jobCd);
        args.putString("mJobCdNm", jobCdNm);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(ResultListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ResultListener){
            mListener = (ResultListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_team_select, container, false);
        mJobCd = getArguments().getString("mJobCd", "8");
        mJobCdNm = getArguments().getString("mJobCdNm", "개발팀");
        int position = 0;
        if (mJobCd.equals("8")) {
            position = 0;
        } else if (mJobCd.equals("7")) {
            position = 1;
        } else {
            position = 2;
        }
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getArguments() != null) {
            mAdapter = new TeamSelectAdapter(this, position);
            mBinding.teamRecyclerView.setAdapter(mAdapter);
            mBinding.teamRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        mBinding.dialogTeamBackImage.setOnClickListener(v-> dismiss());
        mBinding.companyDialogConfirmText.setOnClickListener(v -> {
            mListener.onResultClicked(mJobCdNm);
            dismiss();
        });
        return mBinding.getRoot();
    }

    public interface ResultListener {
        void onResultClicked(String jobCdNm);
    }

    @Override
    public void TeamSelectDialogClicked(String jobCdNm) {
        this.mJobCdNm = jobCdNm;
    }
}
