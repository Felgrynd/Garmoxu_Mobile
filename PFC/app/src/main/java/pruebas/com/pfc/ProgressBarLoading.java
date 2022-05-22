package pruebas.com.pfc;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

public class ProgressBarLoading {

    Activity activity;
    AlertDialog dialog;

    ProgressBarLoading(Activity activity){
        this.activity = activity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_load, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }

}
