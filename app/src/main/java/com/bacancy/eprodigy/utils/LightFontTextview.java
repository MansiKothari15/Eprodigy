package com.bacancy.eprodigy.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by vishal on 20/2/18.
 */

public class LightFontTextview extends AppCompatTextView {

    public LightFontTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LightFontTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LightFontTextview(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Avenir LT 35 Light.ttf");
        setTypeface(tf ,1);

    }
}
