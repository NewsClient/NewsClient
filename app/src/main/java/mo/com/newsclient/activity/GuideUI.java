package mo.com.newsclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import mo.com.newsclient.R;
import mo.com.newsclient.utils.DimenUtils;
import mo.com.newsclient.utils.PreferenceUtils;

public class GuideUI extends Activity {

    private ViewPager mViewPager;
    private List<ImageView> mListData;
    private int[] icons = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private LinearLayout mPointContainer;
    private Button btn_guide;
    private View mViewPoint;
    private int mLeftMargin = 15;
    private int mPointSize = 12;
    int mPointSpace = 0;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_ui);

        initView();
        initdata();
        initEvent();
    }

    private void initdata() {
        if (mListData == null) {
            mListData = new ArrayList<ImageView>();
        } else {
            mListData.clear();
        }
        for (int i = 0; i < icons.length; i++) {
            ImageView iv = new ImageView(GuideUI.this);
            iv.setImageResource(icons[i]);
            mListData.add(iv);
            View view = new View(GuideUI.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DimenUtils.pdTopx(GuideUI.this, mPointSize),
                    DimenUtils.pdTopx(GuideUI.this, mPointSize));
            if (i != 0) {
                params.leftMargin = DimenUtils.pdTopx(GuideUI.this, mLeftMargin);
            }
            view.setBackgroundResource(R.drawable.point_normal);
            mPointContainer.addView(view, params);
        }

    }

    private void initEvent() {

        mViewPager.setAdapter(new GuidePagerViewAdapter());

        // 设置全局布局布局完成改变时的回调
        mViewPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                /*通过获取mPointContainer中的孩子自己的距离*/
                mPointSpace= mPointContainer.getChildAt(1).getLeft() - mPointContainer.getChildAt(0).getLeft();

                mViewPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            }
        });

        /*点击进入主页*/
        btn_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideUI.this, HomeUI.class);
                startActivity(intent);
                finish();
                /*设置标记*/
                PreferenceUtils.putBoolean(GuideUI.this, SplashUI.KEY_FIRST_ENTER, false);
            }
        });

        /*监听页面的切换*/
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /*viewpage滑动的回调
                     int position    当前的位置
                    float positionOffset   滑动的比例
                      int positionOffsetPixels 滑动的像素
                 */

                /*计算选中原点的移动的位置*/

                int mLeft = (int) (mPointSpace * positionOffset + position * mPointSpace);

                RelativeLayout.LayoutParams
                        params = (RelativeLayout.LayoutParams) mViewPoint.getLayoutParams();
                params.leftMargin = mLeft;
                mViewPoint.setLayoutParams(params);

            }

            @Override
            public void onPageSelected(int i) {
                /*当滑动到最后一个页面的时候显示button*/
                if (i == mListData.size() - 1) {
                    btn_guide.setVisibility(View.VISIBLE);
                } else {
                    btn_guide.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        mPointContainer = (LinearLayout) findViewById(R.id.guide_point);
        btn_guide = (Button) findViewById(R.id.btn_guide_welcome);
        mViewPoint = findViewById(R.id.view_seleted);
    }

    public class GuidePagerViewAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (mListData != null) {
                return mListData.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView view = mListData.get(position);
            container.addView(view);

            /*返回标记*/
            return view;
        }

        /*销毁*/
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

}
