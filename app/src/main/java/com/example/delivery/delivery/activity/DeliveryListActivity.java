package com.example.delivery.delivery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.delivery.delivery.Constant;
import com.example.delivery.delivery.InternetCheckerTask;
import com.example.delivery.delivery.Popup;
import com.example.delivery.delivery.R;
import com.example.delivery.delivery.Utility;
import com.example.delivery.delivery.adapter.DeliveryListAdapter;
import com.example.delivery.delivery.entity.Delivery;
import com.example.delivery.delivery.impl.DeliveryImpl;
import com.example.delivery.delivery.interfaces.DeliveryListViewListener;
import com.example.delivery.delivery.interfaces.ItemClickListener;
import com.example.delivery.delivery.interfaces.PaginationListener;
import com.example.delivery.delivery.presenter.DeliveryListPresenter;

import java.util.ArrayList;
import java.util.List;

public class DeliveryListActivity extends AppCompatActivity implements InternetCheckerTask.OnInternetCheckListener,
        DeliveryListViewListener,
        PaginationListener,
        Popup.DialogClickListener,
        ItemClickListener {

    private String TAG = DeliveryListActivity.class.getSimpleName();

    private Context mContext;
    private RecyclerView mRvList;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mInternetConnected;

    private DeliveryListAdapter mDeliveryListAdapter;
    private DeliveryListPresenter mDeliveryListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list);

        setupToolbar();
        init();
        initPresenter();
        checkNetworkConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDeliveryListPresenter.destroy();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    private void init() {
        mContext = DeliveryListActivity.this;

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        mRvList = findViewById(R.id.recycler_view_delivery);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRvList.setLayoutManager(layoutManager);

        mDeliveryListAdapter = new DeliveryListAdapter(mContext, new ArrayList<Delivery>(), mRvList, this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(Constant.ITEM_IN_TIME_MS);
        itemAnimator.setChangeDuration(Constant.ITEM_IN_TIME_MS);
        mRvList.setItemAnimator(itemAnimator);
        mRvList.setAdapter(mDeliveryListAdapter);
        mDeliveryListAdapter.setmPaginationListener(this);

        mProgressBar = findViewById(R.id.progress_bar);
    }

    private void checkNetworkConnection() {
        new InternetCheckerTask(mContext, this).execute();
    }

    private void initPresenter() {
        mDeliveryListPresenter = new DeliveryListPresenter(mContext, this, new DeliveryImpl());
    }

    private void bindData(List<Delivery> deliveries) {
        if (deliveries != null) {
            if (mRvList.getVisibility() == View.GONE)
                mRvList.setVisibility(View.VISIBLE);

            if (deliveries.isEmpty()) {
                if(mDeliveryListAdapter.getmDeliveryList().size() == 0) {
                    mDeliveryListAdapter.removeAll();
                    mDeliveryListAdapter.showNoItem();
                }

                mDeliveryListAdapter.removeItem();
                return;
            }

            mDeliveryListAdapter.removeItem();
            mDeliveryListAdapter.addItems(deliveries);
            mDeliveryListAdapter.setLoaded();

            mDeliveryListPresenter.storeDeliveryListOnDB(mContext, deliveries);
        }
    }

    private void disableSwipeToRefresh() {
        if(mSwipeRefreshLayout.getVisibility() == View.VISIBLE && mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mSwipeRefreshLayout.setRefreshing(true);
            mDeliveryListAdapter.removeAll();
            checkNetworkConnection();
        }
    };

    @Override
    public void onConnectionListener(int status) {
        mInternetConnected = status == Constant.CONNECTED;
        switch (status) {
            case Constant.CONNECTED:
                mDeliveryListPresenter.getDeliveryList(mInternetConnected, 0, Constant.LIMIT, mSwipeRefreshLayout.isRefreshing() ? false : true);
                break;
            default:
                disableSwipeToRefresh();
                new Popup(mContext, this)
                        .showDialog(getString(R.string.msg_dialog_title), getString(R.string.msg_dialog_message));
                break;
        }
    }

    @Override
    public void onDeliveryListError() {
        Utility.showToast(mContext, getString(R.string.err_message));
        disableSwipeToRefresh();
        mDeliveryListAdapter.removeItem();
    }

    @Override
    public void onDeliveryListSuccess(List<Delivery> deliveries) {
        Log.e(TAG, "_log : onDeliveryListSuccess : size : " + deliveries.size());
        disableSwipeToRefresh();
        bindData(deliveries);
    }

    @Override
    public void onNoInternet() {
        Utility.showToast(mContext, getString(R.string.msg_no_interet));
    }

    @Override
    public void onShowProgress() {
        if (mProgressBar.getVisibility() == View.GONE) mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        if (mProgressBar.getVisibility() == View.VISIBLE || mProgressBar.getVisibility() == View.INVISIBLE)
            mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPaginate() {
        if(mInternetConnected) {
            mDeliveryListPresenter.getDeliveryList(mInternetConnected, mDeliveryListAdapter.getmDeliveryList().size(), Constant.LIMIT, false);
            mDeliveryListAdapter.addItem(null);
        }
    }

    @Override
    public void onNegativeButtonClick() {
        finish();
    }

    @Override
    public void onPositiveButtonClick() {
        mDeliveryListPresenter.getDeliveryList(mInternetConnected, Constant.OFFSET, Constant.LIMIT, true);
    }

    @Override
    public void onItemClick(Delivery delivery) {
        startActivity(new Intent(mContext, DetailsActivity.class).putExtra(Constant.KEY_DELIVERY, delivery));
    }
}
