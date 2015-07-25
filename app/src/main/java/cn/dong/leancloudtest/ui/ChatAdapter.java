package cn.dong.leancloudtest.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.dong.leancloudtest.AVHelper;
import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.ui.common.BaseAdapter;

/**
 * author DONG 2015/7/24.
 */
public class ChatAdapter extends BaseAdapter<AVIMMessage, ChatAdapter.ViewHolder> {

    public ChatAdapter(Context context) {
        super(context);
    }

    public void addMessage(AVIMTextMessage message) {
        mData.add(message);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int itemType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.activity_chat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AVIMMessage message = mData.get(position);
        if (message instanceof AVIMTextMessage) {
            holder.textView.setText(((AVIMTextMessage) message).getText());
        }
        if (message.getFrom().equals(AVHelper.getIMClientId())) {
            holder.textView.setGravity(Gravity.END);
        } else {
            holder.textView.setGravity(Gravity.START);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.text)
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
