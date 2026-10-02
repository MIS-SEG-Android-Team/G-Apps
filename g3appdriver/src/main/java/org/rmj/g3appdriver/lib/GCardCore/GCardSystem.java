package org.rmj.g3appdriver.lib.GCardCore;

import android.content.Context;

public class GCardSystem {
    private static final String TAG = GCardSystem.class.getSimpleName();

    private Context mContext;

    public interface GCardSystemCallback{
        void OnSuccess(String args);
        void OnFailed(String message);
    }

    public interface ParseQrCodeCallback{
        void ApplicationResult(String src, Object args);
        void TransactionResult(String src, Object args);
        void OnFailed(String message);
    }

    public enum CoreFunctions{
        GCARD,
        REDEMPTION,
        EXTRAS
    }
    public GCardSystem(Context context) {
        this.mContext = context;
    }
    public iGCardSystem getInstance(CoreFunctions core){
        if(core == CoreFunctions.GCARD){
            return new GCardManager(mContext);
        } else if(core == CoreFunctions.REDEMPTION){
            return new RedemptionManager(mContext);
        } else {
            return new SystemExtras(mContext);
        }
    }
}
