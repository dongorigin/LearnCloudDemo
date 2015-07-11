package cn.dong.leancloudtest.ui.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cn.dong.leancloudtest.util.L;

/**
 * author DONG 2015/7/9.
 */
public abstract class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();
    protected boolean isStatistics = true; // 是否统计页面，默认开启。包含子Fragment的可手动关闭，防止重复统计
    private boolean isActivityCreated = false; // 页面控件是否已初始化
    private boolean isFirstVisible = false; // 是否第一次可见
    protected BaseActivity mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (BaseActivity) getActivity();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isActivityCreated) {
            if (isVisibleToUser) {
                if (!isFirstVisible) {
                    isFirstVisible = true;
                    onPageFirstVisible();
                }
                onPageStart();
            } else {
                onPageEnd();
            }
        }
    }

    protected abstract int getContentViewLayoutId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getContentViewLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isActivityCreated = true;
        if (getUserVisibleHint()) {
            if (!isFirstVisible) {
                isFirstVisible = true;
                onPageFirstVisible();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            onPageStart();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onPageEnd();
        }
    }

    /**
     * 当页面首次可见时调用。调用时页面控件已经完成初始化
     * 用于ViewPager下的页面懒加载，在一个生命周期内只会调用一次
     */
    public void onPageFirstVisible() {
        L.v(TAG, "onPageFirstVisible");
    }

    public void onPageStart() {
        L.v(TAG, "onPageStart");
        if (isStatistics) {
            // page start
        }
    }

    public void onPageEnd() {
        L.v(TAG, "onPageEnd");
        if (isStatistics) {
            // page end
        }
    }
}
