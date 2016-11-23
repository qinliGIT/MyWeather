package com.ql.weather.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/23 0023.
 */

public class FZLTTextView extends TextView {

    public FZLTTextView(Context context) {
        super(context);
        init(context);
    }

    public FZLTTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FZLTTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {
        Typeface roboto= Typeface.createFromAsset(context.getAssets(),"FANGZHENGLANTING.ttf");
        this.setTypeface(roboto);
    }
}