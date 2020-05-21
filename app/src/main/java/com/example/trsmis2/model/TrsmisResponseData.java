package com.example.trsmis2.model;

import lombok.Data;

/**
 * Created by sy-02 on 2018-04-04.
 */

@Data
public class TrsmisResponseData<T> {

    private String status;
    private int trsmisCnt;
    private T data;
}
