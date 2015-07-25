package cn.dong.leancloudtest;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.Conversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;

import java.util.ArrayList;
import java.util.List;

import cn.dong.leancloudtest.model.User;

/**
 * author DONG 2015/7/25.
 */
public class ConversationManager {

    public static void startConversationWithOther(User user, final AVIMConversationCreatedCallback callback) {
        startConversationWithOther(user.getUsername(), callback);
    }

    public static void startConversationWithOther(String otherId, final AVIMConversationCreatedCallback callback) {
        final AVIMClient imClient = AVHelper.getIMClient();
        final List<String> members = new ArrayList<>();
        members.add(AVHelper.getIMClientId());
        members.add(otherId);
        AVIMConversationQuery query = imClient.getQuery();
        query.whereContainsAll(Conversation.COLUMN_MEMBERS, members);
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVException e) {
                if (e != null) {
                    callback.done(null, e);
                } else {
                    if (list == null || list.size() <= 0) {
                        // 对话不存在，创建
                        imClient.createConversation(members, null, callback);
                    } else {
                        // 对话已存在，返回
                        callback.done(list.get(0), null);
                    }
                }
            }
        });
    }
}
