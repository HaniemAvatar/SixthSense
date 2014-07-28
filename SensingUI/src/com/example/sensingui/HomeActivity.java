package com.example.sensingui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class HomeActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter; //HomeActivity는 각 페이지별로 Section이 만들어진다.

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager; //각 페이지별로 상단에 페이지이름이 표시가 된다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); //기본 현태는 activity_home.xml에서 따온다.


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        // 각 Section별로 Fragment들을 생성해서 만든다.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);//activity_home.xml에서 View의 id가 pager
        mViewPager.setAdapter(mSectionsPagerAdapter); //ViewPager에 SectionPagerAdapter함수의 결과값을 넣음으로써 화면에 각 View를 뿌려준다.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Option버튼을 생성
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter { //SectionPagerAdapter의 구성은 아래와 같다.

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                default:
                case 0: // 1번째 화면
                    return new SensingConditionView();
                case 1: // 2번째 화면
                    return new SensingOperationView();
                case 2: // 3번째 화면
                    return new ParallaxBackgroundFragment();
                case 3: // 4번째 화면
                    return new ParallaxBackgroundListFragment();
                case 4: // 5번째 화면
                    return new OverParallaxBackgroundListFragment();
            }
        }

        @Override
        public int getCount() { //화면의 숫자는 5개
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: //1번째 화면의 제목
                    return getString(R.string.title_section1); //1번째 화면의 제목
                case 1: //2번째 화면의 제목
                    return getString(R.string.title_section2);
                case 2: //3번째 화면의 제목 
                    return getString(R.string.title_section3);
                case 3: //4번째 화면의 제목
                    return getString(R.string.title_section4);
                case 4: //5번째 화면의 제목
                    return getString(R.string.title_section5);
            }
            return null;
        }
    }
}
