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
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.Conversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.dong.leancloudtest.AVHelper;
import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.ui.common.BaseAdapter;
import cn.dong.leancloudtest.ui.common.BaseFragment;

/**
 * author DONG 2015/7/24.
 */
public class ConversationFragment extends BaseFragment {
    @InjectView(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;
    @InjectView(R.id.recycler)
    RecyclerView mRecyclerView;

    private ConversationAdapter mAdapter;

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
                findMyConversation();
            }
        });
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ConversationAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void findMyConversation() {
        AVIMClient imClient = AVHelper.getIMClient();
        AVIMConversationQuery query = imClient.getQuery();
        query.whereEqualTo(Conversation.COLUMN_MEMBERS, imClient.getClientId());
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVException e) {
                if (AVHelper.filterException(e)) {
                    mAdapter.setData(list);
                    mRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private static class ConversationAdapter extends BaseAdapter<AVIMConversation, ViewHolder> {

        public ConversationAdapter(Context context) {
            super(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int itemType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_conversation_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AVIMConversation conversation = getItem(position);
            holder.conversation = conversation;
            holder.textView.setText(conversation.getConversationId());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.text)
        TextView textView;

        AVIMConversation conversation;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (conversation != null) {
                ChatActivity.startActivity(v.getContext(), "", conversation.getConversationId());
            }
        }
    }
}
