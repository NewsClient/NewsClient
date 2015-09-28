package mo.com.newsclient.controller.news;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import mo.com.newsclient.R;
import mo.com.newsclient.activity.DetaiUI;
import mo.com.newsclient.bean.NewsPagerBean;
import mo.com.newsclient.bean.NewscenterBean;
import mo.com.newsclient.controller.BaseController;
import mo.com.newsclient.controller.menu.NewsMenuController;
import mo.com.newsclient.utils.Constants;
import mo.com.newsclient.utils.DimenUtils;
import mo.com.newsclient.utils.PreferenceUtils;
import mo.com.newsclient.view.FocusedViewPager;

/**
 * 作者：MoMxMo on 2015/9/26 16:11
 * 邮箱：xxxx@qq.com
 */


public class NewsListController extends BaseController implements ViewPager.OnPageChangeListener,NewsMenuController.OnViewiDLEListener, AdapterView.OnItemClickListener {

    private static final String TAG = "NewsListController";
    private NewscenterBean.NewsListBean mData;
    private List<NewsPagerBean.Data.Topnews> mTopnews;
    private  List<NewsPagerBean.Data.News> newsData;

    @ViewInject(R.id.news_list_viewpager)
    private FocusedViewPager mViewPager;

    @ViewInject(R.id.news_list_tv_title)
    private TextView mTv_title;

    @ViewInject(R.id.news_list_point_container)
    private LinearLayout mPointContainer;

    @ViewInject(R.id.news_list_listview)
    private ListView mListView;
//    private RefreshListView mListView;

    private NewsListPicAdapter mAapter;
    BitmapUtils mBitmapUtils;
    private SwitchHandler mSwitchHandler;

    public NewsListController(Context context, NewscenterBean.NewsListBean bean) {
        super(context);
        this.mData = bean;
    }

    @Override
    protected View initView(Context context) {
        View view = View.inflate(context, R.layout.news_list, null);
        mBitmapUtils   = new BitmapUtils(mContext);
        ViewUtils.inject(this, view);


        View topViewPic = View.inflate(context, R.layout.news_top_pic, null);
        mListView.addHeaderView(topViewPic);
        ViewUtils.inject(this, topViewPic);
        return view;
    }

