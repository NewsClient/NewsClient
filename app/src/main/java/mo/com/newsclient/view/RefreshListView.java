package mo.com.newsclient.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import mo.com.newsclient.R;

/**
 * 作者：MoMxMo on 2015/9/27 20:14
 * 邮箱：xxxx@qq.com
 */


public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = "RefreshListView";
    private RelativeLayout mRefreshLayout;
    private int mRefreshHeaderHeight;
    private float mDownY = -1;
    private float mDownX;

    private static final int STATE_PULL_REFRESH = 0;                    // 下拉刷新状态
    private static final int STATE_RELEASE_REFRESH = 1;                    // 松开刷新状态
    private static final int STATE_REFRESHING = 2;                    // 正在刷新状态

    private int mCurrentState = STATE_PULL_REFRESH;
    private ImageView mIvArrow;
    private ProgressBar mRefreshProgress;
    private TextView mTvRefreshState;
    private TextView mTvRefreshDate;
    private OnRefreshLitener mListener;
    private RelativeLayout mLoadMoreLayout;

    boolean isLoadMore = false; //是否正在加载更多
    private int mLoadMoreLayoutHeight;
    private View mFirstView;
    private int space = -1;



    public RefreshListView(Context context) {
        super(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //添加刷新头
        initRefreshHeader();

        //添加加载更多
        initFooterView();
    }

    private void initFooterView() {
        mLoadMoreLayout = (RelativeLayout) View.inflate(getContext(), R.layout.refresh_footer, null);
        this.addFooterView(mLoadMoreLayout);

        //隐藏
        mLoadMoreLayout.measure(0, 0);
        mLoadMoreLayoutHeight = mLoadMoreLayout.getMeasuredHeight();

        int paddingTop = -mLoadMoreLayoutHeight;
        mLoadMoreLayout.setPadding(0, paddingTop, 0, 0);

        //监听listView的滑动
        this.setOnScrollListener(this);

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
        Log.i(TAG, "mRefreshHeaderHeight: " + mRefreshHeaderHeight);
        int paddingTop = -mRefreshHeaderHeight;
        mRefreshLayout.setPadding(0, paddingTop, 0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveX = ev.getX();
                float mMoveY = ev.getY();
                if (mCurrentState == STATE_REFRESHING) {
                    // 正在刷新,不能进行下面的操作
                    break;
                }
                if (mDownY == -1) {
                    mDownY = mMoveY;
                }
                 boolean canDrag = true;
                if (mFirstView != null) {
                    //获取list左上角的坐标
                    int[] listCor = new int[2];
                    this.getLocationOnScreen(listCor);

                    //获取mFirstView左上角的坐标
                    int[] firstCor = new int[2];
                    mFirstView.getLocationOnScreen(firstCor);

                    Log.i(TAG, "list左上角的坐标 " + listCor[0] + ":" + listCor[1]);
                    Log.i(TAG, "mFirstView左上角的坐标 " + firstCor[0] + ":" + firstCor[1]);


                    if (listCor[1] > firstCor[1] - getDividerHeight()) {
                      /*第一个view没有完全漏出来*/
                        canDrag = false;
                        if (space==-1) {
                            /*获取滑动偏移量*/
                            space = listCor[1] - (firstCor[1] - getDividerHeight());
                        }

                    } else {
                        /*第一个view完全漏出来*/
                        canDrag = true;
                    }
                }

                //有下网上移动 mDownY < mMoveY
                //如果mDownX 大于  并且第一个显示的时候，
                if (mDownY < mMoveY && getFirstVisiblePosition() == 0 && canDrag) {
                    float dif = mMoveY - mDownY;
                    int paddingTop = (int) (dif-space - mRefreshHeaderHeight + 0.5f);
//                    Log.i(TAG, "dif ："+dif);
                    if (paddingTop < 0 && mCurrentState != STATE_PULL_REFRESH) {
//                        Log.i(TAG, "onTouchEvent 下拉刷新");
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
                    mRefreshLayout.setPadding(0, paddingTop, 0, 0);
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                mDownY = -1;
                space = -1;

                //松开
                if (mCurrentState == STATE_PULL_REFRESH) {
                    //如果当前状态是上拉刷新，松开之后，回到最初的位置
                    //隐藏刷新进度
                    mRefreshProgress.setVisibility(INVISIBLE);

                    //设置回到原来位置的滑动速度，提供用户体验，避免滑动过快
                    /*mRefreshLayout.setPadding(0,-mRefreshHeaderHeight,0,0);*/

                    int start = mRefreshLayout.getPaddingTop(); //获取当前paddingTop的距离
                    int end = -mRefreshHeaderHeight;              //最后滑动到的距离
                    headerAnimation(start, end);

                } else if (mCurrentState == STATE_RELEASE_REFRESH) {
                    //如果当前状态是松开刷新，松开之后，状态改变为，正在刷新
                    mCurrentState = STATE_REFRESHING;
                    //隐藏箭头
                    mIvArrow.setVisibility(GONE);

                    //更新UI
                    /*mRefreshLayout.setPadding(0,0,0,0);*/
                    int start = mRefreshLayout.getPaddingTop(); //获取当前paddingTop的距离
                    int end = 0;              //最后滑动到的距离
                    headerAnimation(start, end);
                    refreshUI();

                    //通知调用者刷新数据
                    if (mListener != null) {
                        mListener.onRefresh();
                    }

                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void addHeaderView(View v) {
        if (mFirstView == null && v != mRefreshLayout) {
            mFirstView = v;
        }
        super.addHeaderView(v);
    }

    private void headerAnimation(int start, int end) {

        int duration = Math.abs(end - start) * 10;  //一个像素值增加10毫秒
        if (duration > 600) {
            duration = 600;
        }
        ValueAnimator animation = ValueAnimator.ofInt(start, end);
        animation.setDuration(duration);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                //实时更新UI
                mRefreshLayout.setPadding(0, value, 0, 0);
            }
        });
        /*加速度加速器*/
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
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

                /*清楚动画*/
                mIvArrow.clearAnimation();
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

    //完成刷新
    public void setSuccessRefresh() {

        //隐藏刷新头
        //设置回到原来位置的滑动速度，提供用户体验，避免滑动过快
          /*mRefreshLayout.setPadding(0,-mRefreshHeaderHeight,0,0);*/

        int start = mRefreshLayout.getPaddingTop(); //获取当前paddingTop的距离
        int end = -mRefreshHeaderHeight;              //最后滑动到的距离
        headerAnimation(start, end);

        /*改变状态*/
        mCurrentState = STATE_PULL_REFRESH;
        refreshUI();

        /*设置刷新时间*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        mTvRefreshDate.setText(format);
    }

    //完成加载更多
    public void setSuccessLoadMore() {

        Log.i(TAG, "加载更多完成。。。。。。。。。。。 ");

        //隐藏footer
        int paddingTop = -mLoadMoreLayoutHeight;
        mLoadMoreLayout.setPadding(0, paddingTop, 0, 0);

    }

    public void setOnRefreshLitener(OnRefreshLitener listener) {
        mListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        //当闲置的时候，监听
        //获取最后一个位置
        int lastVisiblePosition = getLastVisiblePosition();

        int index = getAdapter().getCount() - 1;

        if (scrollState == SCROLL_STATE_IDLE && index == lastVisiblePosition) {

            /*添加标记*/

            if (!isLoadMore) {
                //显示footer
                mLoadMoreLayout.setPadding(0, 0, 0, 0);

                this.setSelection(getAdapter().getCount());

                //加载更多数据
                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }

        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //刷新监听器
    public interface OnRefreshLitener {
        void onRefresh();

        void onLoadMore();
    }
}
