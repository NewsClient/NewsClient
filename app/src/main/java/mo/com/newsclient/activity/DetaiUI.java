package mo.com.newsclient.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import mo.com.newsclient.R;
import mo.com.newsclient.utils.PreferenceUtils;
import mo.com.newsclient.utils.ShareUtils;

public class DetaiUI extends AppCompatActivity {

    public static final String KEY_URL = "url";
    private static final String KEY_TEXT_SIZE = "text_size";
    private WebView mWebView;
    private ImageView mIVShare;
    private ImageView mIVTextSize;
    private ImageView mIVBack;
    String url = "http://192.168.23.1:8080/zhbj/10007/724D6A55496A11726628.html";
    private int mCurrentTextSize;
    private WebSettings mWebSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_detai);

        initView();

        initData();

        initEvent();

    }

    private void initEvent() {

        //点击返回
        mIVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置字体大小
        mIVTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewsTextSize();
            }
        });

        //点击分享
        mIVShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.showShare(DetaiUI.this);
            }
        });
    }

    /**
     * 设置字体大小
     */
    private void setNewsTextSize() {
        // 1.得到对话框的构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(DetaiUI.this);
        builder.setTitle("设置字体");
        final String[] items = {"超大字体", "大字体", "正常字体", "小字体", "超小字体"};
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentTextSize = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //保存用户设置的字体大小
                PreferenceUtils.putInt(DetaiUI.this, KEY_TEXT_SIZE, mCurrentTextSize);

                //更新新闻的字体大小
                changeTextSize();
            }
        });
        builder.show();

    }

    /**
     * 改变显示新闻文字的大小
     */
    private void changeTextSize() {
        switch (mCurrentTextSize) {
            case 0:
                mWebSetting.setTextSize(WebSettings.TextSize.LARGEST);
                break;
            case 1:
                mWebSetting.setTextSize(WebSettings.TextSize.LARGER);
                break;
            case 2:
                mWebSetting.setTextSize(WebSettings.TextSize.NORMAL);
                break;
            case 3:
                mWebSetting.setTextSize(WebSettings.TextSize.SMALLER);
                break;
            case 4:
                mWebSetting.setTextSize(WebSettings.TextSize.SMALLEST);
                break;
        }

    }

    private void initData() {
        mWebView.loadUrl(url);

        if (mWebSetting == null) {
            mWebSetting = mWebView.getSettings();
        }
         /*设置js可用*/
        mWebSetting.setJavaScriptEnabled(true);
         /*设置双击放大和缩小*/
        mWebSetting.setUseWideViewPort(true);

//        设置默认字体大小

        mCurrentTextSize=2;

        //设置字体大小，加载配置选中的字体
        mCurrentTextSize = PreferenceUtils.getInt(this, KEY_TEXT_SIZE);
        changeTextSize();

    }

    private void initView() {
        mIVBack = (ImageView) findViewById(R.id.detai_iv_back);
        mIVTextSize = (ImageView) findViewById(R.id.detai_iv_textsize);
        mIVShare = (ImageView) findViewById(R.id.detai_iv_share);
        mWebView = (WebView) findViewById(R.id.detai_wv);
    }

}
