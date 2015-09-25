package mo.com.newsclient.controller.tab;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

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

public class NewsCenterController extends TabController {
    private static final String TAG = "NewsCenterController";
    private ArrayList<BaseController> mMenuController;
    private FrameLayout mContainer;
    private List<NewscenterBean.NewsMenuBean> mMenuData;

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

        Log.i(TAG, "loading newworkData");
        /**
         * load data from newwork and resovle json data
         *
         * we need to import the xUtils  and gson framewoek,it is open resorces
         */

        HttpUtils mHttp = new HttpUtils();

        mHttp.send(HttpRequest.HttpMethod.GET, Constants.NEWS_CENTER_URL, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Our requested data from network was success
                //now ,we can resovl json data

                String json = responseInfo.result;
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
        HomeUI homeUI = (HomeUI) mContext;
        MenuFragment menuFragment = homeUI.getMenuFragment();
        menuFragment.setData(mMenuData);

        mMenuController = new ArrayList<BaseController>();
        for (int i = 0; i < mMenuData.size(); i++) {
            NewscenterBean.NewsMenuBean menuBean = mMenuData.get(i);
            switch (menuBean.type) {
                case 1:
                    //news menu
                    mMenuController.add(new NewsMenuController(mContext,menuBean.children));
                    break;

                case 10:
                    //subject menu
                    mMenuController.add(new SubjectMenuController(mContext));
                    break;

                case 2:
                    //pic menu
                    mMenuController.add(new PicMenuController(mContext));
                    break;

                case 3:
                    //interact menu
                    mMenuController.add(new InteractMenuController(mContext));
                    break;
            }

        }
        switchMenu(0);
    }

    /**
     * check Menu content
     * @param position
     */
    @Override
    public void switchMenu(int position) {
        // 清空内容
        mContainer.removeAllViews();
        BaseController controller = mMenuController.get(position);
        View rootView = controller.getRootView();

        // add mTitle text data of list NewsMenuBean
        NewscenterBean.NewsMenuBean newsMenuBean = mMenuData.get(position);
        mTitle.setText(newsMenuBean.title);
        mContainer.addView(rootView);
        controller.initData();
    }
}
