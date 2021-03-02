package com.example.tunda.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.example.tunda.R;


public class Loading {


    ProgressDialog loadingDialog;
    Handler handler = new Handler();

    public void startLoading(final Context context) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog == null) {
                    loadingDialog = new ProgressDialog(context, R.style.ProgressTheme);
                    loadingDialog.setCancelable(false);
                    loadingDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    loadingDialog.show();
                }
            }
        });
    }

    public void endLoading() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                    loadingDialog = null;
                }
            }
        });
    }
}
