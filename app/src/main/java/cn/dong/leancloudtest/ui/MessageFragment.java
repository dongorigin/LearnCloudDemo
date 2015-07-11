package cn.dong.leancloudtest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.InjectView;
import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.ui.common.BaseFragment;

/**
 * @author dong on 15/7/11.
 */
public class MessageFragment extends BaseFragment {
    @InjectView(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;
    @InjectView(R.id.recycler)
    RecyclerView mRecyclerView;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRefreshLayout();
        setupRecyclerView();
    }

    private void setupRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primaryDark);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //        mRecyclerView.setAdapter(mAdapter);
    }
}
