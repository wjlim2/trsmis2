package com.example.trsmis2.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

@Data
public class TrsmisAtchmnfl implements Parcelable{

    /** 파일명 */
    private String fileNm;

    /** 파일경로 */
    private String filePathNm;

    public static final Parcelable.Creator<TrsmisAtchmnfl> CREATOR = new Parcelable.Creator<TrsmisAtchmnfl>() {
        @Override
        public TrsmisAtchmnfl createFromParcel(Parcel in) {
            return new TrsmisAtchmnfl(in);
        }

        @Override
        public TrsmisAtchmnfl[] newArray(int size) {
            return new TrsmisAtchmnfl[size];
        }
    };

    private TrsmisAtchmnfl(Parcel in) {
        fileNm = in.readString();
        filePathNm = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileNm);
        dest.writeString(filePathNm);
    }
}
