package mo.com.newsclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.viewpagerindicator.TabPageIndicator;

/**
 * 作者：MoMxMo on 2015/9/27 14:33
 * 邮箱：xxxx@qq.com
 */


public class FocusedTabPageIndicator extends TabPageIndicator {
    public FocusedTabPageIndicator(Context context) {
        super(context);
    }

    public FocusedTabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 请求父容器不要拦截
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        //请求父容器不拦截
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
