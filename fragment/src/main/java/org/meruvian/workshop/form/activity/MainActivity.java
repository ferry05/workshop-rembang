package org.meruvian.workshop.form.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;

import org.meruvian.workshop.form.R;

import org.meruvian.workshop.form.fragment.DetailNewsFragment;
import org.meruvian.workshop.form.fragment.NewsFragment;

/**
 * Created by merv on 6/4/15.
 */
public class MainActivity extends ActionBarActivity{
    private FrameLayout container, containerInner;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (FrameLayout)findViewById(R.id.container);
        containerInner = (FrameLayout)findViewById(R.id.container_inner);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        NewsFragment newsFragment = new NewsFragment();

        Bundle bundle = new Bundle();
        if(containerInner != null){
            bundle.putString("screen", "large");
            newsFragment.setArguments(bundle);

            transaction.replace(R.id.container, newsFragment);
            transaction.replace(R.id.container_inner, new DetailNewsFragment());
        }else{
            bundle.putString("screen","normal");
            newsFragment.setArguments(bundle);

            transaction.replace(R.id.container, newsFragment);
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed(){
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }else{
            finish();
        }
    }
}
