package cn.dong.leancloudtest.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * author DONG 2015/7/9.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    protected boolean isStatistics = true; // 是否统计页面，默认开启。当包含Fragment时可以关闭默认统计，手动对Fragment统计
    protected BaseActivity mContext;

    protected abstract int getContentViewLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayoutId());
        ButterKnife.inject(this);
        mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (isStatistics) {
//            MobclickAgent.onPageStart(this.getClass().getName());
//        }
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (isStatistics) {
//            MobclickAgent.onPageEnd(this.getClass().getName());
//        }
//        MobclickAgent.onPause(this);
    }

}
