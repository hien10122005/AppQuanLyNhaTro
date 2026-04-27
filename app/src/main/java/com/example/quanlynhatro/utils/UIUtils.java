package com.example.quanlynhatro.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.quanlynhatro.R;

public class UIUtils {

    public interface DialogCallback {
        void onConfirm();
    }

    /**
     * Hiển thị hộp thoại xác nhận phong cách Glassmorphism
     */
    public static void showConfirmDialog(Context context, String title, String message, DialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView tvTitle = view.findViewById(R.id.tvDialogTitle);
        TextView tvMessage = view.findViewById(R.id.tvDialogMessage);
        View btnCancel = view.findViewById(R.id.btnCancel);
        View btnConfirm = view.findViewById(R.id.btnConfirm);

        if (title != null) tvTitle.setText(title);
        if (message != null) tvMessage.setText(message);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            if (callback != null) callback.onConfirm();
        });

        dialog.show();
    }
}
