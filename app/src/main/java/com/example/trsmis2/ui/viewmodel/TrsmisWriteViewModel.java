package com.example.trsmis2.ui.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.trsmis2.model.TrsmisApor;
import com.example.trsmis2.model.TrsmisAporResModel;
import com.example.trsmis2.model.TrsmisInsertResModel;
import com.example.trsmis2.network.RetrofitClient;
import com.example.trsmis2.network.RetrofitService;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;

public class TrsmisWriteViewModel extends ViewModel {

    private MutableLiveData<ArrayList<TrsmisApor>>  mTrsBizStatusList = new MutableLiveData<>();
    private MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private MutableLiveData<Integer> mLoading = new MutableLiveData<>();
    private MutableLiveData<String> mAction = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();
    private RetrofitService mService = RetrofitClient.create();
    private String jobCd_Selected;

    public void init() {
        mLoading.setValue(View.GONE);
        jobCd_Selected = (Hawk.get("currentJobCd"));
        onAporListCall();
    }

    private void onAporListCall() {
        mLoading.setValue(View.VISIBLE);
        mService.trsmisAporListCall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    if (response.headers().get("new_token") != null) {
                        Hawk.put("auth-token", response.headers().get("new_token"));
                    }
                    if(!response.isSuccessful()){
                        try {
                                JSONObject jsonObject = null;
                                if (response.errorBody() != null) {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                }
                                if (jsonObject != null) {
                                    jsonObject.getString("message");
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<TrsmisAporResModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(Response<TrsmisAporResModel> trsmisAporResModelResponse) {

                        if (trsmisAporResModelResponse.isSuccessful()) {
                            if (trsmisAporResModelResponse.body() != null) {
                                ArrayList<TrsmisApor> temp = trsmisAporResModelResponse.body().getTrsmisAporList();
                                ArrayList<TrsmisApor> list_Apor = new ArrayList<>();
                                for (TrsmisApor apor : temp) {

                                    //경영지원팀
                                    if (jobCd_Selected.equals("4") || jobCd_Selected.equals("6") || jobCd_Selected.equals("12")) {
                                        if (apor.getJobCd().equals("4") || apor.getJobCd().equals("6") || apor.getJobCd().equals("12")) {
                                            list_Apor.add(apor);
                                        }

                                    //공고팀
                                    } else if (jobCd_Selected.equals("7") && apor.getJobCd().equals("7")) {
                                        list_Apor.add(apor);

                                    //개발팀
                                    } else if (jobCd_Selected.equals("8") && apor.getJobCd().equals("8")) {
                                        list_Apor.add(apor);
                                    }
                                }
                                mTrsBizStatusList.setValue(list_Apor);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mError.setValue("SERVER");
                    }
                });
        mLoading.setValue(View.GONE);
    }

    private static RequestBody toRequestBodyString(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    public void onRequest(Long aporId, String prblmText, String jobDstnctCd) {
        String aporId_String = aporId.toString();
        Map<String, RequestBody> temp = new HashMap<>();
        temp.put("aporId", toRequestBodyString(aporId_String));
        temp.put("prblmText", toRequestBodyString(prblmText));
        temp.put("jobDstnctCd", toRequestBodyString(jobDstnctCd));

        mService.trsmisInsertCall(temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<TrsmisInsertResModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(Response<TrsmisInsertResModel> trsmisInsertResModelResponse) {

                        if (trsmisInsertResModelResponse.isSuccessful()) {
                            mToastMessage.setValue("추가를 완료하였습니다");
                        } else {
                            mToastMessage.setValue("추가를 실패하였습니다.");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mToastMessage.setValue("추가를 실패하였습니다.");
                    }
                });
    }

    public MutableLiveData<String> getToastMessage() {
        return mToastMessage;
    }

    public MutableLiveData<String> getAction() {
        return mAction;
    }

    public RetrofitService getService() {
        return mService;
    }

    public MutableLiveData<Integer> getLoading() {
        return mLoading;
    }

    public MutableLiveData<String> getError() {
        return mError;
    }

    public MutableLiveData<ArrayList<TrsmisApor>> getTrsBizStatusList() {
        return mTrsBizStatusList;
    }
}
