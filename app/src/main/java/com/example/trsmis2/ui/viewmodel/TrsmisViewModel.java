package com.example.trsmis2.ui.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.trsmis2.model.Trsmis;
import com.example.trsmis2.model.TrsmisReqModel;
import com.example.trsmis2.model.TrsmisResModel;
import com.example.trsmis2.network.RetrofitClient;
import com.example.trsmis2.network.RetrofitService;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.example.trsmis2.util.AppUtil.monthCalculator;
import static com.example.trsmis2.util.AppUtil.getDateToString;
import static com.example.trsmis2.util.AppUtil.todayDateString;

public class TrsmisViewModel extends ViewModel{
    private TrsmisReqModel mTrsmisReqModel = new TrsmisReqModel("firstInputDt", "", "30", "1");
    private MutableLiveData<ArrayList<Trsmis>> mTrsmisList = new MutableLiveData<>();
    private MutableLiveData<Integer> mTrsmisCnt = new MutableLiveData<>();
    private MutableLiveData<Integer> mLoading = new MutableLiveData<>();
    private MutableLiveData<String> mJobCode = new MutableLiveData<>();
    private MutableLiveData<String> mAction = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();
    private RetrofitService mService = RetrofitClient.create();
    private String jobDstnctCd;

    public void onListCall(String date, String jobCd) {
        Logger.d(jobCd);
        mTrsmisReqModel.setFindStrtDt(monthCalculator(date, -2));
        mTrsmisReqModel.setFindEndDt(date);
        mTrsmisReqModel.setJobDstnctCd(jobCd);
        if(isPageReload()){
            mLoading.setValue(View.VISIBLE);
        }

        mService.trsmisCall(mTrsmisReqModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<TrsmisResModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(Response<TrsmisResModel> trsmisResModelResponse) {
                        //토큰 체크
                        if (trsmisResModelResponse.headers().get("new_token") != null) {
                            Hawk.put("auth-token", trsmisResModelResponse.headers().get("new_token"));
                        }

                        if (trsmisResModelResponse.isSuccessful() && trsmisResModelResponse.body() != null) {
                            Logger.d(trsmisResModelResponse.body());
                            ArrayList<Trsmis> resultTrsmisList = trsmisResModelResponse.body().getTrsmisList();
                            int trsmisCnt = trsmisResModelResponse.body().getTrsmisCnt();
                            mTrsmisCnt.setValue(trsmisCnt);
                            mTrsmisList.setValue(resultTrsmisList);
                        } else {
                            try {
                                JSONObject jsonObject = null;

                                if (trsmisResModelResponse.errorBody() != null) {
                                    jsonObject = new JSONObject(trsmisResModelResponse.errorBody().string());

                                    Logger.d(trsmisResModelResponse.errorBody());
                                }

                                if (jsonObject != null) {
                                    if (jsonObject.getString("message").equals("Access Denied")) {
                                        Logger.d(jsonObject.getString("message"));
                                        mError.setValue("LOGIN");
                                    }
                                }
                            } catch (JSONException | IOException e) {
                                Logger.d(e);
                                mError.setValue("SERVER");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e);
                        mError.setValue("SERVER");
                    }
                });
        if (isPageReload())
            mLoading.setValue(View.GONE);
    }

    public boolean isPageReload() {
        return mTrsmisReqModel.getCurrentPage().equals("1");
    }

    public void backBtnClicked() {}

    public void dateClicked() {}

    public void datePrevClicked() {}

    public void dateNextClicked() {}

    public void teamSelectClicked() {}

    public void writeBtnClicked() {}



    public TrsmisReqModel getTrsmisReqModel() {
        return mTrsmisReqModel;
    }

    public MutableLiveData<String> getError() {
        return mError;
    }

    public MutableLiveData<String> getJobCode() {
        return mJobCode;
    }

    public MutableLiveData<Integer> getTrsmisCnt() {
        return mTrsmisCnt;
    }

    public MutableLiveData<ArrayList<Trsmis>> getTrsmis() {
        return mTrsmisList;
    }

    public MutableLiveData<String> getAction() {
        return mAction;
    }

    public MutableLiveData<Integer> getLoading() {
        return mLoading;
    }

}
