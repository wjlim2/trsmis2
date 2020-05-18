package com.example.trsmis2.network;

import com.example.trsmis2.model.TrsmisReqModel;
import com.example.trsmis2.model.TrsmisResModel;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitService {

    /**
     * 요청사항
     */
    @POST("trsmis/list")
    Single<Response<TrsmisResModel>> trsmisCall(@Body TrsmisReqModel reqModel);
}
