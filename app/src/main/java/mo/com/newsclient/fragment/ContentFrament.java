package mo.com.newsclient.fragment;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import mo.com.newsclient.R;
import mo.com.newsclient.controller.BaseController;
import mo.com.newsclient.controller.tab.GovTabController;
import mo.com.newsclient.controller.tab.HomeTabController;
import mo.com.newsclient.controller.tab.NewsCenterController;
import mo.com.newsclient.controller.tab.SettingController;
import mo.com.newsclient.controller.tab.SmartSericeController;
import mo.com.newsclient.view.NoScrollViewPager;

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


    private NoScrollViewPager mViewPager;
    private RadioGroup mRg;
    private List<BaseController> mListData;
    private ContentViewPagerAdapter mAdatper;
    private Activity mAcitivity;
    private RadioGroup mRg1;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.content, null);
        mAcitivity = getActivity();
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.content_viewpager);
        mRg = (RadioGroup) view.findViewById(R.id.content_rg);

        mRg.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void initData() {
        /*add ViewPage Data*/
        if (mListData == null) {
            mListData = new ArrayList<BaseController>();
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
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.content_rb_home:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.content_rb_newscenter:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.content_rb_smart_service:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.content_rb_gvo:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.content_rb_setting:
                mViewPager.setCurrentItem(4);
                break;

        }
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
            BaseController baseController = mListData.get(position);
            View rootView = baseController.getRootView();
            container.addView(rootView);
            baseController.initData();
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
