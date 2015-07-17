package cn.dong.leancloudtest.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

import butterknife.InjectView;
import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.ui.common.BaseActivity;
import cn.dong.leancloudtest.util.L;
import cn.dong.leancloudtest.util.ToastUtils;

/**
 * author DONG 2015/7/7.
 */
public class LoginActivity extends BaseActivity {
    @InjectView(R.id.username)
    EditText mUsernameView;
    @InjectView(R.id.password)
    EditText mPasswordView;
    @InjectView(R.id.button)
    Button mButton;
    @InjectView(R.id.hint)
    TextView mHintView;

    private boolean isLogin = true; // true登录 false注册

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AVAnalytics.trackAppOpened(getIntent());

        if (AVUser.getCurrentUser() != null) {
            launchMain();
        }

        setTitle(R.string.welcome);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameView.getText().toString().trim();
                String password = mPasswordView.getText().toString().trim();
                if (isLogin) {
                    if (checkLoginInput(username, password)) {
                        login(username, password);
                    }
                } else {
                    if (checkSignupInput(username, password)) {
                        signup(username, password);
                    }
                }
            }
        });

        mHintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = !isLogin;
                if (isLogin) {
                    mButton.setText(R.string.login);
                    mHintView.setText(R.string.login_hint);
                } else {
                    mButton.setText(R.string.signup);
                    mHintView.setText(R.string.signup_hint);
                }
            }
        });

        mHintView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mHintView.setText(R.string.login_hint);
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void launchMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private boolean checkLoginInput(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            ToastUtils.shortT(mContext, R.string.login_username_empty);
            return false;
        } else if (TextUtils.isEmpty(password)) {
            ToastUtils.shortT(mContext, R.string.login_password_empty);
            return false;
        } else {
            return true;
        }
    }

    private void login(String username, String password) {
        AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                if (user != null) {
                    launchMain();
                } else {
                    L.d(TAG, "code:%d message:%s", e.getCode(), e.getMessage());
                    ToastUtils.shortT(e.getMessage());
                    switch (e.getCode()) {
                        case AVException.USERNAME_PASSWORD_MISMATCH:

                            break;
                        case AVException.PASSWORD_MISSING:

                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private boolean checkSignupInput(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            ToastUtils.shortT(mContext, R.string.login_username_empty);
            return false;
        } else if (TextUtils.isEmpty(password)) {
            ToastUtils.shortT(mContext, R.string.login_password_empty);
            return false;
        } else {
            // todo 应用自定规则检查，比如密码不能少于6位
            return true;
        }
    }

    private void signup(String username, String password) {
        AVUser user = new AVUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    launchMain();
                } else {
                    L.d(TAG, "code:%d message:%s", e.getCode(), e.getMessage());
                    ToastUtils.shortT(e.getMessage());
                    switch (e.getCode()) {
                        case AVException.USERNAME_TAKEN:
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

}
