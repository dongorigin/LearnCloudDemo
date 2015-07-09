package cn.dong.leancloudtest;

import android.content.Context;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import cn.dong.leancloudtest.model.Todo;

/**
 * author DONG 2015/7/5.
 */
public class AVHelper {
    public static final String APP_ID = "ezjopb5y6zsnsyowuk2ezxt3xy1h0nq48xid89wxy57do9m8";
    public static final String APP_KEY = "hdks3uhht00ys9gt4yr0kwnc2ang40rt9giow1oo21f25w7q";

    public static void init(Context context) {
        AVOSCloud.initialize(context, APP_ID, APP_KEY);
        // 启用崩溃错误报告
        AVAnalytics.enableCrashReport(context, true);
        // 注册子类
        AVObject.registerSubclass(Todo.class);
    }

    public static void createTodo(String title, SaveCallback saveCallback) {
        final Todo todo = new Todo();
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
        query.orderByAscending("updatedAt");
        query.limit(1000);
        query.findInBackground(findCallback);
    }
}
