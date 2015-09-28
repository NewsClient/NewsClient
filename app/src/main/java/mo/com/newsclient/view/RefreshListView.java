package mo.com.newsclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mo.com.newsclient.R;

/**
 * 作者：MoMxMo on 2015/9/27 20:14
 * 邮箱：xxxx@qq.com
 */


public class RefreshListView extends ListView {

    private static final String TAG = "RefreshListView";
    private RelativeLayout mRefreshLayout;
    private int mRefreshHeaderHeight;
    private float mDownY;
    private float mDownX;

    private static final int STATE_PULL_REFRESH = 0;                    // 下拉刷新状态
    private static final int STATE_RELEASE_REFRESH = 1;                    // 松开刷新状态
    private static final int STATE_REFRESHING = 2;                    // 正在刷新状态

    private int mCurrentState = STATE_PULL_REFRESH;
    private ImageView mIvArrow;
    private ProgressBar mRefreshProgress;
    private TextView mTvRefreshState;
    private TextView mTvRefreshDate;

    public RefreshListView(Context context) {
        super(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //添加刷新头
        initRefreshHeader();
    }

    private void initRefreshHeader() {
        mRefreshLayout = (RelativeLayout) View.inflate(getContext(), R.layout.refresh_header, null);
        addHeaderView(mRefreshLayout);
        mIvArrow = (ImageView) mRefreshLayout.findViewById(R.id.refresh_header_iv_arrow);
        mRefreshProgress = (ProgressBar) mRefreshLayout.findViewById(R.id.refresh_header_progress);
        mTvRefreshDate = (TextView) mRefreshLayout.findViewById(R.id.refresh_header_tv_date);
        mTvRefreshState = (TextView) mRefreshLayout.findViewById(R.id.refresh_header_tv_state);

        //隐藏刷新头，设置paddingTop的值为负数
        mRefreshLayout.measure(0, 0);
        mRefreshHeaderHeight = mRefreshLayout.getMeasuredHeight();

        Log.i(TAG, "mRefreshHeaderHeight: "+mRefreshHeaderHeight);
        int paddingTop = -mRefreshHeaderHeight;
        mRefreshLayout.setPadding(0, -paddingTop, 0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
/*        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveX = ev.getX();
                float mMoveY = ev.getY();
                if (mCurrentState==STATE_REFRESHING) {
                    *//*正在刷新*//*
                    break;
                }

                //有下网上移动 mDownY < mMoveY
                //如果mDownX 大于  并且第一个显示的时候，
                if (mDownY < mMoveY && getFirstVisiblePosition() == 0) {
                    float dif = mMoveY - mDownY;
                    int paddingTop = (int) (dif - mRefreshHeaderHeight + 0.5f);
                    if (paddingTop < 0 && mCurrentState != STATE_PULL_REFRESH) {
                        Log.i(TAG, "onTouchEvent 下拉刷新");
                        //状态改变松开刷新
                        mCurrentState = STATE_PULL_REFRESH;
                        //更新UI
                        refreshUI();

                    } else if (paddingTop > 0 && mCurrentState != STATE_RELEASE_REFRESH) {
                        //状态改变松开刷新
                        mCurrentState = STATE_RELEASE_REFRESH;
                        Log.i(TAG, "onTouchEvent 松开刷新");

                        //更新UI
                        refreshUI();
                    }
//                    mRefreshLayout.setPadding(0, paddingTop, 0, 0);
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:

                //松开
                if (mCurrentState == STATE_PULL_REFRESH) {
                    //如果当前状态是上拉刷新，松开之后，回到最初的位置
                    //隐藏刷新进度
                    mRefreshProgress.setVisibility(INVISIBLE);

                    mRefreshLayout.setPadding(0,-mRefreshHeaderHeight,0,0);

                }else if (mCurrentState== STATE_RELEASE_REFRESH) {
                    //如果当前状态是松开刷新，松开之后，状态改变为，正在刷新
                    mCurrentState = STATE_REFRESHING;
                    //更新UI
                    refreshUI();
                }
                break;
            default:
                break;
        }*/
        return super.onTouchEvent(ev);
    }

    //UI刷新
    private void refreshUI() {

        switch (mCurrentState) {
            case STATE_PULL_REFRESH:
                //下拉刷新
                //状态文本
                mTvRefreshState.setText("下拉刷新");

                //箭头转向
                /*隐藏进度*/
                mRefreshProgress.setVisibility(INVISIBLE);
                mIvArrow.setVisibility(VISIBLE);

                RotateAnimation upToDownAnimation = new RotateAnimation(-180, 0,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                );
                upToDownAnimation.setDuration(300);
                upToDownAnimation.setFillAfter(true);
                mIvArrow.startAnimation(upToDownAnimation);

                break;
            case STATE_RELEASE_REFRESH:
                //松开

                //文本改变
                mTvRefreshState.setText("松开刷新");
                //箭头转向
                //隐藏进度
                mRefreshProgress.setVisibility(INVISIBLE);
                mIvArrow.setVisibility(VISIBLE);

                RotateAnimation downToUpAnimation = new RotateAnimation(0, 180,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                );
                downToUpAnimation.setDuration(300);
                downToUpAnimation.setFillAfter(true);
                mIvArrow.startAnimation(downToUpAnimation);

                break;
            case STATE_REFRESHING:
                //正在数据
                //文本改变
                mTvRefreshState.setText("正在刷新");
                //箭头转向
                //隐藏箭头，显示进度
                mRefreshProgress.setVisibility(VISIBLE);
                mIvArrow.setVisibility(INVISIBLE);
                break;
            default:
                break;
        }



    }
}
