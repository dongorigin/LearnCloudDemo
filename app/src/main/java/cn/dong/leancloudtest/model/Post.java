package cn.dong.leancloudtest.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * @author dong on 15/7/10.
 */
@AVClassName("Post")
public class Post extends AVObject {
    public static final String KEY_USER = "user";

    public void setUser(User user) {
        put(KEY_USER, user);
    }

    public User getUser() {
        return getAVUser(KEY_USER, User.class);
    }

    public void setContent(String content) {
        put("content", content);
    }

    public String getContent() {
        return getString("content");
    }

}
