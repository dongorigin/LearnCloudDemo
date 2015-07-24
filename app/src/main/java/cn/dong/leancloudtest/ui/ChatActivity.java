package cn.dong.leancloudtest.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;

import butterknife.InjectView;
import cn.dong.leancloudtest.AVHelper;
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

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        setTitle(title);
        String conversationId = getIntent().getStringExtra(EXTRA_CONVERSATION_ID);
        mConversation = AVHelper.getIMClient().getConversation(conversationId);
        if (mConversation == null) {
            finish();
        }

        setupView();
    }

    private void setupView() {
        mSendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mInputView.getText().toString().trim();
                if (!TextUtils.isEmpty(input)) {
                    sendMessage(input);
                } else {

                }
            }
        });
    }

    private void sendMessage(String content) {
        AVIMMessage message = new AVIMMessage();
        message.setContent(content);
        mConversation.sendMessage(message, new AVIMConversationCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {

                } else {
                    e.printStackTrace();

                }
            }
        });
    }
}
