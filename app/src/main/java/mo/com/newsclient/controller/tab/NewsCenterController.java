package mo.com.newsclient.controller.tab;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import mo.com.newsclient.R;
import mo.com.newsclient.activity.HomeUI;
import mo.com.newsclient.bean.NewscenterBean;
import mo.com.newsclient.controller.BaseController;
import mo.com.newsclient.controller.TabController;
import mo.com.newsclient.controller.menu.InteractMenuController;
import mo.com.newsclient.controller.menu.NewsMenuController;
import mo.com.newsclient.controller.menu.PicMenuController;
import mo.com.newsclient.controller.menu.SubjectMenuController;
import mo.com.newsclient.fragment.MenuFragment;
import mo.com.newsclient.utils.Constants;
import mo.com.newsclient.utils.PreferenceUtils;

/**
 * @项目名: NewsClient
 * @包名: mo.com.newsclient.controller.tab
 * @类名: ${TYPE_NAME}
 * @创建者: MoMxMo on 2015/9/23 21:02
 * @创建时间: 2015/9/23	21:02
 * @描述: TODO
 * @邮箱: xxxx@qq.com
 * @git版本: $$REV$$
 * @更新人: $$AUTHOR$$
 * @更新时间: $$Date$$
 * @更新描述: TODO
 */

public class NewsCenterController extends TabController implements View.OnClickListener{
    private static final String TAG = "NewsCenterController";
    private ArrayList<BaseController> mMenuController;
    private FrameLayout mContainer;
    private List<NewscenterBean.NewsMenuBean> mMenuData;
    private OnListOrGridListener mListener;

    public NewsCenterController(Context context) {
        super(context);

    }

    @Override
    protected View initContentView(Context context) {
     /*   TextView tv = new TextView(context);
        tv.setText("新闻中心 Content");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(30);
        tv.setTextColor(context.getResources().getColor(R.color.normal_red));*/
        mContainer = new FrameLayout(context);
        return mContainer;
    }

    @Override
    public void initData() {
        mTitle.setText(mContext.getResources().getString(R.string.tab_news_center));
        mMeun.setVisibility(View.VISIBLE);
        final String url = Constants.NEWS_CENTER_URL;

        /*使用缓存方法缓存数据*/
        String cache = PreferenceUtils.getString(mContext, url);
        if (!TextUtils.isEmpty(cache)) {
            /*如果缓存数据不为空*/

            /*获取上次加载的时间，根据业务需要缓存数据的有效事件*/
            long lastTime = PreferenceUtils.getLong(mContext, url + "-time");
            /*这里我们模拟设置缓存的有效时间是5s*/

            if (System.currentTimeMillis() - 5000 > lastTime) {

                /*显示缓存数据*/
                processJson(cache);
                 /*加载缓存中的网络的数据*/
                loadNetWorkData(url);
            } else {
                /*加载缓存数据*/

             /*解析缓存数据*/
                processJson(cache);
            }

        }

        Log.i(TAG, "loading newworkData");
        /**
         * load data from newwork and resovle json data
         *
         * we need to import the xUtils  and gson framewoek,it is open resorces
         */

        loadNetWorkData(url);
    }

    //加载网络数据
    private void loadNetWorkData(final String url) {
        HttpUtils mHttp = new HttpUtils();

        mHttp.send(HttpRequest.HttpMethod.GET, url, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Our requested data from network was success
                //now ,we can resovl json data

                String json = responseInfo.result;

                /*保存缓存数据*/
                PreferenceUtils.putString(mContext, url, null);

                 /*保存这次加载的事件*/
                PreferenceUtils.putLong(mContext, url + "-time", System.currentTimeMillis());

                processJson(json);
            }

            @Override
            public void onFailure(HttpException e, String s) {

                /**
                 * can't connect network:
                 * cause:
                 * 1.phone don't connet the internet
                 *2.server was destory
                 */
                e.printStackTrace();
                Log.i(TAG, "onFailure");
            }
        });
    }


    /**
     * parse json data
     *
     * @param json
     */
    private void processJson(String json) {
        Gson gson = new Gson();
        NewscenterBean bean = gson.fromJson(json, NewscenterBean.class);
        mMenuData = bean.data;
        Log.i(TAG, "mMenuData:" + mMenuData.get(0).title);
        /*
        acquire MenuFregment ,and set data
         */


        mMenuController = new ArrayList<BaseController>();
        for (int i = 0; i < mMenuData.size(); i++) {
            NewscenterBean.NewsMenuBean menuBean = mMenuData.get(i);
            switch (menuBean.type) {
                case 1:
                    //news menu
                    mMenuController.add(new NewsMenuController(mContext, menuBean.children));
                    break;

                case 10:
                    //subject menu
                    mMenuController.add(new SubjectMenuController(mContext));
                    break;

                case 2:
                    //pic menu
                    mMenuController.add(new PicMenuController(mContext,menuBean));
                    break;

                case 3:
                    //interact menu
                    mMenuController.add(new InteractMenuController(mContext));
                    break;
            }

        }
//        switchMenu(0);

        HomeUI homeUI = (HomeUI) mContext;
        MenuFragment menuFragment = homeUI.getMenuFragment();
        menuFragment.setData(mMenuData);
    }

    /**
     * check Menu content
     *
     * @param position
     */
    @Override
    public void switchMenu(int position) {
        // clear container
        mContainer.removeAllViews();
        BaseController controller = mMenuController.get(position);
        View rootView = controller.getRootView();

        // add mTitle text data of list NewsMenuBean
        NewscenterBean.NewsMenuBean newsMenuBean = mMenuData.get(position);
        mTitle.setText(newsMenuBean.title);
        mContainer.addView(rootView);

        if (controller instanceof PicMenuController) {
            //if controller is picMenuController ,we need to show mListOrGrid
            mListOrGrid.setVisibility(View.VISIBLE);
            mListOrGrid.setOnClickListener(this);

            PicMenuController picMenuController = (PicMenuController) controller;
            setOnListOrGridListener(picMenuController);
        } else {
            mListOrGrid.setVisibility(View.GONE);
        }

        controller.initData();
    }

    @Override
    public void onClick(View v) {
        if (v==mListOrGrid) {
            if (mListener != null) {
                mListener.onClick(mListOrGrid);
            }
        }
    }
    public void setOnListOrGridListener(OnListOrGridListener listener) {
        mListener = listener;
    }

    public interface OnListOrGridListener {
        void onClick(ImageView imageView);
    }
}
