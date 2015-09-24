package mo.com.newsclient.controller;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import mo.com.newsclient.R;

/**
 * @项目名: NewsClient
 * @包名: mo.com.newsclient.controller.tab
 * @类名: ${TYPE_NAME}
 * @创建者: MoMxMo on 2015/9/23 20:15
 * @创建时间: 2015/9/23	20:15
 * @描述: include common features with all TabsubClass
 * @邮箱: xxxx@qq.com
 * @git版本: $$REV$$
 * @更新人: $$AUTHOR$$
 * @更新时间: $$Date$$
 * @更新描述: TODO
 */

public abstract class TabController extends BaseController {

    protected TextView mTitle;
    protected ImageView mMeun;
    protected FrameLayout mContentLayout;

    public TabController(Context context) {
        super(context);
    }
    @Override
    protected View initView(Context context) {
        View view = View.inflate(context, R.layout.tab_layout, null);
        mMeun = (ImageView) view.findViewById(R.id.tab_iv_meun);
        mTitle = (TextView) view.findViewById(R.id.tab_tv_title);
        mContentLayout = (FrameLayout) view.findViewById(R.id.tab_fl_content);

        /**
         * FrameLayout add a view on use
         */
        mContentLayout.addView(initContentView(context));

        return view;
    }

    /**
     * init content a part of view
     *
     * @param context
     * @return
     */
    protected abstract View initContentView(Context context);
}
