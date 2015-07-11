package cn.dong.leancloudtest.ui.common;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dong on 15/6/30.
 */
public abstract class BaseAdapter<I, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<I> mData = new ArrayList<>();
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mItemLongClickListener) {
        this.mOnItemLongClickListener = mItemLongClickListener;
    }

    public I getItem(int position) {
        return mData.get(position);
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void setData(List<I> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(List<I> data) {
        int positionStart = getItemCount();
        mData.addAll(data);
        notifyItemRangeInserted(positionStart, data.size());
    }

}
