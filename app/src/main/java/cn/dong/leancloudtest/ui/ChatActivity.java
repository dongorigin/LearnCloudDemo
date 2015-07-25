package cn.dong.leancloudtest.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.List;

import butterknife.InjectView;
import cn.dong.leancloudtest.AVHelper;
import cn.dong.leancloudtest.MessageHandler;
import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.ui.common.BaseActivity;

/**
 * @author dong on 15/7/22.
 */
public class ChatActivity extends BaseActivity {
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_CONVERSATION_ID = "conversation_id";

    @InjectView(R.id.recycler)
    RecyclerView mRecyclerView;
    @InjectView(R.id.input)
    EditText mInputView;
    @InjectView(R.id.send)
    ImageView mSendView;

    private AVIMConversation mConversation;
    private ChatHandler mChatHandler;
    private ChatAdapter mAdapter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_chat;
    }

    public static void startActivity(Context context, String title, String conversationId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_CONVERSATION_ID, conversationId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String conversationId = getIntent().getStringExtra(EXTRA_CONVERSATION_ID);
        mConversation = AVHelper.getIMClient().getConversation(conversationId);
        if (mConversation == null) {
            finish();
        }
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        setTitle(title);

        mAdapter = new ChatAdapter(mContext);
        mChatHandler = new ChatHandler();
        MessageHandler.setActivityMessageHandler(mChatHandler);

        setupView();
        loadMessage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageHandler.setActivityMessageHandler(null);
    }

    private void setupView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);

        mSendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mInputView.getText().toString().trim();
                if (!TextUtils.isEmpty(input)) {
                    sendMessage(input);
                }
            }
        });
    }

    private void loadMessage() {
        mConversation.queryMessages(new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVException e) {
                if (AVHelper.filterException(e)) {
                    mAdapter.setData(list);
                    scrollToLast();
                }
            }
        });
    }

    private void loadOldMessage() {

    }

    private void sendMessage(String content) {
        final AVIMTextMessage message = new AVIMTextMessage();
        message.setText(content);
        mConversation.sendMessage(message, new AVIMConversationCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    mAdapter.addMessage(message);
                    mInputView.setText(null);
                    scrollToLast();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void scrollToLast() {
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
    }

    private class ChatHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

        @Override
        public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
            if (!(message instanceof AVIMTextMessage)) {
                return;
            }
            if (conversation.getConversationId().equals(mConversation.getConversationId())) {
                mAdapter.addMessage((AVIMTextMessage) message);
                scrollToLast();
            }
        }
    }
}
