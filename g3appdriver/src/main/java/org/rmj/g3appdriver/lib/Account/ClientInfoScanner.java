package org.rmj.g3appdriver.lib.Account;

import android.content.Context;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DGcardApp;
import org.rmj.g3appdriver.dev.Database.Entities.EGCardTransactionLedger;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;
import org.rmj.g3appdriver.dev.Repositories.RGCardTransactionLedger;
import org.rmj.g3appdriver.etc.Telephony;
import org.rmj.g3appdriver.lib.GCardCore.CodeGenerator;
import org.rmj.g3appdriver.lib.GCardCore.GCardManager;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;

public class ClientInfoScanner {
    private static final String TAG = ClientInfoScanner.class.getSimpleName();

    private Context mContext;
    private CodeGenerator loCodeGen;

    private String message;
    private boolean isSuccess;
    private boolean isTransaction;

    public ClientInfoScanner(Context context){
        this.mContext = context;
        this.loCodeGen = new CodeGenerator();
    }

    public String getMessage() {
        return message;
    }


    public boolean isTransaction(){
        return isTransaction;
    }

    public boolean ParseQrCodeValue(String fsValue){
        try{
            loCodeGen.setEncryptedQrCode(fsValue);
            if(!loCodeGen.isCodeValid()){
                message = "Qr Code is not applicable to any GCard transaction.";
                return false;
            } else if(loCodeGen.isQrCodeTransaction()){
                isTransaction = true;

                return SaveGCardTransaction();
            } else {
                isTransaction = false;
                new GCardManager(mContext).AddGCardQrCode(loCodeGen.getGCardNumber(), new GCardSystem.GCardSystemCallback() {
                    @Override
                    public void OnSuccess(String args) {
                        isSuccess = true;
                    }

                    @Override
                    public void OnFailed(String errorMsg) {
                        message = errorMsg;
                        isSuccess = false;
                    }
                });
                return isSuccess;
            }
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    private boolean SaveGCardTransaction() throws Exception{
        DGcardApp loGcard = GGC_GuanzonAppDB.getInstance(mContext).EGcardAppDao();
        String lsMobile = new Telephony(mContext).getMobilNumbers();
        String lsCardNm = loGcard.getCardNo();
        if(!loCodeGen.isDeviceValid(lsMobile, lsCardNm)){
            message = "Mobile Number or Account is not valid to confirm this transaction";
            return false;
        } else {
            RGCardTransactionLedger loLedger = new RGCardTransactionLedger(mContext);
            if(loLedger.isTransactionValid(lsCardNm,
                    loCodeGen.getTransSource(),
                    loCodeGen.getSourceNo(),
                    loCodeGen.getSourceCD(),
                    loCodeGen.getSourceNo(),
                    loCodeGen.getPointsxx()) != null){
                EGCardTransactionLedger loTrans = new EGCardTransactionLedger();
                loTrans.setReferNox(loCodeGen.getSourceNo());
                loTrans.setGCardNox(lsCardNm);
                loTrans.setTransact(loCodeGen.getDTransact());
                loTrans.setSourceDs(loCodeGen.getTransSource());
                loTrans.setTranDesc(loCodeGen.getSourceCD());
                loTrans.setSourceNo(loCodeGen.getSourceNo());
                loTrans.setPointsxx(Double.parseDouble(loCodeGen.getPointsxx()));
                loTrans.setQRCodexx("1");
                loTrans.setReceived("0");
                loLedger.Save(loTrans);
                return true;
            } else {
                message = "Invalid transaction.";
                return false;
            }
        }
    }
}
