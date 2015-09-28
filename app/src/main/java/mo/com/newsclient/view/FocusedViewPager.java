package mo.com.newsclient.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者：MoMxMo on 2015/9/27 14:10
 * 邮箱：xxxx@qq.com
 */

public class FocusedViewPager extends ViewPager {

    private float mDownY;
    private float mDownX;

    public FocusedViewPager(Context context) {
        super(context);
    }

    public FocusedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*计算拦截事件*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {   /**
     * 如果是第一个页面，需要父容器拦击
     * 如果是最后一个页面，需要父容器拦截
     *
     * 其他情况， 不需要拦截
     */

        int count = getAdapter().getCount();
        int currentItem = getCurrentItem();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                /*请求父容器不要拦截*/
                /**
                 * 参数：true  请求父容器不要拦截
                 * false   请求父容器拦截
                 */
                getParent().requestDisallowInterceptTouchEvent(true);

                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mMovwX = ev.getX();
                float mMovwY = ev.getY();

                /*水平方向的滑动大于垂直方向的滑动*/
                if (Math.abs(mMovwX - mDownX) > Math.abs(mMovwY - mDownY)) {
                    if (currentItem == 0 && mDownX < mMovwX) {
                    /*如果是第一个页面，需要父容器拦击*/
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (currentItem == count - 1 && mDownX > mMovwX) {
                        //如果是最后一个页面，需要父容器拦截
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        //其他情况,不要拦截
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } else {
                    /*让父容器滑动*/
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}
