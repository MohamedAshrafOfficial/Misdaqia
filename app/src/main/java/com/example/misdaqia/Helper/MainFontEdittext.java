package com.example.misdaqia.Helper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class MainFontEdittext extends android.support.v7.widget.AppCompatEditText {
    public MainFontEdittext(Context context) {
        super(context);
    }

    public MainFontEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "tajawalmedium.ttf"));


    }
}