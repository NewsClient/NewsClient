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
 * @创建者: MoMxMo on 2015/9/23 21:07
 * @创建时间: 2015/9/23	21:07
 * @描述: government information about it
 * @邮箱: xxxx@qq.com
 * @git版本: $$REV$$
 * @更新人: $$AUTHOR$$
 * @更新时间: $$Date$$
 * @更新描述: TODO
 */

public class GovTabController  extends TabController{
    public GovTabController(Context context) {
        super(context);

    }

    @Override
    protected View initContentView(Context context) {
        TextView tv = new TextView(context);
        tv.setText("政务内容 Content");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(30);
        tv.setTextColor(context.getResources().getColor(R.color.normal_red));
        return tv;
    }

    @Override
    public void initData() {
        mTitle.setText(mContext.getResources().getString(R.string.tab_gov_tab));
        mMeun.setVisibility(View.VISIBLE);
    }
}
