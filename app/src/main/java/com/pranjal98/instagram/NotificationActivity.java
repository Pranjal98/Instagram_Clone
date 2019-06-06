package com.pranjal98.instagram;

import android.content.Intent;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class NotificationActivity extends AppCompatActivity {

    public void homeActivity(View view){

        Intent homeActivity = new Intent(getApplicationContext(), NewsFeed.class);
        startActivity(homeActivity);
        finish();
    }

    public void searchActivity(View view){

        Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(searchActivity);
        finish();
    }

    public void uploadActivity(View view){

        Intent uploadActivity = new Intent(getApplicationContext(), AddActivity.class);
        startActivity(uploadActivity);
        finish();
    }

    public void profileActivity(View view){

        Intent profileActivity = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(profileActivity);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ImageView prof = findViewById(R.id.notifyNotify);
        prof.setImageResource(R.drawable.kaladil);


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Following"));
        tabLayout.addTab(tabLayout.newTab().setText("You"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
