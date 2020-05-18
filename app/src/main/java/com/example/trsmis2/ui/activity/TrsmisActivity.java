package com.example.trsmis2.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.trsmis2.R;
import com.example.trsmis2.base.BaseActivity;
import com.example.trsmis2.databinding.ActivityTrsmisBinding;
import com.example.trsmis2.model.Emp;
import com.example.trsmis2.model.Trsmis;
import com.example.trsmis2.ui.adapter.TrsmisAdapter;
import com.example.trsmis2.ui.dialog.ErrorDialogFragment;
import com.example.trsmis2.ui.dialog.TeamSelectDialogFragment;
import com.example.trsmis2.ui.listener.TrsmisItemClickListener;
import com.example.trsmis2.ui.viewmodel.TrsmisViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.example.trsmis2.util.AppUtil.*;
import com.example.trsmis2.util.RecyclerViewScrollListener;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;

import static com.example.trsmis2.util.AppUtil.changeDateFormat;
import static com.example.trsmis2.util.AppUtil.dayCalculator;
import static com.example.trsmis2.util.AppUtil.getDateDay;
import static com.example.trsmis2.util.AppUtil.getDateToString;
import static com.example.trsmis2.util.AppUtil.getDayAndDate;
import static com.example.trsmis2.util.AppUtil.getStringToDate;
import static com.example.trsmis2.util.AppUtil.jobCdToNm;
import static com.example.trsmis2.util.AppUtil.jobNmToCd;
import static com.example.trsmis2.util.AppUtil.monthCalculator;
import static com.example.trsmis2.util.AppUtil.testEmp;
import static com.example.trsmis2.util.AppUtil.todayDateString;

public class TrsmisActivity extends BaseActivity implements TeamSelectDialogFragment.ResultListener, View.OnClickListener, TrsmisItemClickListener {

