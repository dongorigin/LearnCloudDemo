package cn.dong.leancloudtest.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * @author dong on 15/7/10.
 */
@AVClassName("Post")
public class Post extends AVObject {
    public void setUser(AVUser user) {
        put("user", user);
    }

    public AVUser getUser() {
        return getAVUser("user");
    }

    public void setContent(String content) {
        put("content", content);
    }

    public String getContent() {
        return getString("content");
    }

}
