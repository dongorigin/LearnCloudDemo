package cn.dong.leancloudtest.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * @author dong on 15/7/10.
 */
@AVClassName("Post")
public class Post extends AVObject {

    public void setUser(User user) {
        put("user", user);
    }

    public User getUser() {
        return getAVUser("user", User.class);
    }

    public void setContent(String content) {
        put("content", content);
    }

    public String getContent() {
        return getString("content");
    }

}
