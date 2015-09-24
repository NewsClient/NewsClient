package mo.com.newsclient.fragment;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import mo.com.newsclient.R;
import mo.com.newsclient.activity.HomeUI;
import mo.com.newsclient.controller.BaseController;
import mo.com.newsclient.controller.TabController;
import mo.com.newsclient.controller.tab.GovTabController;
import mo.com.newsclient.controller.tab.HomeTabController;
import mo.com.newsclient.controller.tab.NewsCenterController;
import mo.com.newsclient.controller.tab.SettingController;
import mo.com.newsclient.controller.tab.SmartSericeController;
import mo.com.newsclient.view.LazyViewPager;

/**
 * @项目名: NewsClient
 * @包名: mo.com.newsclient.activity
 * @类名: ${TYPE_NAME}
 * @创建者: MoMxMo on 2015/9/23 14:58
 * @创建时间: 2015/9/23	14:58
 * @描述: TODO
 * @邮箱: xxxx@qq.com
 * @git版本: $$REV$$
 * @更新人: $$AUTHOR$$
 * @更新时间: $$Date$$
 * @更新描述: TODO
 */

public class ContentFrament extends BaseFrament implements RadioGroup.OnCheckedChangeListener {


    private static final String TAG = "ContentFrament";
    private LazyViewPager mViewPager;       //using Lazy data
    private RadioGroup mRg;
    private List<TabController> mListData;
    private ContentViewPagerAdapter mAdatper;
    private Activity mAcitivity;
    private RadioGroup mRg1;
    int mCurrent = 0;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.content, null);
        mAcitivity = getActivity();
        mViewPager = (LazyViewPager) view.findViewById(R.id.content_viewpager);
        mRg = (RadioGroup) view.findViewById(R.id.content_rg);

        mRg.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void initData() {
        /*add ViewPage Data*/
        if (mListData == null) {
            mListData = new ArrayList<TabController>();
        } else {
            mListData.clear();
        }
   /*
      this is add data test

      for (int i = 0; i < 5; i++) {
            TextView tv = new TextView(mActivity);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.normal_red));
            tv.setText("内容" + i);
            mListData.add(tv);
        }*/

        mListData.add(new HomeTabController(mAcitivity));
        mListData.add(new NewsCenterController(mAcitivity));
        mListData.add(new SmartSericeController(mAcitivity));
        mListData.add(new GovTabController(mAcitivity));
        mListData.add(new SettingController(mAcitivity));

        mAdatper = new ContentViewPagerAdapter();
        mViewPager.setAdapter(mAdatper);

        //default selected RadioGroup
        mRg.check(R.id.content_rb_home);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.content_rb_home:
                mCurrent = 0;
                //不可以打开菜单
                enableSildingMenu(false);
                break;
            case R.id.content_rb_newscenter:
                mCurrent = 1;
                //可以打开菜单
                enableSildingMenu(true);
                break;
            case R.id.content_rb_smart_service:
                mCurrent = 2;
                //可以打开菜单
                enableSildingMenu(true);
                break;
            case R.id.content_rb_gvo:
                mCurrent = 3;
                //可以打开菜单
                enableSildingMenu(true);
                break;
            case R.id.content_rb_setting:
                mCurrent = 4;
                //不可以打开菜单
                enableSildingMenu(false);
                break;

        }
        mViewPager.setCurrentItem(mCurrent);
    }

    private void enableSildingMenu(boolean flag) {
        HomeUI homeUI= (HomeUI) mActivity;
        SlidingMenu slidingMenu = homeUI.getSlidingMenu();
        slidingMenu.setTouchModeAbove(flag?SlidingMenu.TOUCHMODE_FULLSCREEN:SlidingMenu.TOUCHMODE_NONE);
    }

    /**
     * select show menu
     * @param position
     */
    public  void switchContent(int position) {
        //find current menu controller
        TabController mCurrentController   = mListData.get(mCurrent);

        //notify controller of the menu change
        mCurrentController.switchMenu(position);
    }

    /*It is content viewpager Adapter */
    private class ContentViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mListData != null) {
                return mListData.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i(TAG, "instantiateItem 加载："+position);
            BaseController baseController = mListData.get(position);
            View rootView = baseController.getRootView();
            container.addView(rootView);
            baseController.initData();
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i(TAG, "destroyItem 销毁："+position);
            container.removeView((View) object);
        }
    }
}
