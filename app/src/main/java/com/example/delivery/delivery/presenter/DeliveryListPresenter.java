package com.example.delivery.delivery.presenter;

import android.content.Context;

import com.example.delivery.delivery.Utility;
import com.example.delivery.delivery.db.DbHelper;
import com.example.delivery.delivery.entity.Delivery;
import com.example.delivery.delivery.interfaces.DeliveryListListener;
import com.example.delivery.delivery.interfaces.DeliveryListViewListener;

import java.util.List;

public class DeliveryListPresenter implements DeliveryListListener.DeliveryListener {

    private String TAG = DeliveryListPresenter.class.getSimpleName();

    private Context mContext;
    private DeliveryListViewListener mDeliveryListViewListener;
    private DeliveryListListener mDeliveryListListener;

    public DeliveryListPresenter(Context context, DeliveryListViewListener deliveryListViewListener, DeliveryListListener deliveryListListener) {
        mContext = context;
        mDeliveryListViewListener = deliveryListViewListener;
        mDeliveryListListener = deliveryListListener;
    }

    public void destroy() {
        mDeliveryListViewListener = null;
    }

    public void getDeliveryList(boolean connection_status, int offset, int limit, boolean isProgressBarShow) {
        if (isProgressBarShow && mDeliveryListViewListener != null) {
            mDeliveryListViewListener.onShowProgress();
        }

        if(connection_status) {
            if (mDeliveryListViewListener != null) {
                if (!Utility.isConnectionAvailable(mContext)) {
                    mDeliveryListViewListener.onNoInternet();
                    return;
                }
            }

            mDeliveryListListener.getDeliveryList(offset, limit, this);
        } else {
            mDeliveryListListener.getDeliveryListOffline(mContext, offset, offset + limit/*limit*/, this);
        }
    }

    public void storeDeliveryListOnDB(Context context, List<Delivery> deliveries) {
        DbHelper dbHelper = new DbHelper(context);
        for (Delivery delivery : deliveries)
            dbHelper.insertDelivery(delivery);
    }

    @Override
    public void onError() {
        if (mDeliveryListViewListener != null) {
            mDeliveryListViewListener.onHideProgress();
            mDeliveryListViewListener.onDeliveryListError();
        }
    }

    @Override
    public void onSuccess(List<Delivery> deliveries) {
        if (mDeliveryListViewListener != null) {
            mDeliveryListViewListener.onHideProgress();
            mDeliveryListViewListener.onDeliveryListSuccess(deliveries);
        }
    }
}
