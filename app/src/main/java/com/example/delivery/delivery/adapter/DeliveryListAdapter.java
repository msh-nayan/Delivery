package com.example.delivery.delivery.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.delivery.delivery.R;
import com.example.delivery.delivery.entity.Delivery;
import com.example.delivery.delivery.interfaces.ItemClickListener;
import com.example.delivery.delivery.interfaces.PaginationListener;
import com.example.delivery.delivery.viewholder.ViewHolderDelivery;
import com.example.delivery.delivery.viewholder.ViewHolderEmpty;
import com.example.delivery.delivery.viewholder.ViewHolderProgress;

import java.util.ArrayList;
import java.util.List;

public class DeliveryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = DeliveryListAdapter.class.getSimpleName();

    class ViewType {
        int VIEW_PROGRESS = 1;
        int VIEW_NO_DATA = 2;
        int VIEW_NOTIFICATION = 3;
    }

    ViewType mViewType = new ViewType();

    private Context mContext;

    private int mShowNoItem = 0;

    private int mVisibleThreshold = 1;
    private int mLastVisibleItem;
    private int mTotalItemCount;
    private boolean isLoading;

    private List<Delivery> mDeliveryList;

    private PaginationListener mPaginationListener;
    private ItemClickListener mItemClickListener;

    public DeliveryListAdapter(Context context, List<Delivery> deliveryList, RecyclerView recyclerView, ItemClickListener itemClickListener) {
        mContext = context;
        mDeliveryList = deliveryList;
        mItemClickListener = itemClickListener;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    mTotalItemCount = linearLayoutManager.getItemCount();
                    mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && mTotalItemCount <= (mLastVisibleItem + mVisibleThreshold)) {
                        if (mPaginationListener != null) {
                            mPaginationListener.onPaginate();
                        }
                        isLoading = true;
                    }
                }
            });
        }
    }

    public void setmPaginationListener(PaginationListener paginationListener) {
        mPaginationListener = paginationListener;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void showNoItem() {
        mShowNoItem = 1;
        notifyDataSetChanged();
    }

    public List<Delivery> getmDeliveryList() {
        return mDeliveryList;
    }

    public void addItems(List<Delivery> deliveryList) {
        mDeliveryList.addAll(deliveryList);
        mShowNoItem = 0;
        notifyDataSetChanged();
    }

    public void addItem(Delivery delivery) {
        mDeliveryList.add(delivery);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeAll() {
        mDeliveryList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void removeItem() {
        if (mDeliveryList.size() > 0) {
            mDeliveryList.remove(mDeliveryList.size() - 1);
            notifyItemRemoved(mDeliveryList.size());
        }
    }

    @Override
    public int getItemCount() {
        return mDeliveryList.size() + mShowNoItem;
    }

    @Override
    public int getItemViewType(int position) {
        if (mShowNoItem == 0) {
            if (mDeliveryList.get(position) != null) return mViewType.VIEW_NOTIFICATION;
            else return mViewType.VIEW_PROGRESS;
        } else
            return mViewType.VIEW_NO_DATA;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mShowNoItem == 0) {
            if (viewType == mViewType.VIEW_NOTIFICATION) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery, parent, false);
                return new ViewHolderDelivery(mContext, view, mItemClickListener);
            } else if (viewType == mViewType.VIEW_PROGRESS) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
                return new ViewHolderProgress(view);
            }
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            return new ViewHolderEmpty(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mShowNoItem == 0) {
            if (holder instanceof ViewHolderDelivery) {
                ((ViewHolderDelivery) holder).onBind(mDeliveryList.get(position));
            } else if (holder instanceof ViewHolderProgress) {
                ((ViewHolderProgress) holder).onBind();
            }
        } else {
            if (holder instanceof ViewHolderEmpty) {
                ((ViewHolderEmpty) holder).onBind(mContext.getString(R.string.msg_db_empty_title), mContext.getString(R.string.msg_db_empty_message));
            }
        }
    }
}
