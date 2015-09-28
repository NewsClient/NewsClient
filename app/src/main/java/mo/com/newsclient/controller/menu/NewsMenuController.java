package mo.com.newsclient.controller.menu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import mo.com.newsclient.R;
import mo.com.newsclient.activity.HomeUI;
import mo.com.newsclient.bean.NewscenterBean;
import mo.com.newsclient.controller.BaseController;
import mo.com.newsclient.controller.news.NewsListController;


/**
 * 作者：MoMxMo on 2015/9/24 21:46
 * 邮箱：xxxx@qq.com
 */


public class NewsMenuController extends BaseController implements ViewPager.OnPageChangeListener, SlidingMenu.OnOpenedListener, SlidingMenu.OnOpenListener, SlidingMenu.OnClosedListener, SlidingMenu.OnCloseListener {

    private static final String TAG = "NewsMenuController";

    @ViewInject(R.id.menu_news_view_pager)
    private ViewPager mViewPager;

    @ViewInject(R.id.menu_news_indicator)
    private TabPageIndicator mIndicator;

    private List<NewscenterBean.NewsListBean> mListNewsData;

    public NewsMenuController(Context context, List<NewscenterBean.NewsListBean> beanList) {
        super(context);
        mListNewsData = beanList;

        Log.i(TAG, "beanList: " + beanList.size());
    }

    @Override
    protected View initView(Context context) {

        //using xUtils Framework can't be reflect  Xml layout id
        View view = View.inflate(context, R.layout.menu_news, null);

        //Using xUtils FrameWork
        //ViewUtils.inject(Object handler, View view)
        com.lidroid.xutils.ViewUtils.inject(NewsMenuController.this, view);

        //初始化的时候，设置监听
        HomeUI homeUI= (HomeUI) mContext;
        SlidingMenu slidingMenu = homeUI.getSlidingMenu();
        slidingMenu.setOnOpenedListener(this);
        slidingMenu.setOnOpenListener(this);
        slidingMenu.setOnClosedListener(this);
        slidingMenu.setOnCloseListener(this);
        return view;
    }

    /**
     * 点击翻页
     *
     * @param view
     */
    @OnClick(R.id.menu_news_arrow)
    public void clickArrow(View view) {
        /*选中下一页*/
        int currentItem = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(++currentItem);

    }

    public void initData() {
        //add customes adapter to viewPager
        NewsMenuAdaper mAdapter = new NewsMenuAdaper();
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
    public void onPageSelected(int position) {
        HomeUI homeUI = (HomeUI) mContext;
        SlidingMenu slidingMenu = homeUI.getSlidingMenu();
                /*如果当前选中的是第一位，可以拉动菜单*/
        if (position == 0) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }

    @Override
    public void onOpened() {
        notifyAllListener();
    }

    @Override
    public void onOpen() {
        notifyAllListener();
    }

    @Override
    public void onClosed() {
        notifyAllListener();
    }

    @Override
    public void onClose() {
        notifyAllListener();

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
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            NewscenterBean.NewsListBean bean = mListNewsData.get(position);
            NewsListController controller = new NewsListController(mContext, bean);
            View rootView = controller.getRootView();
            container.addView(rootView);
            controller.initData();

            /*设置监听事件*/
//            setOnViewiDLEListener(controller);

            //将标记传给destroy方法
            rootView.setTag(controller);
            addOnViewiDLEListener(controller);

            return rootView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View rootView = (View) object;
            NewsListController controller = (NewsListController) rootView.getTag();
            addOnViewiDLEListener(controller);

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

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {

            //通知一个改变
            if (mListeners != null) {
                notifyAllListener();
            }
        }
    }

///*    //1.一个监听
//    private OnViewiDLEListener mListener;

    private List<OnViewiDLEListener> mListeners = new ArrayList<OnViewiDLEListener>();
//
//    //2.设置一个监听
//    public void setOnViewiDLEListener(OnViewiDLEListener listener) {
//        mListener = listener;
//    }*/

    public void addOnViewiDLEListener(OnViewiDLEListener listener) {
        mListeners.add(listener);
    }

    public void removeOnViewiDLEListener(OnViewiDLEListener listener) {
        mListeners.remove(listener);
    }

    public void notifyAllListener() {
        ListIterator<OnViewiDLEListener> iterator = mListeners.listIterator();
        while (iterator.hasNext()) {
            OnViewiDLEListener listener = iterator.next();
            listener.onIDLE();
        }
    }

    public interface OnViewiDLEListener {
        void onIDLE();
    }


}
