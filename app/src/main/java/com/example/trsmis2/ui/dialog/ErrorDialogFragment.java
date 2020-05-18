package com.example.trsmis2.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.trsmis2.R;

public class ErrorDialogFragment extends DialogFragment {
    private ErrorClickListener listener;

    public static ErrorDialogFragment getInstance(@NonNull String title, @NonNull String content, int type) {
        ErrorDialogFragment fragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }
    public void setListener(ErrorClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ErrorClickListener){
            listener = (ErrorClickListener) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String content = getArguments().getString("content");
        final int type = getArguments().getInt("type", 0);
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialog_error);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        TextView titleText = dialog.findViewById(R.id.team_title);
        TextView contentText = dialog.findViewById(R.id.team_content);
        TextView okText = dialog.findViewById(R.id.team_ok);
        titleText.setText(title);
        contentText.setText(content);
        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.errorClicked(type);
                }
                dismiss();
            }
        });
        return dialog;
    }

    public interface ErrorClickListener{
        void errorClicked(int type);
    }
}

