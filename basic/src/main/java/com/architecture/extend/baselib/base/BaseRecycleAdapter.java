package com.architecture.extend.baselib.base;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:dongpo: 6/21/2016
 */
public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private SparseArray<List<? extends Object>> mDataLayouts = new SparseArray<>();
    private SparseArray<ViewDataBinding> headerFooters;
    private List<Integer> mHeaders;
    private List<Integer> mFooters;
    private int mHeaderCount;
    private int mFooterCount;
    private int mUserItemCount;

    public BaseRecycleAdapter(List<T> data, int layoutId) {
        mDataLayouts.put(layoutId, data);
        mUserItemCount = reCountUserItem();
    }

    public BaseRecycleAdapter() {
    }

    public void addItemType(List<? extends Object> data, int layoutId) {
        mDataLayouts.put(layoutId, data);
        mUserItemCount = reCountUserItem();
        notifyDataSetChanged();
    }

    public void replaceData(List data, int layoutId) {
        if (containsKey(layoutId, mDataLayouts)) {
            List<?> objects = mDataLayouts.get(layoutId);
            objects.clear();
            objects.addAll(data);
            mUserItemCount = reCountUserItem();
            notifyDataSetChanged();
            return;
        }
        mDataLayouts.put(layoutId, data);
        mUserItemCount = reCountUserItem();
        notifyDataSetChanged();
    }

    public void addData(List newData, int layoutId) {
        List<? extends Object> originData = mDataLayouts.get(layoutId);
        originData.addAll(newData);
        mUserItemCount = reCountUserItem();
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutIdFromViewType(viewType);
        ViewHolder holder;
        if ((mHeaders != null && mHeaders.contains(layoutId)) || (mFooters != null && mFooters
                .contains(layoutId))) {
            ViewDataBinding binding = headerFooters.get(layoutId);
            if (binding == null) {
                holder = ViewHolder.get(parent.getContext(), parent, layoutId);
                headerFooters.put(layoutId, holder.getBinding());
            } else {
                holder = ViewHolder.get(parent.getContext(), binding);
            }
        } else {
            holder = ViewHolder.get(parent.getContext(), parent, layoutId);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (isHeaderType(position)) {
            onBindHeaderData(holder, position, viewType);
            return;
        }

        if (isFooterType(position)) {
            onBindBottomData(holder, position, viewType);
            return;
        }

        int userPosition = convertUserPosition(position);
        holder.updatePosition(userPosition);
        onBindViewData(holder, userPosition, viewType);
    }

    protected abstract void onBindViewData(ViewHolder holder, int userPosition, int viewType);

    protected void onBindHeaderData(ViewHolder holder, int position, int viewType) {
    }

    protected void onBindBottomData(ViewHolder holder, int position, int viewType) {
    }


    @Override
    public int getItemCount() {
        return mHeaderCount + mFooterCount + mUserItemCount;
    }

    private List<? extends Object> getDataFromViewType(int viewType) {
        return mDataLayouts.get(viewType);
    }

    private int getLayoutIdFromViewType(int viewType) {
        return viewType;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderType(position)) {
            return mHeaders.get(position);
        } else if (isFooterType(position)) {
            position = convertFootPosition(position);
            return mFooters.get(position);
        } else {
            position = convertUserPosition(position);
            return getUserHolderType(position);
        }
    }

    private int convertUserPosition(int position) {
        return position - mHeaderCount;
    }

    private int convertFootPosition(int position) {
        return position - mHeaderCount - mUserItemCount;
    }

    protected int getUserHolderType(int userPosition) {
        return mDataLayouts.keyAt(0);
    }

    public <K> List<K> getData(int layoutId) {
        return (List<K>) mDataLayouts.get(layoutId);
    }

    public boolean isHeaderType(int position) {
        if (position < mHeaderCount) {
            return true;
        }
        return false;
    }

    public boolean isFooterType(int position) {
        int beforeFooterCount = mHeaderCount + mUserItemCount;
        int totalItemCount = getItemCount();
        if (position >= beforeFooterCount && position < totalItemCount) {
            return true;
        }
        return false;
    }

    public boolean isUserItemType(int position) {
        if (position >= mHeaderCount && position < mHeaderCount + mUserItemCount) {
            return true;
        }
        return false;
    }

    private int getHeaderCount() {
        if (mHeaders != null) {
            return mHeaders.size();
        }
        return 0;
    }

    private int getFooterCount() {
        if (mFooters != null) {
            return mFooters.size();
        }
        return 0;
    }

    private final int reCountUserItem() {
        int userItemCount = 0;
        for (int i = 0; i < mDataLayouts.size(); i++) {
            int layoutId = mDataLayouts.keyAt(i);
            List<?> data = mDataLayouts.get(layoutId);
            userItemCount += data.size();
        }
        return userItemCount;
    }

    public int getUserItemCounts() {
        return mUserItemCount;
    }

    public void addHeader(int viewType, @NonNull ViewDataBinding header) {
        if (headerFooters == null) {
            headerFooters = new SparseArray<>();
        }

        if (mHeaders == null) {
            mHeaders = new ArrayList<>();
        }
        mHeaders.add(viewType);
        headerFooters.put(viewType, header);
        mHeaderCount = getHeaderCount();
    }

    public ViewDataBinding getHeader(int index) {
        if (mHeaders != null && mHeaders.size() > 0) {
            int size = mHeaders.size();
            if (index < size) {
                return headerFooters.get(mHeaders.get(index));
            }
        }
        return null;
    }

    public void addFooter(int viewType, ViewDataBinding footer) {
        if (footer != null) {
            if (headerFooters == null) {
                headerFooters = new SparseArray<>();
            }

            if (mFooters == null) {
                mFooters = new ArrayList<>();
            }
            mFooters.add(viewType);
            headerFooters.put(viewType, footer);
            mFooterCount = getFooterCount();
        }
    }

    public ViewDataBinding getFooter(int index) {
        if (mFooters != null && mFooters.size() > 0) {
            int size = mFooters.size();
            if (index < size) {
                return headerFooters.get(mFooters.get(index));
            }
        }
        return null;
    }

    private boolean containsKey(int key, SparseArray sa) {
        int size = sa.size();
        for (int i = 0; i < size; i++) {
            int i1 = sa.keyAt(i);
            if (i1 == key) {
                return true;
            }
        }
        return false;
    }

    public void hideFooterView() {
        if (mFooterCount >= 1) {
            mFooterCount--;
            notifyDataSetChanged();
        }
    }

    public void showFooterView() {
        if (mFooterCount < getFooterCount()) {
            mFooterCount++;
            notifyDataSetChanged();
        }
    }
}
