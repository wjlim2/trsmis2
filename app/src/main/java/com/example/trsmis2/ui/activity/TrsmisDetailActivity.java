package com.example.trsmis2.ui.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.trsmis2.BuildConfig;
import com.example.trsmis2.R;
import com.example.trsmis2.base.BaseActivity;
import com.example.trsmis2.databinding.ActivityTrsmisDetailBinding;
import com.example.trsmis2.model.PositTeam;
import com.example.trsmis2.model.TrsmisAtchmnfl;
import com.example.trsmis2.model.TrsmisFormatModel;
import com.example.trsmis2.ui.adapter.TrsmisDetailAdapter;
import com.example.trsmis2.ui.listener.TrsmisAtchmnflItemClickListener;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;

public class TrsmisDetailActivity extends BaseActivity implements TrsmisAtchmnflItemClickListener {

    private ActivityTrsmisDetailBinding mBinding;
    private ArrayList<TrsmisAtchmnfl> mFileList;
    private DownloadManager mDownloadManager;

    private ArrayList<Long> mDownloadIdList;
    private int mOpenFolderCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_trsmis_detail);
        start();
        onDownloadManager();
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(onDownloadComplete);
        super.onDestroy();
    }
    private void start(){
        mBinding.trsmisDetailBackImage.setOnClickListener(v -> finish());
        Intent intent = getIntent();
        if (intent.getParcelableExtra("model") != null) {
            mBinding.setLifecycleOwner(this);
            TrsmisFormatModel model = intent.getParcelableExtra("model");
            if (model != null) {
                mFileList = model.getFileList();
            }
            mBinding.setModel(model);
            if (model != null) {
                switch (model.getPrblmDlngStatNmUpper()) {
                    case "확인":
                        mBinding.trsmisDetailStatusText.setTextColor(ContextCompat.getColor(this, R.color.color87b868));
                        mBinding.trsmisDetailStatusText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color87b868)));
                        break;
                    case "미확인":
                        mBinding.trsmisDetailStatusText.setTextColor(ContextCompat.getColor(this, R.color.colore23a3a));
                        mBinding.trsmisDetailStatusText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colore23a3a)));
                        break;
                    case "완료":
                        mBinding.trsmisDetailStatusText.setTextColor(ContextCompat.getColor(this, R.color.color689ec1));
                        mBinding.trsmisDetailStatusText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color689ec1)));
                        break;
                    case "취소":
                        setCancel();
                        mBinding.trsmisDetailStatusText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.coloraaaaaa)));
                        mBinding.trsmisDetailStatusText.setTextColor(ContextCompat.getColor(this, R.color.coloraaaaaa));
                        break;
                    case "미해결":
                        mBinding.trsmisDetailStatusText.setTextColor(ContextCompat.getColor(this, R.color.coloraa58be));
                        mBinding.trsmisDetailStatusText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.coloraa58be)));
                        break;
                    default:
                        mBinding.trsmisDetailStatusText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        mBinding.trsmisDetailStatusText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
                        break;
                }
            }
        }
        setAdapter();
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
    }

    private void setAdapter() {
        TrsmisDetailAdapter mAdapter = new TrsmisDetailAdapter(mFileList);

        mAdapter.setItemClickListener(this);
        mBinding.trsmisAttachFileList.setAdapter(mAdapter);
    }

    private void onDownloadManager() {
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        mDownloadIdList = new ArrayList<>();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        registerReceiver(onDownloadComplete, intentFilter);
    }

    private void setTeamColor(String custDstnctCd) {
        ArrayList<PositTeam> list = Hawk.get("TEAM_LIST", new ArrayList<>());
        int custDstnctcdColor = ContextCompat.getColor(this, R.color.colorPrimary);
        if (list.size() == 0) {
            mBinding.trsmisDetailCustDstnctCardView.setCardBackgroundColor(custDstnctcdColor);
            return;
        }
        for (PositTeam positTeam : list) {
            if (positTeam.getAppDeepColor() != null && positTeam.getCustDstnctCd().equals(custDstnctCd)) {
                custDstnctcdColor = Color.parseColor(positTeam.getAppDeepColor());
                break;
            }
        }
        mBinding.trsmisDetailCustDstnctCardView.setCardBackgroundColor(custDstnctcdColor);
    }

    private void setCancel() {
        mBinding.trsmisDetailCustDstnctCardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.coloraaaaaa));
    }

    @Override
    public void onTrsmisAtchmnflItemClicked(TrsmisAtchmnfl model) {

        downloadFile(model);
    }

    private void downloadFile(TrsmisAtchmnfl model) {
        File file = new File(getExternalFilesDir(null), model.getFileNm());
        String filePath = model.getFilePathNm();
        Uri path = Uri.parse(BuildConfig.SERVER_URL + "download" + filePath);
        Logger.d(path);
        DownloadManager.Request request  = new DownloadManager
                .Request(path)
                .setTitle("파일 다운로드 중...")
                .setDescription(model.getFileNm()).setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, model.getFileNm())
                .setDestinationUri(Uri.fromFile(file))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);

        mDownloadIdList.add(mDownloadManager.enqueue(request));
//
//        Toast.makeText(this, model.getFileNm(), Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                if (mDownloadIdList.contains(id)) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(id);
                    Cursor cursor = mDownloadManager.query(query);
                    if (!cursor.moveToFirst()) {
                        return;
                    }
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                    Logger.d("failed: " + cursor.getInt(columnReason));
                    int status = cursor.getInt(columnIndex);
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        if (mOpenFolderCount == 0) {
                            startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                        }
                        mOpenFolderCount++;
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        Toast.makeText(context, "다운로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };
}
