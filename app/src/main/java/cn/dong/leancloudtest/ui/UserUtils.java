package cn.dong.leancloudtest.ui;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.model.User;

/**
 * author DONG 2015/7/14.
 */
public class UserUtils {

    public static void setUserAvatar(Context context, ImageView avatarView, User user) {
        if (user != null && user.getAvatar() != null) {
            Picasso.with(context)
                    .load(user.getAvatarUrl())
                    .into(avatarView);
        } else {
            Picasso.with(context)
                    .load(R.drawable.ic_account_circle_black_18dp)
                    .into(avatarView);
        }
    }
}
