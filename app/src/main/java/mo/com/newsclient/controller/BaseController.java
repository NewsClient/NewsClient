package mo.com.newsclient.controller;

import android.content.Context;
import android.view.View;

/**
 * @项目名: NewsClient
 * @包名: mo.com.newsclient.controller
 * @类名: ${TYPE_NAME}
 * @创建者: MoMxMo on 2015/9/23 15:02
 * @创建时间: 2015/9/23	15:02
 * @描述:  tab controller base tyle
 * @邮箱: xxxx@qq.com
 * @git版本: $$REV$$
 * @更新人: $$AUTHOR$$
 * @更新时间: $$Date$$
 * @更新描述: TODO
 */

public abstract class BaseController {
    protected Context mContext;
    protected View mRootView;
    public BaseController(Context context) {
        mContext = context;
        mRootView = initView(context);
    }

    /*init our View*/
    protected abstract View initView(Context context);

    /**
     * is provider get root view
     *
     * @return
     */
    public View getRootView() {
        return mRootView;
    }

    /**
     * this method override if subClass is need to load data
     */
    public void initData() {

    }

}
