package mycodlabs.utils;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;


public class MYEditTextView extends AppCompatEditText {


    public MYEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public MYEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public MYEditTextView(Context context) {
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