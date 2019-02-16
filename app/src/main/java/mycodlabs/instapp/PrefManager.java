package mycodlabs.instapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yoonus on 2/8/2017.
 */
public class PrefManager {


        SharedPreferences pref;
        SharedPreferences.Editor editor;
        Context _context;
        // shared pref mode
        int PRIVATE_MODE = 0;
        // Shared preferences file name
        private static final String PREF_NAME = "appintro";

        private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String BASE_URL = "baseUrl";
    private static final String DOMAIN = "domain";
    private static final String DURATION = "duration";

        public PrefManager(Context context) {
            this._context = context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }

        public void setFirstTimeLaunch(boolean isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
            editor.commit();
        }

        public boolean isFirstTimeLaunch() {
            return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
        }

    public void setBaseUrl(String baseUlr) {
        editor.putString(BASE_URL, baseUlr);
        editor.commit();
    }

    public String getBaseUrl() {
        return pref.getString(BASE_URL, "http://");
    }


    public void setDuration(String duratio) {
        editor.putString(DURATION, duratio);
        editor.commit();
    }

    public String getDuration() {
        return pref.getString(DURATION, "");
    }

    public void setDomain(String domain) {
        editor.putString(DOMAIN, domain);
        editor.commit();
    }

    public String getDomain() {
        return pref.getString(DOMAIN, "demo");
    }


}
