package mo.com.newsclient.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import mo.com.newsclient.R;
import mo.com.newsclient.fragment.ContentFrament;
import mo.com.newsclient.fragment.MenuFragment;
import mo.com.newsclient.utils.DimenUtils;

/**
 * This is using FrameWork of teacher provided
 */
public class HomeUI extends SlidingFragmentActivity {

    private static final String TAG_MENU = "menu";
    private static final String TAG_CONTENT = "content";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Load Content Layout*/
        setContentView(R.layout.home_content);

        //Load Meun Layout
        setBehindContentView(R.layout.home_menu);

        //obtain meun layout
        SlidingMenu slidingMenu = getSlidingMenu();

        //setting haul mode
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //setting menu width
        slidingMenu.setBehindWidth(DimenUtils.pdTopx(this, 180));

        //setting scroll scale
        slidingMenu.setBehindScrollScale(0.5f);

        //setting shadow
        slidingMenu.setShadowWidth(DimenUtils.pdTopx(this,20));
        slidingMenu.setShadowDrawable(R.drawable.shadow);

        //load left meun and content of fragment
        initFragement();
    }

    private void initFragement() {

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        // Meun part and Set the tag
        transaction.add(R.id.menu_container, new MenuFragment(),TAG_MENU);

        /*content part and set the tag*/
        transaction.add(R.id.home_container, new ContentFrament(),TAG_CONTENT);
        transaction.commit();
    }

    /**
     * acquire menu Fragment
     * @return
     */
    public MenuFragment getMenuFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG_MENU);
        return (MenuFragment)fragment;
    }
    /**
     * acquire Content  Fragment
     * @return
     */
    public ContentFrament getContentFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG_CONTENT);
        return (ContentFrament)fragment;
    }




}
