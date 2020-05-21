package com.example.trsmis2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
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
import com.example.trsmis2.databinding.DialogTeamWriteBinding;
import com.example.trsmis2.ui.adapter.TeamWriteAdapter;
import com.example.trsmis2.ui.listener.TeamWriteDialogClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

/**
 * Created by sy-02 on 2018-04-05.
 */

public class TeamWriteDialogFragment extends BottomSheetDialogFragment implements TeamWriteDialogClickListener {

    private String mEmpNm;
    private DialogTeamWriteBinding mBinding;
    private TeamWriteAdapter mAdapter;
    private ResultListener mListener;
    private ArrayList<String> mEmpNmList;

    public static TeamWriteDialogFragment getInstance(ArrayList<String> empNmList, String empNm) {
        TeamWriteDialogFragment fragment = new TeamWriteDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("empNmList", empNmList);
        args.putString("empNm", empNm);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(ResultListener listener) {
        mListener = listener;
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_team_write, container, false);
        mEmpNmList = getArguments().getStringArrayList("empNmList");
        mEmpNm = getArguments().getString("empNm", "");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        int position;
        if (mEmpNm.isEmpty()) {
            position = 0;
        } else {
            position = mEmpNmList.indexOf(mEmpNm);
        }
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAdapter = new TeamWriteAdapter(this, mEmpNmList, position);
        mBinding.teamWriteRecyclerView.setAdapter(mAdapter);
        mBinding.teamWriteRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mBinding.dialogTeamWriteBackImage.setOnClickListener(v -> dismiss());
        mBinding.companyDialogConfirmLayout.setOnClickListener(v -> {
            mListener.onResultClicked(mEmpNm);
        });
        return mBinding.getRoot();
    }


    public interface ResultListener {
        //void onResultClicked(String jobCd);
        void onResultClicked(String empNm);
    }

    @Override
    public void TeamWriteDialogClicked(String empNm) {
        this.mEmpNm = empNm;
    }

   /* @Override
    public void TeamSelectDialogClicked(String jobCdNm) {
        if (jobCdNm != null) {
            this.mJobCdNm = jobCdNm;
            mListener.onResultClicked(mJobCdNm);
        }else{
            mListener.onResultClicked(mJobCdNm);
        }
    }*/
}
