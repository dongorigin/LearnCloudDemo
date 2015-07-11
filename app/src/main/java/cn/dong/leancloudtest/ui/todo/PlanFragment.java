package cn.dong.leancloudtest.ui.todo;

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
import cn.dong.leancloudtest.model.Todo;
import cn.dong.leancloudtest.ui.common.BaseFragment;

/**
 * @author dong on 15/7/11.
 */
public class PlanFragment extends BaseFragment {
    @InjectView(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;
    @InjectView(R.id.recycler)
    RecyclerView mRecyclerView;

    TodoAdapter mAdapter;

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
                updateTodos();
            }
        });
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new TodoAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void updateTodos() {
        AVHelper.fetchTodos(new FindCallback<Todo>() {
            @Override
            public void done(List<Todo> list, AVException e) {
                if (e == null) {
                    mAdapter.setTodos(list);
                    mRefreshLayout.setRefreshing(false);
                } else {

                }
            }
        });
    }
}
