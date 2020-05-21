package com.example.trsmis2.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class TrsmisResModel { //요청사항 서버요청결과 모델
    /** 상태 */
   private String status;
   /**
    * 카운트
    * 요청조건에 해당하는 총 아이템 갯수
    */
   private int trsmisCnt;
    /** 서버에서 받는 리스트 목록 */
   private ArrayList<Trsmis> trsmisList;

   private ArrayList<TrsmisFormatModel> trsmisFormatModel;

}
