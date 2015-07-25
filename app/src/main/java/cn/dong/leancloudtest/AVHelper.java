package cn.dong.leancloudtest;

import android.content.Context;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

import java.io.File;
import java.io.IOException;

import cn.dong.leancloudtest.model.Post;
import cn.dong.leancloudtest.model.Todo;
import cn.dong.leancloudtest.model.User;
import cn.dong.leancloudtest.ui.LoginActivity;
import cn.dong.leancloudtest.util.L;

/**
 * author DONG 2015/7/5.
 */
public class AVHelper {
    private static final String TAG = AVHelper.class.getSimpleName();
    public static final String APP_ID = "ezjopb5y6zsnsyowuk2ezxt3xy1h0nq48xid89wxy57do9m8";
    public static final String APP_KEY = "hdks3uhht00ys9gt4yr0kwnc2ang40rt9giow1oo21f25w7q";

    public static void init(Context context) {
        AVOSCloud.initialize(context, APP_ID, APP_KEY);
        AVOSCloud.setDebugLogEnabled(BuildConfig.DEBUG);
        // 启用崩溃错误报告
        AVAnalytics.enableCrashReport(context, true);
        // 注册子类
        AVObject.registerSubclass(Todo.class);
        AVObject.registerSubclass(Post.class);
        // 注册MessageHandler
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler(context));
    }

    public static void initPush(Context context) {
        PushService.setDefaultPushCallback(context, LoginActivity.class);
        PushService.subscribe(context, "public", LoginActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    User user = AVUser.getCurrentUser(User.class);
                    user.setInstallationId(installationId);
                    user.saveInBackground();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static String getIMClientId() {
        AVUser user = AVUser.getCurrentUser();
        return user != null ? user.getUsername() : "";
    }

    public static AVIMClient getIMClient() {
        return AVIMClient.getInstance(AVUser.getCurrentUser().getUsername());
    }

    public static boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            L.d(TAG, e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    public static void createTodo(String title, SaveCallback saveCallback) {
        Todo todo = new Todo();
        todo.setUser(AVUser.getCurrentUser());
        todo.setTitle(title);
        todo.setACL(new AVACL(AVUser.getCurrentUser()));
        todo.saveInBackground(saveCallback);
    }

    public static void deleteTodo(Todo todo, DeleteCallback deleteCallback) {
        todo.deleteInBackground(deleteCallback);
    }

    public static void updateTodo(String objectId, String title, SaveCallback saveCallback) {
        Todo todo = new Todo();
        todo.setObjectId(objectId);
        todo.setTitle(title);
        todo.saveInBackground(saveCallback);
    }

    public static void fetchTodos(FindCallback<Todo> findCallback) {
        AVQuery<Todo> query = AVQuery.getQuery(Todo.class);
        query.whereEqualTo("user", AVUser.getCurrentUser());
        query.orderByAscending(Constants.UPDATED_AT);
        query.limit(100);
        query.findInBackground(findCallback);
    }

    public static void updateUserAvatar(final File imageFile, final SaveCallback callback) {
        new Thread() {
            @Override
            public void run() {
                User user = AVUser.getCurrentUser(User.class);
                if (user == null) {
                    return;
                }
                String fileName = System.currentTimeMillis() + ".png";
                AVFile file;
                try {
                    file = AVFile.withFile(fileName, imageFile);
                    file.save();
                    user.setAvatar(file);
                    user.saveInBackground(callback);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AVException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void createPost(String Content, SaveCallback callback) {
        Post post = new Post();
        post.setUser(AVUser.getCurrentUser(User.class));
        post.setContent(Content);
        post.saveInBackground(callback);
    }

    public static void findPost(FindCallback<Post> callback) {
        AVQuery<Post> query = AVQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.orderByDescending(Constants.UPDATED_AT);
        query.limit(100);
        query.findInBackground(callback);
    }

    public static void findUsers(FindCallback<User> callback) {
        AVQuery<User> query = AVUser.getUserQuery(User.class);
        query.whereNotEqualTo(Constants.OBJECT_ID, AVUser.getCurrentUser().getObjectId());
        query.findInBackground(callback);
    }

}
