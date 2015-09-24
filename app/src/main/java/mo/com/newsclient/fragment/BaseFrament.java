package mo.com.newsclient.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @项目名: NewsClient
 * @包名: mo.com.newsclient.activity
 * @类名: ${TYPE_NAME}
 * @创建者: MoMxMo on 2015/9/23 14:58
 * @创建时间: 2015/9/23	14:58
 * @描述:  自定义Fragment基类
 * @邮箱: xxxx@qq.com
 * @git版本: $$REV$$
 * @更新人: $$AUTHOR$$
 * @更新时间: $$Date$$
 * @更新描述: TODO
 */

public abstract class BaseFrament extends Fragment {
    protected Activity mActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*创建View*/
       return initView();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //加载数据
        initData();
    }
    protected abstract View initView() ;

    protected void initData() {
        //TODO
    }
}
