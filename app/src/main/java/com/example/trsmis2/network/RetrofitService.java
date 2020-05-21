package com.example.trsmis2.network;

import com.example.trsmis2.model.TrsmisAporResModel;
import com.example.trsmis2.model.TrsmisInsertResModel;
import com.example.trsmis2.model.TrsmisReqModel;
import com.example.trsmis2.model.TrsmisResModel;

import java.util.Map;

import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface RetrofitService {

    /**
     * 요청사항
     */
    @POST("trsmis/list")
    Single<Response<TrsmisResModel>> trsmisCall(@Body TrsmisReqModel reqModel);

    /**
     * 요청사항 지정자 리스트
     */
    @GET("trsmis/apor/list")
    Single<Response<TrsmisAporResModel>> trsmisAporListCall();

    /**
     * 요청사항 입력
     */
    @Multipart
    @POST("trsmis/insert")
    Single<Response<TrsmisInsertResModel>> trsmisInsertCall(@PartMap Map<String, RequestBody> reqModel);
}
