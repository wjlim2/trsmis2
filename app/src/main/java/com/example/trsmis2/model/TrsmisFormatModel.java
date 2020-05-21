package com.example.trsmis2.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TrsmisFormatModel implements Parcelable {


    private TrsmisFormatModel() {
        fileList = new ArrayList<TrsmisAtchmnfl>();
    }

    /**
     * 요청사항 아이템 Position
     */
    private String trsmisPos;

    /**
     * 고객 축약명
     * ex.진, 인, 스쿨, 다, 더, 오
     */
    private String custDstnctSoNm;

    /**
     * 요청사항 요청고객명
     * ex. 더비스, 수금팀, 다온(대구), 수원(오)
     */

    private String positTmNm;

    /**
     * 요청사항 처리상태명 상단
     * ex.확인, 미확인, 완료, 취소
     */
    private String prblmDlngStatNmUpper;

    /**
     * 요청사항 처리상태명 하단
     * ex.확인, 완료, 취소
     */
    private String prblmDlngStatNmLower;

    /**
     * 요청사항 분류
     */
    private String trsmisPrblmTitleNm;

    /**
     * 요청사항 텍스트
     */
    private String prblmText;

    /**
     * 처리완료 메시지 텍스트
     */
    private String dlngResltText;

    /**
     * 요청자 구분코드
     */
    private String custDstnctCd;

    /**
     * 요청사항 입력일
     */
    private String firstInputDt;

    /**
     * 요청자명
     */
    private String writrNm;

    /**
     * 처리자명
     */
    private String dlrNm;

    /**
     * 첨부파일 리스트 // 리스트인데 임시로
     */
    private ArrayList<TrsmisAtchmnfl> fileList;

    public TrsmisFormatModel(String trsmisPos, String custDstnctSoNm, String positTmNm, String prblmDlngStatNmUpper, String prblmDlngStatNmLower, String trsmisPrblmTitleNm, String prblmText, String dlngResltText, String custDstnctCd) {
        this.trsmisPos = trsmisPos;
        this.custDstnctSoNm = custDstnctSoNm;
        this.positTmNm = positTmNm;
        this.prblmDlngStatNmUpper = prblmDlngStatNmUpper;
        this.prblmDlngStatNmLower = prblmDlngStatNmLower;
        this.trsmisPrblmTitleNm = trsmisPrblmTitleNm;
        this.prblmText = prblmText;
        this.dlngResltText = dlngResltText;
        this.custDstnctCd = custDstnctCd;
    }

    public static final Parcelable.Creator<TrsmisFormatModel> CREATOR = new Parcelable.Creator<TrsmisFormatModel>() {
        @Override
        public TrsmisFormatModel createFromParcel(Parcel in) {
            return new TrsmisFormatModel(in);
        }

        @Override
        public TrsmisFormatModel[] newArray(int size) {
            return new TrsmisFormatModel[size];
        }
    };

    private TrsmisFormatModel(Parcel in) { // 객체를 받았을 때 직렬화를 푸는 메소드
        trsmisPos = in.readString();
        custDstnctSoNm = in.readString();
        positTmNm = in.readString();
        prblmDlngStatNmUpper = in.readString();
        trsmisPrblmTitleNm = in.readString();
        prblmText = in.readString();
        dlngResltText = in.readString();
        custDstnctCd = in.readString();
        firstInputDt = in.readString();
        writrNm = in.readString();
        dlrNm = in.readString();
        fileList = new ArrayList<>();
        in.readTypedList(fileList, TrsmisAtchmnfl.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) { // 데이터를 직렬화하여 dest에 순차적으로 넣는다.
        dest.writeString(trsmisPos);
        dest.writeString(custDstnctSoNm);
        dest.writeString(positTmNm);
        dest.writeString(prblmDlngStatNmUpper);
        dest.writeString(trsmisPrblmTitleNm);
        dest.writeString(prblmText);
        dest.writeString(dlngResltText);
        dest.writeString(custDstnctCd);
        dest.writeString(firstInputDt);
        dest.writeString(writrNm);
        dest.writeString(dlrNm);
        dest.writeTypedList(fileList);
    }
}
