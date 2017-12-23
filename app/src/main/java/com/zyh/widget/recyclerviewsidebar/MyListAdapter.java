package com.zyh.widget.recyclerviewsidebar;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * adpater
 * <p>
 * Created by zyh on 2017/11/6.
 */
public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyViewHolder> implements SectionIndexer {
    private Context mContext;
    private final List<MyItemEntity> mItemEntityList;
    protected List<String> list; //分段用list

    protected SparseIntArray positionOfSection;
    protected SparseIntArray sectionOfPosition;

    public MyListAdapter(Context context, List<MyItemEntity> itemEntityList) {
        mContext = context;
        this.mItemEntityList = itemEntityList;
    }

    @Override
    public MyListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyListAdapter.MyViewHolder holder, int position) {
        final MyItemEntity itemEntity = getItem(position);
        int itemViewType = holder.getItemViewType();
        switch (itemViewType) {
            case MyItemEntity.TYPE_DATA:
                holder.mNameTv.setText(itemEntity.getName());
                holder.mNameTv.setTextColor(Color.BLACK);
                holder.mNameTv.setBackgroundColor(Color.WHITE);
                break;
            case MyItemEntity.TYPE_INDEX:
                holder.mNameTv.setText(itemEntity.getName());
                holder.mNameTv.setTextColor(Color.RED);
                holder.mNameTv.setBackgroundColor(Color.GRAY);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItemEntityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemEntityList.get(position).getType();
    }

    public void refreshData(List<MyItemEntity> itemEntityList) {
        if (itemEntityList == null) {
            return;
        }
        int oldSize = mItemEntityList.size();
        mItemEntityList.clear();
        notifyItemRangeRemoved(0, oldSize);
        mItemEntityList.addAll(itemEntityList);
        notifyItemRangeInserted(0, itemEntityList.size());
    }

    private MyItemEntity getItem(int position) {
        return mItemEntityList.get(position);
    }

    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getItemCount();
        list = new ArrayList<>();
        if (count > 0) {
            list.add("↑");
            positionOfSection.put(0, 0);
            sectionOfPosition.put(0, 0);
            for (int i = 0; i < count; i++) {

                String letter = getItem(i).getHeader();
                int section = list.size() - 1;
                if (list.get(section) != null && !list.get(section).equals(letter)) {
                    list.add(letter);
                    section++;
                    positionOfSection.put(section, i);
                }
                sectionOfPosition.put(i, section);
            }
        }
        return list.toArray(new String[list.size()]);
    }

    @Override
    public int getPositionForSection(int i) {
        return positionOfSection.get(i);
    }

    @Override
    public int getSectionForPosition(int i) {
        return sectionOfPosition.get(i);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mNameTv;

        private MyViewHolder(View itemView) {
            super(itemView);
            prepareView(itemView);
        }

        private void prepareView(View itemView) {
            mNameTv = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}
