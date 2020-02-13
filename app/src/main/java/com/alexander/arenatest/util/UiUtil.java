package com.alexander.arenatest.util;

import android.graphics.Color;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;

public class UiUtil {
    public static Snackbar makeErrorSnack(String text, View view) {
        Snackbar snack = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        View snackView = snack.getView();
        snackView.setBackgroundColor(Color.RED);

        return snack;
    }
}
