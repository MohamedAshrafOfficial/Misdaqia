package com.example.misdaqia.Helper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class MainFontTextview extends android.support.v7.widget.AppCompatTextView {
    public MainFontTextview(Context context) {
        super(context);
    }

    public MainFontTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "tajawalmedium.ttf"));


    }
}