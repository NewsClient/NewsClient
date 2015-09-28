package mo.com.newsclient.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.List;

import mo.com.newsclient.R;
import mo.com.newsclient.activity.HomeUI;
import mo.com.newsclient.bean.NewscenterBean;

/**
 * 作者：MoMxMo on 2015/9/24 18:34
 * 邮箱：xxxx@qq.com
 */


public class MenuFragment extends BaseFrament {
    private List<NewscenterBean.NewsMenuBean> mMenuData;

    private ListView mListView;
    private int mCurrentMenuPosition = 0;
    private MeunAdapter mAdapter;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.menu, null);
        mListView = (ListView) view.findViewById(R.id.menu_listview);
        return view;
    }

    public void setData(List<NewscenterBean.NewsMenuBean> menuData) {
        this.mMenuData = menuData;
        mAdapter = new MeunAdapter();
        mListView.setAdapter(mAdapter);

        HomeUI homeUI = (HomeUI) mActivity;
        ContentFrament contentFragment = homeUI.getContentFragment();
        contentFragment.switchContent(mCurrentMenuPosition);

        /**
         * click item of ListView
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //如果是选中的  不做操作
                if (mCurrentMenuPosition == position) {
                    return;
                }

                //点击切换在内容中切换 内容
                HomeUI homeUI = (HomeUI) mActivity;
                ContentFrament contentFragment = homeUI.getContentFragment();
                contentFragment.switchContent(position);

                //关闭菜单
                SlidingMenu slidingMenu = homeUI.getSlidingMenu();
                slidingMenu.toggle();


                mCurrentMenuPosition = position;
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public class MeunAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mMenuData != null) {
                return mMenuData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mMenuData != null) {
                mMenuData.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            ViewHolder mHolder = null;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.menu_item, null);
                convertView.setTag(mHolder);
                mHolder.mTextView = (TextView) convertView.findViewById(R.id.item_menu_tv_title);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            NewscenterBean.NewsMenuBean newsMenuBean = mMenuData.get(position);
            String meunTitle = newsMenuBean.title;
            mHolder.mTextView.setText(meunTitle);

            if (mCurrentMenuPosition == position) {
                mHolder.mTextView.setEnabled(true);
            } else {
                mHolder.mTextView.setEnabled(false);

            }


            return convertView;
        }
    }

    private class ViewHolder {
        TextView mTextView;
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
