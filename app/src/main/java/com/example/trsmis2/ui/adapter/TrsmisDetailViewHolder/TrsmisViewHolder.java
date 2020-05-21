package com.example.trsmis2.ui.adapter.TrsmisDetailViewHolder;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trsmis2.R;
import com.example.trsmis2.databinding.ItemTrsmisBinding;
import com.example.trsmis2.model.PositTeam;
import com.example.trsmis2.model.TrsmisFormatModel;
import com.example.trsmis2.ui.listener.TrsmisItemClickListener;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

public class TrsmisViewHolder extends RecyclerView.ViewHolder {
    private ItemTrsmisBinding mBinding;

    public TrsmisViewHolder(@NonNull ItemTrsmisBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }

    public void onBindView(TrsmisFormatModel model, TrsmisItemClickListener listener) {
        setTeamColor(model.getCustDstnctCd());
        setDefault();
        switch (model.getPrblmDlngStatNmUpper()) {
            case "확인":
                mBinding.trsmisPrblmDlngStatNmText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color87b868));
                mBinding.trsmisPrblmDlngStatNmText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.color87b868)));
                break;
            case "미확인":
                mBinding.trsmisPrblmDlngStatNmText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colore23a3a));
                mBinding.trsmisPrblmDlngStatNmText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.colore23a3a)));
                break;
            case "완료":
                mBinding.trsmisPrblmDlngStatNmText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color689ec1));
                mBinding.trsmisPrblmDlngStatNmText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.color689ec1)));
                break;
            case "취소":
                setCancel();
                mBinding.trsmisPrblmDlngStatNmText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.coloraaaaaa)));
                mBinding.trsmisPrblmDlngStatNmText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.coloraaaaaa));
                break;
            case "미해결":
                mBinding.trsmisPrblmDlngStatNmText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.coloraa58be));
                mBinding.trsmisPrblmDlngStatNmText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.coloraa58be)));
                break;
            default:
                mBinding.trsmisPrblmDlngStatNmText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
                mBinding.trsmisPrblmDlngStatNmText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary)));
                break;
        }

        mBinding.setModel(model);
        mBinding.setListener(listener);
    }

    private void setTeamColor(String custDstnctCd) {
        ArrayList<PositTeam> list = Hawk.get("TEAM_LIST", new ArrayList<>());
        int custDstnctcdColor = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary);
        if (list.size() == 0) {
            mBinding.trsmisCustdstnctCardView.setCardBackgroundColor(custDstnctcdColor);
            return;
        }
        for (PositTeam positTeam : list) {
            if (positTeam.getAppDeepColor() != null && positTeam.getCustDstnctCd().equals(custDstnctCd)) {
                custDstnctcdColor = Color.parseColor(positTeam.getAppDeepColor());
                break;
            }
        }
        mBinding.trsmisCustdstnctCardView.setCardBackgroundColor(custDstnctcdColor);
    }

    private void setCancel(){
        mBinding.trsmisCustdstnctCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.coloraaaaaa));
        mBinding.trsmisPositTeamIdText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.coloraaaaaa));
        mBinding.trsmisProblmPendTitleText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.coloraaaaaa));
        mBinding.trsmisProblmPendText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.coloraaaaaa));
        mBinding.trsmisPrblmTitleText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.coloraaaaaa));
        mBinding.trsmisPrblmText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.coloraaaaaa));

        mBinding.trsmisPrblmText.setPaintFlags(mBinding.trsmisPrblmText.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        mBinding.trsmisPrblmTitleText.setPaintFlags(mBinding.trsmisProblmPendText.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        mBinding.trsmisProblmPendTitleText.setPaintFlags(mBinding.trsmisProblmPendTitleText.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        mBinding.trsmisProblmPendText.setPaintFlags(mBinding.trsmisProblmPendText.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
    } //취소된 요청 처리

    private void setDefault(){
        mBinding.trsmisPositTeamIdText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color333333));
        mBinding.trsmisProblmPendTitleText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color333333));
        mBinding.trsmisPrblmTitleText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color333333));
        mBinding.trsmisProblmPendText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color888888));
        mBinding.trsmisPrblmText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color888888));

        mBinding.trsmisPrblmText.setPaintFlags(mBinding.trsmisPrblmText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        mBinding.trsmisPrblmTitleText.setPaintFlags(mBinding.trsmisPrblmTitleText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        mBinding.trsmisProblmPendTitleText.setPaintFlags(mBinding.trsmisProblmPendTitleText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        mBinding.trsmisProblmPendText.setPaintFlags(mBinding.trsmisProblmPendText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
    }
}
