package com.bacancy.eprodigy.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by vishal on 20/2/18.
 */

public class HeavyFontTextview extends AppCompatTextView {

    public HeavyFontTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public HeavyFontTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeavyFontTextview(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Avenir LT 85 Heavy.ttf");
        setTypeface(tf ,1);

    }
}
