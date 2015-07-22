package cn.dong.leancloudtest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;

import java.util.List;

import butterknife.InjectView;
import cn.dong.leancloudtest.AVHelper;
import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.model.Post;
import cn.dong.leancloudtest.model.event.PostCreateEvent;
import cn.dong.leancloudtest.ui.common.BaseFragment;
import de.greenrobot.event.EventBus;

/**
 * @author dong on 15/7/11.
 */
public class HomeFragment extends BaseFragment {
    @InjectView(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;
    @InjectView(R.id.recycler)
    RecyclerView mRecyclerView;

    private HomeAdapter mAdapter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRefreshLayout();
        setupRecyclerView();
        findData();
    }

    private void setupRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primaryDark);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                findData();
            }
        });
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new HomeAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void findData() {
        AVHelper.findPost(new FindCallback<Post>() {
            @Override
            public void done(List<Post> list, AVException e) {
                if (e == null) {
                    mAdapter.setData(list);
                    mRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    public void onEvent(PostCreateEvent event) {
        findData();
    }
}
