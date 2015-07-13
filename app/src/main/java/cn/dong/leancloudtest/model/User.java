package cn.dong.leancloudtest.model;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;

/**
 * @author dong on 15/7/11.
 */
public class User extends AVUser {

    public AVFile getAvatar() {
        return getAVFile("avatar");
    }

    public void setAvatar(AVFile file) {
        put("avatar", file);
    }

    public String getAvatarUrl() {
        return getAvatar() != null ? getAvatar().getUrl() : "";
    }

}
