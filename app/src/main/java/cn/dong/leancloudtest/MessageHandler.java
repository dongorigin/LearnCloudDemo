package cn.dong.leancloudtest;

import android.content.Context;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;

import cn.dong.leancloudtest.util.L;
import cn.dong.leancloudtest.util.ToastUtils;

/**
 * author DONG 2015/7/24.
 */
public class MessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {
    private static final String TAG = MessageHandler.class.getSimpleName();
    private Context context;
    private static AVIMTypedMessageHandler<AVIMTypedMessage> activityMessageHandler;

    public MessageHandler(Context context) {
        this.context = context;
    }

    public static AVIMTypedMessageHandler<AVIMTypedMessage> getActivityMessageHandler() {
        return activityMessageHandler;
    }

    public static void setActivityMessageHandler(AVIMTypedMessageHandler<AVIMTypedMessage> activityMessageHandler) {
        MessageHandler.activityMessageHandler = activityMessageHandler;
    }

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        if (client.getClientId().equals(AVHelper.getIMClientId())) {
            if (activityMessageHandler != null) {
                // 当前正在聊天页面
                activityMessageHandler.onMessage(message, conversation, client);
            } else {
                // 不在聊天页面
                ToastUtils.shortT(context, "新消息 " + message.getFrom() + " : " + message.getContent());
            }
        } else {
            client.close(null);
        }
    }

    @Override
    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        L.d(TAG, "消息已到达对方" + message.getContent());
    }
}
