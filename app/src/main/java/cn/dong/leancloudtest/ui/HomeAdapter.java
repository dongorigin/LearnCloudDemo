package cn.dong.leancloudtest.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.model.Post;
import cn.dong.leancloudtest.ui.common.BaseAdapter;
import cn.dong.leancloudtest.util.DateUtils;

/**
 * @author dong on 15/7/11.
 */
public class HomeAdapter extends BaseAdapter<Post, HomeAdapter.HomeViewHolder> {

    public HomeAdapter(Context context) {
        super(context);
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        Post item = getItem(position);
        UserUtils.setUserAvatar(holder.itemView.getContext(), holder.avatarView, item.getUser());
        holder.usernameView.setText(item.getUser().getUsername());
        holder.timeView.setText(DateUtils.getTimeByDate(item.getUpdatedAt()));
        holder.contentView.setText(item.getContent());
    }

    static class HomeViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.avatar)
        ImageView avatarView;
        @InjectView(R.id.username)
        TextView usernameView;
        @InjectView(R.id.time)
        TextView timeView;
        @InjectView(R.id.content)
        TextView contentView;

        public HomeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
