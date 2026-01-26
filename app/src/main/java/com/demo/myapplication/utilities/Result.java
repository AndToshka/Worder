package com.demo.myapplication.utilities;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.demo.myapplication.check.Proverka;
import com.demo.myapplication.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Result extends AppCompatDialogFragment {

    private final int right, notRight;
    ThemeClass themeClassResult;
    Context context;
    Activity activity;
    SharedPreferences topScores;

    public Result(int data1, int data2, Context context, Activity activity) {
        this.right = data1;
        this.notRight = data2;
        this.context = context;
        this.activity = activity;
        this.topScores = context.getSharedPreferences("PREFS_DB", MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.stil_result, null);
        themeClassResult = new ThemeClass(context, activity, null, null,
                null, null, null, null, null, null, null, null);
        themeClassResult.callTheme();

        builder.setView(view)
                .setTitle(R.string.tx40)
                .setPositiveButton(R.string.tx41, (dialogInterface, i) -> {
                    Intent intent = new Intent(getContext(), Proverka.class);
                    startActivity(intent);
                });
        builder.setCancelable(false);

        TextView text1, text2, text3;
        text1 = view.findViewById(R.id.text1);
        text2 = view.findViewById(R.id.text2);
        text3 = view.findViewById(R.id.text3);

        String g = getString(R.string.tx42) + right;
        String g2 = getString(R.string.tx43) + notRight;
        int sum = right + notRight;
        int proc = Math.round(((float) right / sum) * 100);
        String g3 = proc + "%";
        boolean top = ResultTable();
        text1.setText(g);
        text2.setText(g2);
        text3.setText(g3);

        text3.setTextColor(themeClassResult.getForText());
        text1.setTextColor(themeClassResult.getForText());
        text2.setTextColor(themeClassResult.getForText());
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null && dialog instanceof AlertDialog) {
            AlertDialog alertDialog = (AlertDialog) dialog;
            Window window = alertDialog.getWindow();
            if (window != null) {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setCornerRadius(32f);
                drawable.setStroke(4, themeClassResult.getButtonColor());
                drawable.setColor(themeClassResult.getForFon());
                window.setBackgroundDrawable(drawable);
            }
            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (positiveButton != null) {
                positiveButton.setTextColor(themeClassResult.getButtonColor());
            }
        }
        assert dialog != null;
        dialog.setCanceledOnTouchOutside(false);
    }

    private boolean ResultTable() {
        if(right==0) return false;
        boolean isFull = topScores.getBoolean("rows", false);
        int minResult = topScores.getInt("minResult", 0);
        if(isFull && right <= minResult) return false;

        if(DataBase.SameCount(context, right)>=2) return false;

        String result = right + "/" + (right + notRight);
        DataBase.CreateRow(context, DataBase.TABLE5, DataBase.CreateMap(DataBase.COL_RESULT, result, DataBase.COL_DATE, createDate()));

        SharedPreferences.Editor editor = topScores.edit();
        minResult = DataBase.MinResult(context);
        editor.putInt("minResult", minResult);
        if(!isFull){
            if(DataBase.CountRow(context, DataBase.TABLE5)>15){
                isFull=true;
                editor.putBoolean("rows", true);
            }
        }
        editor.apply();

        if(isFull) {
            DataBase.DeleteMin(context);
            minResult = DataBase.MinResult(context);
            editor.putInt("minResult", minResult);
            editor.apply();
            return true;}

        if(minResult==0){ DataBase.DeleteMin(context); }

      return true;
    }
    private static String createDate(){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        return today.format(formatter);
    }
}
