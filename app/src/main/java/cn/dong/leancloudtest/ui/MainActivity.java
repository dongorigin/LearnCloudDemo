package cn.dong.leancloudtest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.ui.common.BaseActivity;

public class MainActivity extends BaseActivity {
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.nav_view)
    NavigationView mNavigationView;
    View mHeaderView;
    ImageView mHeaderAvatarView;
    TextView mHeaderUsernameView;
    @InjectView(R.id.tabs)
    TabLayout mTabLayout;
    @InjectView(R.id.viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.fab)
    FloatingActionButton mFAB;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDrawer();
        setupViewPager();
        setupFAB();
        updateDrawerHeader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_logout:
                AVUser.logOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, getToolbar(), 0, 0);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        mHeaderView = mNavigationView.inflateHeaderView(R.layout.activity_main_drawerheader);
        mHeaderUsernameView = (TextView) mHeaderView.findViewById(R.id.header_username);
        mHeaderAvatarView = (ImageView) mHeaderView.findViewById(R.id.header_avatar);
        mHeaderAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        break;
                    case R.id.nav_settings:
                        break;
                    default:
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setupViewPager() {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new HomeFragment(), "Home");
        adapter.addPage(new MessageFragment(), "Message");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupFAB() {
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PostCreateActivity.class));
            }
        });
    }

    private void snackTest() {
        Snackbar.make(mFAB, "I'm here", Snackbar.LENGTH_SHORT)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
    }

    private void updateDrawerHeader() {
        // avatar
        mHeaderUsernameView.setText(AVUser.getCurrentUser().getUsername());
    }

    static class MainPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addPage(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