    @Override
    public void initData() {
        /*初始化View的地方不能勾加载数据，不然会报nullPoint异常*/
        /*加载缓存数据*/
        String url = Constants.BASE_SERVER + mData.url;
        String json = PreferenceUtils.getString(mContext, url);
        if (!TextUtils.isEmpty(json)) {
            processJson(json);
        }

        /*加载网络中的新闻数据*/
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                /*加载数据成*/
                /*获取数据*/
                String json = responseInfo.result;

                /*保存缓存数据*/
                PreferenceUtils.putString(mContext, json, null);

                /*解析数据*/
                processJson(json);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                /*加载数据失败*/
                Toast.makeText(mContext, "抱歉，联网失败", Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * 解析数据
     *
     * @param json
     */
    private void processJson(String json) {


        Gson gson = new Gson();
        NewsPagerBean newsPagerBean = gson.fromJson(json, NewsPagerBean.class);

        /*判断加载的数据是否正确*/
        if (newsPagerBean.retcode == 200) {
        /*获取轮播图片数据*/
            Log.i(TAG, "................. "+newsPagerBean.data.title);
            mTopnews = newsPagerBean.data.topnews;

            //新闻数据列表
            newsData= newsPagerBean.data.news;
        }

        /*网络加载轮播图的图片,数据*/
        mAapter = new NewsListPicAdapter();
        mViewPager.setAdapter(mAapter);

        //TODO
        mListView.setAdapter(new NewsAdapter());

        //设置点击事件
        mListView.setOnItemClickListener(this);

        /*加载点*/
        /*清楚*/
        mPointContainer.removeAllViews();
        for (int i = 0; i < mTopnews.size(); i++) {
            NewsPagerBean.Data.Topnews bean = mTopnews.get(i);
            View view = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DimenUtils.pdTopx(mContext,10),DimenUtils.pdTopx(mContext,10));

            if (i != 0) {
                params.leftMargin = DimenUtils.pxTopd(mContext, 10);
                view.setBackgroundResource(R.drawable.dot_normal);
            } else {
                /*选中*/
                view.setBackgroundResource(R.drawable.dot_focus);
            }
         /*设置图片标题*/
            mTv_title.setText(bean.title);
            mPointContainer.addView(view,params);
        }

        mViewPager.setOnPageChangeListener(this);


        //实现动画轮播
        if (mSwitchHandler == null) {
            mSwitchHandler = new SwitchHandler();
        }
        mSwitchHandler.start();

        //监听轮播
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //停止轮播
                        mSwitchHandler.stop();
                        Log.i(TAG, "按下 ");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        //开始轮播
                        mSwitchHandler.start();
                        Log.i(TAG, "弹起 ");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        //如果父容器抢走事件，会通知child view
                        Log.i(TAG, "取消 ");
                        break;
                }
                return false;
            }
        });

   }

    /**
     * 点击进入新闻详情
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick 点击新闻详情");

        /*先消除ListView中的头部分*/
        position = position - mListView.getHeaderViewsCount();

        NewsPagerBean.Data.News news = newsData.get(position);
        Intent intent = new Intent(mContext,DetaiUI.class);
        intent.putExtra(DetaiUI.KEY_URL, news.url);
        mContext.startActivity(intent);

    }

    /**
     * 新闻适配器
     */
    private class NewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (newsData != null) {
                return newsData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (newsData != null) {
                return newsData.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_news_list, null);
                mHolder = new ViewHolder();
                mHolder.mComment = (ImageView) convertView.findViewById(R.id.item_news_comment);
                mHolder.mTime = (TextView) convertView.findViewById(R.id.item_news_time);
                mHolder.mTitile = (TextView) convertView.findViewById(R.id.item_news_titile);
                mHolder.mIcon = (ImageView) convertView.findViewById(R.id.item_news_icon);
                convertView.setTag(mHolder);
            } else {
                mHolder= (ViewHolder) convertView.getTag();
            }
            NewsPagerBean.Data.News news = newsData.get(position);

            /*设置默认的图片，防止加载网络图过慢*/
            mHolder.mIcon.setImageResource(R.drawable.pic_item_list_default);
            mBitmapUtils.display(mHolder.mIcon, news.listimage);
            mHolder.mTitile.setText(news.title);
            mHolder.mTime.setText(news.pubdate);
            if (news.comment) {
                mHolder.mComment.setImageResource(R.drawable.icon_news_comment_num);
            }
            return convertView;
        }
    }
    private class ViewHolder{
        public ImageView mIcon;
        public TextView mTitile;
        public TextView mTime;
        public ImageView mComment;
    }
    private class SwitchHandler extends Handler implements Runnable {

        @Override
        public void run() {
            //执行轮播
            int count = mViewPager.getAdapter().getCount();
            int currentItem = mViewPager.getCurrentItem();
            if (currentItem == count - 1) {
                currentItem = 0;
            } else {
                currentItem++;
            }
            mViewPager.setCurrentItem(currentItem);

            //继续轮播
            postDelayed(this, 2000);
        }

        /**
         * 开始轮播
         */
        public void start() {
            stop();
            postDelayed(this, 2000);
        }

        /**
         * 停止轮播
         */
        public void stop() {
            removeCallbacks(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        NewsPagerBean.Data.Topnews bean = mTopnews.get(position);

        /*选中时，改变标题*/
        mTv_title.setText(bean.title);
        int count = mPointContainer.getChildCount();

                /*改变图标*/
        for (int i = 0; i < count; i++) {
            View view = mPointContainer.getChildAt(i);
            view.setBackgroundResource(i==position?R.drawable.dot_focus:R.drawable.dot_normal);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class NewsListPicAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mTopnews != null) {
                return mTopnews.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewsPagerBean.Data.Topnews bean = mTopnews.get(position);

            ImageView mImageView = new ImageView(mContext);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);

            if (mBitmapUtils == null) {
                mBitmapUtils = new BitmapUtils(mContext);
            }
            // 加载网络图片
            mBitmapUtils.display(mImageView, bean.topimage);
            container.addView(mImageView);


            return mImageView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onIDLE() {

        //开始轮播图片
        if (mSwitchHandler!=null) {
            mSwitchHandler.start();
        }
    }
}
