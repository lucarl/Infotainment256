package com.semcon.oil.carpoc;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivityButtonListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.testbutton:
            default:
                Log.d("MainActivityButtonListener:", "no such listener: " + view.getId());
        }

    }
}
