package cn.dong.leancloudtest.ui;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;

import butterknife.InjectView;
import cn.dong.leancloudtest.AVHelper;
import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.model.event.PostCreateEvent;
import cn.dong.leancloudtest.ui.common.BaseActivity;
import cn.dong.leancloudtest.util.ToastUtils;
import de.greenrobot.event.EventBus;

/**
 * @author dong on 15/7/11.
 */
public class PostCreateActivity extends BaseActivity {
    @InjectView(R.id.input)
    EditText mInputView;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_post_create;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_create_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                done();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void done() {
        String input = mInputView.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            ToastUtils.shortT(R.string.input_empty);
            return;
        }
        AVHelper.createPost(input, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    ToastUtils.shortT(R.string.release_success);
                    EventBus.getDefault().post(new PostCreateEvent());
                    finish();
                } else {
                    ToastUtils.shortT(R.string.release_failure);
                }
            }
        });
    }

}
