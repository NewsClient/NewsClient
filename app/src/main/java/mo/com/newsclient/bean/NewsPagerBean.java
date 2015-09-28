package mo.com.newsclient.bean;

import java.util.List;

/**
 * 作者：MoMxMo on 2015/9/27 11:24
 * 邮箱：xxxx@qq.com
 *
 * 新闻
 */


public class NewsPagerBean {

    /*新闻数据*/
    public Data data;
    public int retcode;

    public static class Data {
        public String countcommenturl;
        public String more;         //加载更多
        public String title;          //新闻标题
        public List<News> news;     //新闻列表
        public List<Topic> topic; // 新闻轮播图
        public List<Topnews> topnews;    //新闻

        public static class News {
            public boolean comment;
            public String commentlist;
            public String commenturl;
            public int id;
            public String listimage;
            public String pubdate;
            public String title;
            public String type;
            public String url;
        }

        public static class Topic {
            public String description;
            public int id;
            public String listimage;
            public int sort;
            public String title;
            public String url;
        }

        public static class Topnews {
            public boolean comment;
            public String commentlist;
            public String commenturl;
            public int id;
            public String pubdate;
            public String title;
            public String topimage;
            public String type;
            public String url;

        }
    }
}
