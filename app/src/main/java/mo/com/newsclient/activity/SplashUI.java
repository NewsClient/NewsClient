package mo.com.newsclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import mo.com.newsclient.R;
import mo.com.newsclient.utils.PreferenceUtils;

/**
 * 作者：MoMxMo on 2015/9/22 12:26
 * 邮箱：xxxx@qq.com
 * <p/>
 * 欢迎界面
 */

public class SplashUI extends Activity {

    public static final java.lang.String KEY_FIRST_ENTER = "key_first_enter";
    private static final long ANIMATION_DURATION = 2000;
    private LinearLayout mSplashView;
    private AnimationSet mAnimationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        startAnimation();
    }

    private void startAnimation() {
    /*旋转*/
        RotateAnimation mRotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_PARENT, 0.5f,
                Animation.RELATIVE_TO_PARENT, 0.5f);
          /*缩放*/
        ScaleAnimation mScale = new ScaleAnimation(0f, 1f,
                0f, 1f,
                Animation.RELATIVE_TO_PARENT, 0.5f,
                Animation.RELATIVE_TO_PARENT, 0.5f);

        /*透明*/
        AlphaAnimation mAlpha = new AlphaAnimation(0, 1);
        /*动画集合*/
        mAnimationSet = new AnimationSet(true);
        mAnimationSet.addAnimation(mRotate);
        mAnimationSet.addAnimation(mScale);
        mAnimationSet.addAnimation(mAlpha);
        mAnimationSet.setDuration(ANIMATION_DURATION);
        mAnimationSet.setAnimationListener(new SplashAnimationListener());
        /*开始动画*/
        mSplashView.startAnimation(mAnimationSet);
    }

    private void initView() {
        mSplashView = (LinearLayout) findViewById(R.id.ll_splash);

    }

    private class SplashAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                         /*动画执行结束之后，进入主页或者设置向导界面*/
                    boolean isFirst = PreferenceUtils.getBoolean(SplashUI.this, KEY_FIRST_ENTER, true);
                    if (isFirst ) {

                    /*第一次进入，进入向导界面*/
                        Intent intent = new Intent(SplashUI.this, GuideUI.class);
                        startActivity(intent);
                        finish();

                    } else {
                    /*不是第一次进入，进入主页*/
                        Intent intent = new Intent(SplashUI.this, HomeUI.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }).start();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
