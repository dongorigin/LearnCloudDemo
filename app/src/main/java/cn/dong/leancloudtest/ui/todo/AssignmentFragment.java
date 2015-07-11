package cn.dong.leancloudtest.ui.todo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import cn.dong.leancloudtest.AVHelper;
import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.ui.common.BaseFragment;
import cn.dong.leancloudtest.util.L;

/**
 * @author dong on 15/7/11.
 */
public class AssignmentFragment extends BaseFragment {
    @InjectView(R.id.tabs)
    TabLayout mTabLayout;
    @InjectView(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_assignment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager();
    }

    private void setupViewPager() {
        MainPagerAdapter adapter = new MainPagerAdapter(getChildFragmentManager());
        adapter.addPage(new PlanFragment(), "Plan");
        adapter.addPage(new TodoFragment(), "Todo");
        adapter.addPage(new FinishFragment(), "Finish");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void showCreateDialog() {
        final EditText inputView = new EditText(getActivity());
        new AlertDialog.Builder(getActivity())
                .setTitle("添加Todo")
                .setView(inputView)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createTodo(inputView.getText().toString());
                    }
                })
                .show();
    }

    private void createTodo(String title) {
        AVHelper.createTodo(title, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Snackbar.make(getView(), "添加成功", Snackbar.LENGTH_SHORT).show();
                } else {
                    L.d(TAG, e.getMessage());
                    Snackbar.make(getView(), "添加失败", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
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
