package cn.dong.leancloudtest.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.InjectView;
import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.ui.common.BaseActivity;

/**
 * @author dong on 15/7/22.
 */
public class ChatActivity extends BaseActivity {
    @InjectView(R.id.recycler)
    RecyclerView mRecyclerView;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
