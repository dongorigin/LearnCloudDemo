package cn.dong.leancloudtest.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.dong.leancloudtest.AVHelper;
import cn.dong.leancloudtest.ConversationManager;
import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.model.User;
import cn.dong.leancloudtest.ui.common.BaseAdapter;
import cn.dong.leancloudtest.ui.common.BaseFragment;
import cn.dong.leancloudtest.util.L;

/**
 * @author dong on 15/7/11.
 */
public class MessageFragment extends BaseFragment {
    @InjectView(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;
    @InjectView(R.id.recycler)
    RecyclerView mRecyclerView;

    private MessageAdapter mAdapter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRefreshLayout();
        setupRecyclerView();
        setupIM();
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
        mAdapter = new MessageAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupIM() {
        AVIMClient imClient = AVHelper.getIMClient();
        imClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVException e) {
                if (AVHelper.filterException(e)) {
                    findData();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AVIMClient imClient = AVHelper.getIMClient();
        imClient.close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVException e) {
                if (AVHelper.filterException(e)) {
                    L.d(TAG, "退出连接");
                }
            }
        });
    }

    private void findData() {
        AVHelper.findUsers(new FindCallback<User>() {
            @Override
            public void done(List<User> list, AVException e) {
                if (e == null) {
                    mAdapter.setData(list);
                    mRefreshLayout.setRefreshing(false);
                } else {

                }
            }
        });
    }

    private static class MessageAdapter extends BaseAdapter<User, ViewHolder> {

        public MessageAdapter(Context context) {
            super(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_message_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final User user = getItem(position);
            holder.user = user;
            UserUtils.setUserAvatar(holder.itemView.getContext(), holder.avatarView, user);
            holder.usernameView.setText(user.getUsername());
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.avatar)
        ImageView avatarView;
        @InjectView(R.id.username)
        TextView usernameView;
        @InjectView(R.id.time)
        TextView timeView;
        @InjectView(R.id.content)
        TextView contentView;

        User user;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            if (user == null) {
                return;
            }
            ConversationManager.startConversationWithOther(user, new AVIMConversationCreatedCallback() {
                @Override
                public void done(AVIMConversation conversation, AVException e) {
                    if (conversation != null) {
                        ChatActivity.startActivity(v.getContext(), user.getUsername(), conversation.getConversationId());
                    }
                }
            });
        }
    }

}
