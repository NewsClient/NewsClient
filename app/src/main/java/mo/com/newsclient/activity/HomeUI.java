package mo.com.newsclient.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import mo.com.newsclient.R;
import mo.com.newsclient.fragment.ContentFrament;

public class HomeUI extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Load Content Layout*/
        setContentView(R.layout.home_content);

        //Load Meun Layout TODO

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
