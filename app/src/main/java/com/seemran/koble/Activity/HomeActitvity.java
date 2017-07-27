package com.seemran.koble.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.seemran.koble.ChatPubnub.ChatActivity;
import com.seemran.koble.PhonebookContacts.ContactsFragment;
import com.seemran.koble.Fragments.CallFragment;
import com.seemran.koble.Fragments.Groupfragment;
import com.seemran.koble.Fragments.RecentFragment;
import com.seemran.koble.Fragments.SettingsFragment;
import com.seemran.koble.R;

import java.util.ArrayList;
import java.util.List;





public class HomeActitvity extends AppCompatActivity  {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    final int RequestPermissionCode = 1;
//    <---floatbutton--->
    private Boolean isFabOpen = false;
    public  FloatingActionButton fab,fab1,fab2;
    public Animation fab_open,fab_close,rotate_forward,rotate_backward;
//    <--newchat-->
    public Layout layout_root;
    final Context context = this;




    private int[] tabIcons = {
            R.drawable.ic_recent,
            R.drawable.ic_call,
            R.drawable.ic_menu,
            R.drawable.ic_profile,
            R.drawable.ic_settings
    }; // This is an array of images

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EnableRuntimePermission();
        viewPager = (ViewPager) findViewById(R.id.viewpager); // we get the viewpager from activity
        setupViewPager(viewPager); // set this activity to support viewpager

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager); // Connect viewpage to tablayout
        setupTabIcons(); // function





        //<----FLOATING ACTION BUTTON---->
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActitvity.this, ChatActivity.class);
                startActivity(i);
            }
        });
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }
//    <--oncreatebundleclose-->



    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager()); // This is hosekeeping android stuff
        adapter.addFrag(new RecentFragment());
        adapter.addFrag(new CallFragment());
        adapter.addFrag(new ContactsFragment());
        adapter.addFrag(new Groupfragment());
        adapter.addFrag(new SettingsFragment());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>(); // This adapter takes two arrays
        //private final List<String> mFragmentTitleList = new ArrayList<>(); // This is the array I was talking about

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        } // This asks for manager as an argument

        @Override
        public Fragment getItem(int position) { // This function is used by the adapter to get the position of the fragment
            return mFragmentList.get(position); // Now suppose you on tab 1. And then you swipe to tab two.
        } // This function will run that time. Saying tab 2. And then the adapter will fetch frg 2 and display it.

        @Override
        public int getCount() { // This function is to get the total count of the adapter lists.
            return mFragmentList.size(); // When the tab 5 is displayed. And you swipe. But there is no frag 6.
        } // This function will tell that. and will stop displaying more.


        public void addFrag(Fragment fragment) { // This function is adding data to the two lists
            mFragmentList.add(fragment); // Fragment added to fraglist
            // mFragmentTitleList.add(title); // frag title added to fragtitle list
        }


    }
    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                HomeActitvity.this,
                Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(HomeActitvity.this, new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
        }
    }



    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("floatbutton", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("floatbutton","open");

        }
    }




}
