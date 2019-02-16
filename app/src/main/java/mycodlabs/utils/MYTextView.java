package mycodlabs.utils;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import mycodlabs.events.R;


public class MYTextView extends android.support.v7.widget.AppCompatTextView {


    public MYTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public MYTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public MYTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {

            try {

                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + "montserrat_regular.ttf");
                    setTypeface(myTypeface);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}