package mo.com.newsclient.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 作者：MoMxMo on 2015/9/22 19:14
 * 邮箱：xxxx@qq.com
 * <p/>
 * 测量工具类( px 和 dp之间的换算工具)
 */
public class DimenUtils {
    /**
     * dp  转 px
     * @param context
     * @param dp
     * @return
     */
    public static int pdTopx(Context context, int dp) {
        /* 计算公式
         1px = 1dp * (dpi / 160)*/
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;
        int px = dp * (dpi / 160);
        return px;
    }
    /**px  转 dp
     * @param context
     * @param px
     * @return
     */
    public static int pxTopd(Context context, int px) {
        /*计算公式*/
        /*1dp = 1 px * 160 / dpi*/
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;
        int dp = px * 160 / dpi;
        return dp;
    }
}
