package com.example.trsmis2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.trsmis2.R;
import com.example.trsmis2.base.BaseActivity;
import com.example.trsmis2.databinding.ActivityTrsmisWriteBinding;
import com.example.trsmis2.model.TrsmisApor;
import com.example.trsmis2.ui.dialog.TeamWriteDialogFragment;
import com.example.trsmis2.ui.viewmodel.TrsmisWriteViewModel;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;


public class TrsmisWriteActivity extends BaseActivity implements TeamWriteDialogFragment.ResultListener {

    private String mSelectEmpNm;
    private TrsmisWriteViewModel mViewModel;
    private ActivityTrsmisWriteBinding mBinding;
    private ArrayList<TrsmisApor> mTrsmisAporList;
    private ArrayList<String> mEmpNmList = new ArrayList<>();
    private TeamWriteDialogFragment mDialogFragment;
    private String mJobCd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start();
    }

    private void start() {
        ViewModelProvider.AndroidViewModelFactory mViewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        mViewModel = new ViewModelProvider(this, mViewModelFactory).get(TrsmisWriteViewModel.class);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_trsmis_write);
        mBinding.setLifecycleOwner(this);
        mBinding.setViewmodel(mViewModel);
        mViewModel.init();

        Intent intent = getIntent();
        mJobCd = intent.getStringExtra("jobCd");
        Hawk.put("currentJobCd", mJobCd);

        onClick();
        viewModelObserver();
    }

    private void onClick() {
        mBinding.trsmisWriteBackImage.setOnClickListener(v -> onBackPressed());
        mBinding.trsmisWriteTeamText.setOnClickListener(v -> {
            mDialogFragment = TeamWriteDialogFragment.getInstance(mEmpNmList, mSelectEmpNm);
            mDialogFragment.setListener(this);
            mDialogFragment.show(getSupportFragmentManager(), "");
        });
        mBinding.trsmisWriteRequstLayout.setOnClickListener(v -> {
            long aporId = 0L;
            String prblmText = mBinding.trsmisWriteDisplayEdit.getText().toString();
            for (TrsmisApor t : mTrsmisAporList) {
                if (t.getEmpNm().equals(mSelectEmpNm)) {
                    aporId = Long.parseLong(t.getEmpId());
                    break;
                }
            }
            mViewModel.onRequest(aporId, prblmText, mJobCd);
        });
    }

    private void viewModelObserver() {
        //로딩 프로그레스바
        mViewModel.getLoading().observe(this, integer -> {
            if (integer == View.GONE) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
        //업로드 결과 토스트
        mViewModel.getToastMessage().observe(this, s -> {
            if (!s.isEmpty()) {
                onToast(s);
                onBackPressed();
            }
        });
        //지정자 리스트 추가
        mViewModel.getTrsBizStatusList().observe(this, list -> {
            mSelectEmpNm = list.get(0).getEmpNm();
            for (TrsmisApor apor : list) {
                mEmpNmList.add(apor.getEmpNm());
            }
            mTrsmisAporList = list;
        });
    }

    @Override
    public void onResultClicked(String empNm) {
        this.mSelectEmpNm = empNm;
        mBinding.trsmisWriteTeamText.setText(empNm);
        mDialogFragment.dismiss();
    }
}
