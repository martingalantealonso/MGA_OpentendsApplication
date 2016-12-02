package com.galante.martin.opentendsapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Martin on 02/12/2016.
 */

public class SplashScreen extends Activity {

    Thread splashTread;
    private static final String TAG = "SplashScreen";

    //TODO delete this if nothing is done
    /*public void onAttachedToWindow() {
        super.onAttachedToWindow();
               //Window window = getWindow();
        //window.setFormat(PixelFormat.RGBA_8888);
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        StartAnimations();
    }

    private void StartAnimations() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000); //Sleep the thread for two seconds while the welcome screen is displayed
                    Intent intent = new Intent(SplashScreen.this, FirstActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);//launch the main activity with an animation
                    startActivity(intent);
                    SplashScreen.this.finish(); //finish the SplashScreen
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                } finally {
                    SplashScreen.this.finish();
                }

            }
        };
        splashTread.start();

    }

}