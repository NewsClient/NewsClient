package mo.com.newsclient.controller.menu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.List;

import mo.com.newsclient.R;
import mo.com.newsclient.bean.NewscenterBean;
import mo.com.newsclient.controller.BaseController;


/**
 * 作者：MoMxMo on 2015/9/24 21:46
 * 邮箱：xxxx@qq.com
 */


public class NewsMenuController extends BaseController implements ViewPager.OnPageChangeListener {

    private static final String TAG = "NewsMenuController";

    @ViewInject(R.id.menu_news_view_pager)
    ViewPager mViewPager;

    @ViewInject(R.id.menu_news_indicator)
    private TabPageIndicator mIndicator;

    private List<NewscenterBean.NewsListBean> mListNewsData;

    public NewsMenuController(Context context,List<NewscenterBean.NewsListBean> beanList) {
        super(context);
        mListNewsData = beanList;

        Log.i(TAG, "beanList: "+beanList.size());
    }
    @Override
    protected View initView(Context context) {

        //using xUtils Framework can't be reflect  Xml layout id
        View view = View.inflate(context, R.layout.menu_news, null);

        //Using xUtils FrameWork
        //ViewUtils.inject(Object handler, View view)
        com.lidroid.xutils.ViewUtils.inject(NewsMenuController.this, view);
 /*       TextView tv = new TextView(mContext);
        tv.setText("菜单 新闻");
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(R.color.normal_width);
        tv.setTextColor(mContext.getResources().getColor(R.color.normal_red));
        tv.setTextSize(25);*/
        return view;
    }
    public void initData() {
        //add customes adapter to viewPager
        NewsMenuAdaper  mAdapter=  new NewsMenuAdaper();
        mViewPager.setAdapter(mAdapter);


        // 给indicator设置viewpager
        mIndicator.setViewPager(mViewPager);

        /*给indicator设置监听事件*/
        mIndicator.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public class NewsMenuAdaper extends PagerAdapter {
        @Override
        public int getCount() {
            if (mListNewsData != null) {
                return mListNewsData.size();
            }
            return 0;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView tv = new TextView(mContext);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(R.color.normal_width);
            tv.setTextColor(mContext.getResources().getColor(R.color.normal_orange));
            tv.setTextSize(25);
            NewscenterBean.NewsListBean children = mListNewsData.get(position);
            tv.setText(children.title+" 新闻");

            container.addView(tv);
            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (mListNewsData != null) {
                NewscenterBean.NewsListBean bean = mListNewsData.get(position);
                return bean.title;
            }
            return super.getPageTitle(position);
        }
    }



}
