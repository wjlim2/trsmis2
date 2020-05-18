package com.example.trsmis2.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStoreOwner;

public abstract class BaseActivity extends AppCompatActivity implements ViewModelStoreOwner {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 기본 토스트 메소드
    private static Toast TOAST;
    @SuppressLint("ShowToast")
    public void onToast(String text) {
        if (TOAST == null) {
            TOAST = Toast.makeText(this, text, Toast.LENGTH_LONG);
        } else {
            TOAST.setText(text);
        }
        TOAST.show();
    }
}
