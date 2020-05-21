package com.example.trsmis2.ui.viewmodel;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.trsmis2.model.Trsmis;
import com.example.trsmis2.model.TrsmisFormatModel;
import com.example.trsmis2.model.TrsmisReqModel;
import com.example.trsmis2.model.TrsmisResModel;
import com.example.trsmis2.network.RetrofitClient;
import com.example.trsmis2.network.RetrofitService;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.example.trsmis2.util.AppUtil.*;

public class TrsmisViewModel extends ViewModel {
    private TrsmisReqModel mTrsmisReqModel = new TrsmisReqModel("firstInputDt", "", "30", "1");
    private MutableLiveData<ArrayList<TrsmisFormatModel>> mTrsmisFormat = new MutableLiveData<>();
    private MutableLiveData<Integer> mTrsmisCnt = new MutableLiveData<>();
    private MutableLiveData<Integer> mLoading = new MutableLiveData<>();
    private MutableLiveData<String> mJobCode = new MutableLiveData<>();
    private MutableLiveData<String> mAction = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();
    private RetrofitService mService = RetrofitClient.create();
    private String jobDstnctCd;

    public void onListCall(String date, String jobCd) {
        mTrsmisReqModel.setFindStrtDt(monthCalculator(date, -2));
        mTrsmisReqModel.setFindEndDt(date);
        mTrsmisReqModel.setJobDstnctCd(jobCd);
        if (isPageReload()) {
            mLoading.setValue(View.VISIBLE);
        }
        mService.trsmisCall(mTrsmisReqModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    if (!response.isSuccessful() || response.body() == null) {
                        try {
                            JSONObject jsonObject = null;
                            if (response.errorBody() != null) {
                                jsonObject = new JSONObject(response.errorBody().string());
                            }
                            if (jsonObject != null) {
                                if (jsonObject.getString("message").equals("Access Denied")) {
                                    mError.setValue("LOGIN");
                                }
                            }
                        } catch (JSONException | IOException e) {
                            mError.setValue("SERVER");
                        }
                    }
                    return response;
                })
                .observeOn(Schedulers.io())
                .map( response -> {
                    ArrayList<TrsmisFormatModel> trsmisFormatModel = new ArrayList<>();
                    if (response.isSuccessful() && response.body() != null) {
                        ArrayList<Trsmis> trsmis = response.body().getTrsmisList();
                        for (int index = 0; index < trsmis.size(); index++) {
                            //아이템 포지션
                            String position = String.valueOf(response.body().getTrsmisCnt() - ((index + (Integer.parseInt(mTrsmisReqModel.getCurrentPage())-1)*30)));

                            // 요청사항 분류
                            if (trsmis.get(index).getTrsmisLclasNm() == null) trsmis.get(index).setTrsmisLclasNm("미지정");//대분류
                            if (trsmis.get(index).getTrsmisSclasNm() == null) trsmis.get(index).setTrsmisSclasNm("미지정");//소분류
                            String trsmisPrblmTitle = "[" + trsmis.get(index).getTrsmisLclasNm() + ">" + trsmis.get(index).getTrsmisSclasNm() + "]";

                            //요청 처리상태
                            String trsmisPendTitle;
                            if (trsmis.get(index).getDlngResltText() == null) trsmis.get(index).setDlngResltText(""); //결과
                            if (trsmis.get(index).getPrblmDlngStatNm() == null || trsmis.get(index).getPrblmDlngStatNm().equals("미확인")) { //진행 및 결과
                                trsmisPendTitle = "";
                            } else {
                                trsmisPendTitle = "[" + trsmis.get(index).getPrblmDlngStatNm() + "]";
                            }

                            //하단 요청 처리결과 텍스트
                            String trsmisPend;
                            if (trsmis.get(index).getDlngResltText().isEmpty()) {
                                if (trsmis.get(index).getPrblmPendText() != null) {
                                    trsmisPend = trsmis.get(index).getPrblmPendText();
                                } else {
                                    trsmisPend = "";
                                }
                            } else {
                                trsmisPend =trsmis.get(index).getDlngResltText();
                            }

                            trsmisFormatModel.add(new TrsmisFormatModel(
                                    position, //아이템 포지션
                                    trsmis.get(index).getCustDstnctSoNm(), // 요청업체 축약명
                                    trsmis.get(index).getPositTeamNm(), //요청업체명
                                    trsmis.get(index).getPrblmDlngStatNm(), //상단 요청처리상태
                                    trsmisPendTitle, //하단 요청 처리상태
                                    trsmisPrblmTitle, //요청사항 분류
                                    trsmis.get(index).getPrblmText(), //요청사항 텍스트
                                    trsmisPend,
                                    trsmis.get(index).getCustDstnctCd())); //요청처리결과 텍스트

                            //요청일
                            trsmisFormatModel.get(index).setFirstInputDt(changeDateSlashFormat(trsmis.get(index).getFirstInputDt()));

                            //요청자
                            trsmisFormatModel.get(index).setWritrNm(trsmis.get(index).getWritrNm());

                            //처리자명
                            trsmisFormatModel.get(index).setDlrNm(trsmis.get(index).getDlrNm());

                            //첨부파일
                            trsmisFormatModel.get(index).setFileList(trsmis.get(index).getFileList());
                            Logger.d(trsmis.get(index).getFileList());
                        }
                    }


                    return trsmisFormatModel;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<TrsmisFormatModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(ArrayList<TrsmisFormatModel> trsmisFormatModel) {
                        mTrsmisFormat.setValue(trsmisFormatModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mError.setValue("SERVER");
                    }
                });
        if (isPageReload())
            mLoading.setValue(View.GONE);
    }

    public boolean isPageReload() {
        return mTrsmisReqModel.getCurrentPage().equals("1");
    }

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

    public MutableLiveData<ArrayList<TrsmisFormatModel>> getTrsmisFormat() {
        return mTrsmisFormat;
    }

    public MutableLiveData<String> getAction() {
        return mAction;
    }

    public MutableLiveData<Integer> getLoading() {
        return mLoading;
    }


}
