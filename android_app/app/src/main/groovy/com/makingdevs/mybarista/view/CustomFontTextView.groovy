package com.makingdevs.mybarista.view

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView
import com.makingdevs.mybarista.R
import groovy.transform.CompileStatic;

@CompileStatic
public class CustomFontTextView extends TextView{

    private String mFont;

    public CustomFontTextView(Context context) {
        super(context);
        init(null);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            obtainAttributes(attrs);

            if (mFont  != null)
                setFont(mFont);
        }
    }

    private void obtainAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
        mFont = a.getString(R.styleable.CustomFontTextView_fontName);

        a.recycle();
    }

    private Typeface getTypeFace(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    public void setFont(String font) {
        mFont = font;

        Typeface typeface = getTypeFace(getContext(), font);
        setTypeface(typeface);
    }
}