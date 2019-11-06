package com.android_twitter_show_timelines_demo.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android_twitter_show_timelines_demo.R;
import com.android_twitter_show_timelines_demo.adapter.TabViewPagerAdapter;
import com.android_twitter_show_timelines_demo.helper.MyPreferenceManager;
import com.android_twitter_show_timelines_demo.tabs.CollectionTimelineFragment;
import com.android_twitter_show_timelines_demo.tabs.SearchTimelineFragment;
import com.android_twitter_show_timelines_demo.tabs.UserTimelineFragment;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MyPreferenceManager myPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        myPreferenceManager = new MyPreferenceManager(getApplicationContext());
    }

    /**
     * init views including finding id of views and setting tab with fragments
     */
    private void initViews() {
        viewPager = findViewById(R.id.twitter_view_pager);
        setupViewPager();

        tabLayout = findViewById(R.id.twitter_tab);
        tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager
    }

    /**
     * set fragment to View pager
     */
    private void setupViewPager() {

        //get tab array from string.xml
        String[] tabArray = getResources().getStringArray(R.array.tab_items);

        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(UserTimelineFragment.newInstance(), tabArray[0]);
        adapter.addFrag(SearchTimelineFragment.newInstance(), tabArray[1]);
//        adapter.addFrag(CollectionTimelineFragment.newInstance(), tabArray[2]);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                TwitterCore.getInstance().getSessionManager().clearActiveSession();
                myPreferenceManager.clearSession();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                return true;
            case R.id.newtweet:
                TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                Intent intent = new ComposerActivity.Builder(this)
                        .session(twitterSession)//Set the TwitterSession of the User to Tweet
                        .text("This tweet is composed with Social Spam Detector App!!")//Text to prefill in composer
                        .hashtags("#SSD")//Hashtags to prefill in composer
                        .createIntent();//finally create intent
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }


    }
}
