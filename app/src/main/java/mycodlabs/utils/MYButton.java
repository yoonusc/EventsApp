package mycodlabs.utils;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.widget.Button;


public class MYButton extends android.support.v7.widget.AppCompatButton {


    public MYButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public MYButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public MYButton(Context context) {
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