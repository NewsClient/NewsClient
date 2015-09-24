package mo.com.newsclient.bean;

import java.util.List;

/**
 * 作者：MoMxMo on 2015/9/23 22:09
 * 邮箱：xxxx@qq.com
 */


public class NewscenterBean {

    public List<NewsMenuBean> data;
    public List<Long> extend;
    public int retcode;

    public class NewsMenuBean {
        public List<NewsListBean> children;
        public long id;
        public String title;
        public int	type;
        public String url;
        public String url1;
        public String dayurl;
        public String excurl;
        public String weekurl;
    }
    public class NewsListBean {
        public long id;
        public String title;
        public int type;
        public String url;
    }

}
