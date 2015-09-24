package mo.com.newsclient.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import mo.com.newsclient.R;
import mo.com.newsclient.fragment.ContentFrament;

/**
 * This is using FrameWork of teacher provided
 */
public class HomeUI extends SlidingActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Load Content Layout*/
        setContentView(R.layout.home_content);

        //Load Meun Layout TODO
        setBehindContentView(R.layout.home_menu);

        initFragement();
    }

    private void initFragement() {

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        // Meun part TODO
//        transaction.add(R.id.meun_ui, new ContentFrament());


        /*content part*/
        transaction.add(R.id.home_ui, new ContentFrament());
        transaction.commit();
    }


}
