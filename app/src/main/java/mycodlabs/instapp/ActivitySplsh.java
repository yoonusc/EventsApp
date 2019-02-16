package mycodlabs.instapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.roger.catloadinglibrary.CatLoadingView;

import mycodlabs.events.R;


public class ActivitySplsh extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    public CatLoadingView mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mView=new CatLoadingView();
        showDialog();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                try {
                    hideDialog();
                } catch (IllegalStateException ignored) {
                    // There's no way to avoid getting this if saveInstanceState has already been called.
                }
                Intent i = new Intent(ActivitySplsh.this, LoginActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    public void showDialog() {
        mView.show(getSupportFragmentManager(), "");
    }
    public void hideDialog() {
        mView.dismiss();
    }
}