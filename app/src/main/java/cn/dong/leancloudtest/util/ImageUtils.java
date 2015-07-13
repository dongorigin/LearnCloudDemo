package cn.dong.leancloudtest.util;

import android.app.Activity;
import android.content.Intent;

/**
 * @author dong on 15/7/11.
 */
public class ImageUtils {
    public static final int PICK_IMAGE_REQUEST = 1001;

    public static void startImagePick(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), PICK_IMAGE_REQUEST);
    }

}
