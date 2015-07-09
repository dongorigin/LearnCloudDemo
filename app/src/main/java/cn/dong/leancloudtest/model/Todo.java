package cn.dong.leancloudtest.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * author DONG 2015/7/5.
 */
@AVClassName("Todo")
public class Todo extends AVObject {

    public void setUser(AVUser user) {
        put("user", user);
    }

    public AVUser getUser() {
        return getAVUser("user");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getTitle() {
        return getString("title");
    }

}
