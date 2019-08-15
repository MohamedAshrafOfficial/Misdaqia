package com.example.misdaqia.Helper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class HeaderFontTextview extends android.support.v7.widget.AppCompatTextView {
    public HeaderFontTextview(Context context) {
        super(context);
    }

    public HeaderFontTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "lemonadasemibold.ttf"));


    }
}