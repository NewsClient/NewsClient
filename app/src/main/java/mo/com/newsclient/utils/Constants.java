package mo.com.newsclient.utils;

/**
 * @项目名: NewsClient
 * @包名: mo.com.newsclient.utils
 * @类名: ${TYPE_NAME}
 * @创建者: MoMxMo on 2015/9/23 21:39
 * @创建时间: 2015/9/23	21:39
 * @描述: TODO
 * @邮箱: xxxx@qq.com
 * @git版本: $$REV$$
 * @更新人: $$AUTHOR$$
 * @更新时间: $$Date$$
 * @更新描述: TODO
 */

public class Constants {

    /**
     * Base Server URL
     */
    public static String BASE_SERVER = "http://192.168.23.1:8080/zhbj";

    /**
     * News Center Url
     */
    public static String NEWS_CENTER_URL =BASE_SERVER+ "/categories.json";

    /**
     * Pix Url
     */
    public static String NEWS_CENTER_PIC_URL = BASE_SERVER+"/photos/photos_1.json";
}