    private String mCurrentDate = todayDateString();// 오늘 날짜를 yyyy-MM-dd 포맷으로 저장.
    private DatePickerDialog mDatePickerDialog;
    private String mJobCdNm, mJobCd, mTeamNm;
    private ActivityTrsmisBinding mBinding;
    private TrsmisViewModel mViewModel;
    private TrsmisAdapter mAdapter;
    private Calendar cal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        createViewModel();// 뷰모델 생성
        getEmp();// 앱 사용자 직원정보
        dataBinding();// 바인딩 초기화
        setAdapter();// 리사이클러뷰 어댑터 세팅
        mViewModel.onListCall(mCurrentDate, mJobCd);
        viewModelObserver();// 뷰모델 옵저버
        onRecyclerViewScrolledListener();
    }

    private void dataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_trsmis);
        mBinding.setLifecycleOwner(this);
        mBinding.setViewModel(mViewModel);
        mBinding.trsmisTeamSelectText.setOnClickListener(this);
        mBinding.trsmisDateLayout.setOnClickListener(this);
        mBinding.trsmisRightImage.setOnClickListener(this);
        mBinding.trsmisWriteImage.setOnClickListener(this);
        mBinding.trsmisBackImage.setOnClickListener(this);
        mBinding.trsmisLeftImage.setOnClickListener(this);
        mBinding.setDate(getDayAndDate(mCurrentDate));
        mBinding.setTeam(mTeamNm);
    }

    private void createViewModel() {
        ViewModelProvider.AndroidViewModelFactory mViewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        mViewModel = new ViewModelProvider(this, mViewModelFactory).get(TrsmisViewModel.class);
    }

    private void setAdapter() {
        mAdapter = new TrsmisAdapter();
        mAdapter.setItemClickListener(this);
        mBinding.trsmisRecycler.setAdapter(mAdapter);
    }

    private void getEmp() {
        Emp emp = testEmp();
        mJobCdNm = emp.getJobCdNm();
        mJobCd = emp.getJobCd();
        mTeamNm= emp.getTeamNm();
    }

    private void viewModelObserver() {

        mViewModel.getTrsmisCnt().observe(this, count -> {

        });
        // 요청사항 리스트 변화 감지
        mViewModel.getTrsmis().observe(this, trsmis -> {
            if (mViewModel.getTrsmisReqModel().getCurrentPage().equals("1")) {
                mAdapter.removeAllItem();
            }
            mAdapter.addItem(trsmis);

            if (mAdapter.getItemCount() == 0) {
                mBinding.emptyText.setVisibility(View.VISIBLE);
                mBinding.trsmisTagLayout.setVisibility(View.GONE);
                mBinding.trsmisTagBottomLine.setVisibility(View.GONE);
            } else {
                mBinding.emptyText.setVisibility(View.GONE);
                mBinding.trsmisTagLayout.setVisibility(View.VISIBLE);
                mBinding.trsmisTagBottomLine.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.getJobCode().observe(this, code -> {

        });
        //통신 에러메시지 변화 감지
        mViewModel.getError().observe(this, errorMsg -> {
            switch (errorMsg) {
                case "SERVER":
                    ErrorDialogFragment dialogFragment = ErrorDialogFragment.getInstance("알림", getString(R.string.str_error), 0);
                    dialogFragment.show(getSupportFragmentManager(), null);
                    dialogFragment.setListener(type -> {
                        finish();
                    });
                    break;
                case "LOGIN":
                    ErrorDialogFragment dialogFragment2 = ErrorDialogFragment.getInstance("알림", getString(R.string.str_login), 0);
                    dialogFragment2.show(getSupportFragmentManager(), null);
                    dialogFragment2.setListener(type -> {
//                        Intent intent = new Intent(TrsmisActivity.this, IntroActivity.class);
//                        startActivity(intent);
                        finishAffinity();
                    });
                    break;
            }
        });

    }

    private void onRecyclerViewScrolledListener() {
        mViewModel.getTrsmisReqModel().setCurrentPage("1");
        mBinding.trsmisRecycler.clearOnScrollListeners();
        mBinding.trsmisRecycler.addOnScrollListener(new RecyclerViewScrollListener() {
            @Override
            public void onHide() {
            }

            @Override
            public void onShow() {
            }

            @Override
            public void onLoadMore(int currentPage) {
                if (currentPage != 1) {
                    mViewModel.getTrsmisReqModel().setCurrentPage(String.valueOf(currentPage));
                    mViewModel.onListCall(mCurrentDate, mJobCdNm);
                }
            }
        });
    }

    @Override
    public void onResultClicked(String jobCdNm) {
        mJobCdNm = jobCdNm;
        onRecyclerViewScrolledListener();
        mJobCd = jobNmToCd(jobCdNm);
        mBinding.setTeam(mJobCdNm);
        mViewModel.onListCall(mCurrentDate, mJobCd);
    }

    @Override
    public void onTrsmisItemClicked(Trsmis model) {
        Intent intent = new Intent(TrsmisActivity.this, TrsmisDetailActivity.class);
        intent.putExtra("model", model);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trsmis_back_image:
                onBackPressed();
                break;
            case R.id.trsmis_date_layout:
                onDatePickerClicked("");
                break;
            case R.id.trsmis_left_image:
                onDatePickerClicked("LEFT");
                break;
            case R.id.trsmis_right_image:
                onDatePickerClicked("RIGHT");
                break;
            case R.id.trsmis_teamSelect_text:
                onTeamPickerClicked();
                break;
            case R.id.trsmis_write_image:
                Intent intent = new Intent(this, TrsmisWriteActivity.class);
                this.mJobCd = jobCdToNm(mJobCdNm);
                intent.putExtra("jobCd", mJobCd);
                startActivity(intent);
                break;

        }
    }

    private void onTeamPickerClicked() {
        TeamSelectDialogFragment mFragment = TeamSelectDialogFragment.getInstance(mJobCd, mJobCdNm);
        mFragment.setListener(this);
        mFragment.show(getSupportFragmentManager(), "");
    }

    private void onDatePickerClicked(String direction) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStringToDate(mCurrentDate));

        if(direction.isEmpty()){ // 날짜가 클릭된 경우
            if (mDatePickerDialog != null && mDatePickerDialog.isShowing()) {
                mDatePickerDialog.dismiss();
            }
            mDatePickerDialog = new DatePickerDialog(TrsmisActivity.this, (datePicker, i, i1, i2) -> {
                String dateResult = i + "-" + (i1+1) + "-" + i2;
                mCurrentDate = changeDateFormat(dateResult);
                String displayDate = getDayAndDate(mCurrentDate);
                mBinding.setDate(displayDate);
                onRecyclerViewScrolledListener();
                mViewModel.onListCall(mCurrentDate, mJobCd);
            }, calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH)), calendar.get(Calendar.DAY_OF_MONTH));

            mDatePickerDialog.setCanceledOnTouchOutside(false);
            mDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            mDatePickerDialog.show();
        } else { // 화살표가 클릭된 경우
            switch (direction){
                case "LEFT":
                    mCurrentDate = dayCalculator(mCurrentDate, -1);
                    break;
                case "RIGHT":
                    Logger.d(todayDateString());
                    Logger.d(mCurrentDate);
                    if (todayDateString().equals(mCurrentDate)) {
                        onToast("오늘 이후의 날짜는\n선택하실 수 없습니다.");
                        return;
                    } else {
                        mCurrentDate = dayCalculator(mCurrentDate, 1);
                    }
                    break;
            }
            String displayDate = getDayAndDate(mCurrentDate);
            mBinding.setDate(displayDate);
            onRecyclerViewScrolledListener();
            mViewModel.onListCall(mCurrentDate, mJobCd);
        }

    }
}
