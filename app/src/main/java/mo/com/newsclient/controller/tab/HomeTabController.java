package mo.com.newsclient.controller.tab;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import mo.com.newsclient.R;
import mo.com.newsclient.controller.TabController;

/**
 * @项目名: NewsClient
 * @包名: mo.com.newsclient.controller.tab
 * @类名: ${TYPE_NAME}
 * @创建者: MoMxMo on 2015/9/23 20:09
 * @创建时间: 2015/9/23	20:09
 * @描述: TODO
 * @邮箱: xxxx@qq.com
 * @git版本: $$REV$$
 * @更新人: $$AUTHOR$$
 * @更新时间: $$Date$$
 * @更新描述: TODO
 */

public class HomeTabController extends TabController {
    public HomeTabController(Context context) {
        super(context);
    }

    /**
     * this is content View
     * @param context
     * @return
     */
    @Override
    protected View initContentView(Context context) {
        TextView tv = new TextView(context);
        tv.setText("首页  Content");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);
        tv.setTextColor(context.getResources().getColor(R.color.normal_red));
        return tv;
    }

    @Override
    public void initData() {
        mTitle.setText(mContext.getResources().getString(R.string.tab_title));
        mMeun.setVisibility(View.GONE);
    }
}
