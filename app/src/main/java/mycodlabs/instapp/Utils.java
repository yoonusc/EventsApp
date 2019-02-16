package mycodlabs.instapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;

/**
 * Created by froger_mcs on 05.11.14.
 */
public class Utils {
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static String getStringImage(Bitmap bitmap){
        Log.i("MyHitesh",""+bitmap);
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);

        return temp;
    }
    public static String fromHtml(String text) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            text = Html.fromHtml(new String(text), Html.FROM_HTML_MODE_LEGACY).toString();
        else
            text = Html.fromHtml(new String(text)).toString();
        text = text.replaceAll("<!--.*-->", ""); // Remove HTML comments, if any
        return text;
    }

}
