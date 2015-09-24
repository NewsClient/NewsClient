package mo.com.newsclient.controller.tab;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import mo.com.newsclient.R;
import mo.com.newsclient.bean.NewscenterBean;
import mo.com.newsclient.controller.TabController;
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

    public NewsCenterController(Context context) {
        super(context);

    }

    @Override
    protected View initContentView(Context context) {
        TextView tv = new TextView(context);
        tv.setText("新闻中心 Content");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(30);
        tv.setTextColor(context.getResources().getColor(R.color.normal_red));
        return tv;
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

                Log.i(TAG, "onSuccess");

                Log.i(TAG, ""+json);

                Gson gson = new Gson();
                NewscenterBean data =  gson.fromJson(json, NewscenterBean.class);

                Log.i(TAG, " "+data.data.get(0).children.get(0).title);

            }

            @Override
            public void onFailure(HttpException e, String s) {

                Log.i(TAG, "onFailure");

            }
        });




    }
}
