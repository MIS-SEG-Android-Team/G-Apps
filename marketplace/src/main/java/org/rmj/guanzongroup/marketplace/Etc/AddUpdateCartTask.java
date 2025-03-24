package org.rmj.guanzongroup.marketplace.Etc;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.etc.ConnectionUtil;

/* TODO: NOT USED REPLACED ALREADY ON VMWISHLIST THROUGH TASKEXECUTOR METHOD
public class AddUpdateCartTask extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = AddUpdateCartTask.class.getSimpleName();
    private final ConnectionUtil poConnect;
    private final ROrder poOrdersx;
    private final OnTransactionsCallback poCallBck;
    private final int pnItemQty;
    private final boolean QtyUpdate;
    private String psMessage = "";

    public AddUpdateCartTask(Context foContext, int fnItemQty, boolean QtyUpdate, OnTransactionsCallback foCallBck) {
        this.poConnect = new ConnectionUtil(foContext);
        this.poOrdersx = new ROrder(foContext);
        this.poCallBck = foCallBck;
        this.pnItemQty = fnItemQty;
        this.QtyUpdate = QtyUpdate;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        poCallBck.onLoading();
    }
    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            String lsListIdx = strings[0];
            if(poConnect.isDeviceConnected()) {
                if(QtyUpdate){
                    Log.d(TAG, "Updating cart item with item ID: " + lsListIdx);
                    if(poOrdersx.UpdateCartQuantity(lsListIdx, pnItemQty)) {
                        psMessage = "Item added successfully";
                        return true;
                    } else {
                        Log.e(TAG, "Failed to update item on cart: " + psMessage);
                        psMessage = poOrdersx.getMessage();
                        return false;
                    }
                } else {
                    Log.d(TAG, "Adding item on cart: " + lsListIdx);
                    if(poOrdersx.AddUpdateCart(lsListIdx, pnItemQty)) {
                        psMessage = "Item added successfully";
                        return true;
                    } else {
                        Log.e(TAG, "Failed to add item on cart: " + psMessage);
                        psMessage = poOrdersx.getMessage();
                        return false;
                    }
                }
            } else {
                psMessage = "Unable to connect.";
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            psMessage = e.getMessage();
            return false;
        }
    }
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean) {
            poCallBck.onSuccess(psMessage);
        } else {
            poCallBck.onFailed(psMessage);
        }
    }
}*/
