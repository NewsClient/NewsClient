package mo.com.newsclient.controller.menu;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

import mo.com.newsclient.R;
import mo.com.newsclient.bean.NewsPagerBean;
import mo.com.newsclient.bean.NewscenterBean;
import mo.com.newsclient.controller.BaseController;
import mo.com.newsclient.controller.tab.NewsCenterController;
import mo.com.newsclient.utils.Constants;

/**
 * 作者：MoMxMo on 2015/9/24 21:46
 * 邮箱：xxxx@qq.com
 */


public class PicMenuController extends BaseController implements NewsCenterController.OnListOrGridListener {

    private static final String TAG = "PicMenuController";
    private GridView mGVPic;
    private ListView mLVPic;
    private NewscenterBean.NewsMenuBean menuBean;
    private List<NewsPagerBean.Data.News> mNewsDatas;
    private BitmapUtils mBitmapUtils;
    private PicAdapter mAdapter;
    private boolean isDispalyList= true;

    public PicMenuController(Context context, NewscenterBean.NewsMenuBean menuBean) {
        super(context);
        this.menuBean = menuBean;
    }

    @Override
    protected View initView(Context context) {
        View view = View.inflate(context, R.layout.menu_pic, null);


        mBitmapUtils= new BitmapUtils(context);

        mLVPic = (ListView) view.findViewById(R.id.menu_pic_lv);
        mGVPic = (GridView) view.findViewById(R.id.menu_pic_gv);
        return view;
    }

    @Override
    public void initData() {
        Log.i(TAG, "initData ..................");
        //网络中加载数据
        HttpUtils utils = new HttpUtils();
        String url = Constants.NEWS_CENTER_PIC_URL;

        utils.send(HttpRequest.HttpMethod.GET, url, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                 /*成功加载数据*/
                String json = responseInfo.result;
                processJson(json);
                Log.i(TAG, "onSuccess ..................");
            }
            @Override
            public void onFailure(HttpException e, String s) {
                //加载数据失败
                Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onFailure ..................");
            }
        });

        mAdapter = new PicAdapter();
        /*加载数据*/
        mLVPic.setAdapter(mAdapter);
        mGVPic.setAdapter(mAdapter);

    }

    /**
     * 解析json数据
     *
     * @param json
     */
    private void processJson(String json) {
        Gson gson = new Gson();
        NewsPagerBean bean = gson.fromJson(json, NewsPagerBean.class);
        mNewsDatas = bean.data.news;

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(ImageView view) {
        if (isDispalyList) {
            //如果当前是listView显示，点击之后，listView隐藏，gridView显示,图标显示List
            mLVPic.setVisibility(View.GONE);
            mGVPic.setVisibility(View.VISIBLE);
            view.setImageResource(R.drawable.icon_pic_list_type);

        } else {
            //如果当前是GridView显示，点击之后，gridView隐藏，listView显示,图标显示grid
            mLVPic.setVisibility(View.VISIBLE);
            mGVPic.setVisibility(View.GONE);
            view.setImageResource(R.drawable.icon_pic_grid_type);
        }
        isDispalyList = !isDispalyList;
    }

    private class PicAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mNewsDatas != null) {
                return mNewsDatas.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mNewsDatas != null) {
                return mNewsDatas.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_pic, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.mPic = (ImageView) convertView.findViewById(R.id.item_pic_icon);
                holder.mTvTitle = (TextView) convertView.findViewById(R.id.item_pic_title);
                holder.mTvDate = (TextView) convertView.findViewById(R.id.item_pic_date);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            NewsPagerBean.Data.News news = mNewsDatas.get(position);
            mBitmapUtils.display(holder.mPic,news.listimage);
            holder.mTvTitle.setText(news.title);
            holder.mTvDate.setText(news.pubdate);
            return convertView;
        }
    }
    private class ViewHolder{
        ImageView mPic;
        TextView mTvTitle;
        TextView mTvDate;
    }

}
