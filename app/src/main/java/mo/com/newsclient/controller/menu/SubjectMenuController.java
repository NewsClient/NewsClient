package mo.com.newsclient.controller.menu;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import mo.com.newsclient.R;
import mo.com.newsclient.controller.BaseController;

/**
 * 作者：MoMxMo on 2015/9/24 21:46
 * 邮箱：xxxx@qq.com
 */


public class SubjectMenuController extends BaseController {

    public SubjectMenuController(Context context) {
        super(context);
    }
    @Override
    protected View initView(Context context) {
        TextView tv = new TextView(mContext);
        tv.setText("专题 新闻");
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(R.color.normal_width);
        tv.setTextColor(mContext.getResources().getColor(R.color.normal_red));
        tv.setTextSize(25);
        return tv;
    }

}
