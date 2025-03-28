package org.rmj.g3appdriver.dev.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DProduct;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DSearchLog;
import org.rmj.g3appdriver.dev.Database.Entities.EProducts;
import org.rmj.g3appdriver.dev.Database.Entities.ESearchLog;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;
import org.rmj.g3appdriver.dev.ServerRequest.HttpHeaders;
import org.rmj.g3appdriver.dev.ServerRequest.ServerAPIs;
import org.rmj.g3appdriver.dev.ServerRequest.WebClient;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.FilterType;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.apprdiver.util.SQLUtil;
import org.rmj.g3appdriver.lib.Account.AccountInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RProduct {
    private static final String TAG = RProduct.class.getSimpleName();

    private final Context mContext;
    private final DProduct poDao;
    private final DSearchLog poSearch;

    private JSONObject data;
    private String message;

    private final MutableLiveData<oFilterx> poProdType = new MutableLiveData<>();

    public RProduct(Context context){
        this.mContext = context;
        this.poDao = GGC_GuanzonAppDB.getInstance(mContext).prodctDao();
        this.poSearch = GGC_GuanzonAppDB.getInstance(mContext).searchDao();
    }

    public JSONObject getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean ImportProductList() {
        try {
            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());
            JSONObject params = new JSONObject();
            params.put("bsearch", true);
            params.put("descript", "");

            String lsTimeStmp = poDao.GetLatestProductStamp();
            if(lsTimeStmp != null){
                params.put("timestamp", lsTimeStmp);
            }
            String lsResponse = WebClient.httpsPostJSon(
                    loApis.getImportProducts(),
                    params.toString(),
                    new HttpHeaders(mContext).getHeaders());
            if (lsResponse == null) {
                message = "Unable to retrieve server response.";
                return false;
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if (!lsResult.equalsIgnoreCase("success")) {
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = loError.getString("message");
                    return false;
                } else {
                    JSONArray laDetail = loResponse.getJSONArray("detail");
                    for (int x = 0; x < laDetail.length(); x++) {
                        JSONObject joDetail = laDetail.getJSONObject(x);
                        EProducts loDetail = poDao.GetProductIfExist(joDetail.getString("sListngID"));
                        if (loDetail == null) {
                            EProducts loProdct = new EProducts();
                            loProdct.setListngID(joDetail.getString("sListngID"));
                            loProdct.setBriefDsc(joDetail.getString("sBriefDsc"));
                            loProdct.setStockIDx(joDetail.getString("sStockIDx"));
                            loProdct.setDescript(joDetail.getString("sDescript"));
                            loProdct.setRatingxx(joDetail.getString("nRatingxx"));
                            loProdct.setBarCodex(joDetail.getString("xBarCodex"));
                            loProdct.setDescrptx(joDetail.getString("xDescript"));
                            loProdct.setBrandNme(joDetail.getString("xBrandNme"));
                            loProdct.setModelNme(joDetail.getString("xModelNme"));
                            loProdct.setModelIDx(joDetail.getString("sModelIDx"));
                            loProdct.setImagesxx(joDetail.getString("sImagesxx"));
                            loProdct.setColorNme(joDetail.getString("xColorNme"));
                            loProdct.setCategrNm(joDetail.getString("xCategrNm"));
                            loProdct.setTotalQty(joDetail.getString("nTotalQty"));
                            loProdct.setQtyOnHnd(joDetail.getString("nQtyOnHnd"));
                            loProdct.setAllwCrdt(joDetail.getString("cAllwCrdt"));
                            loProdct.setResvOrdr(joDetail.getString("nResvOrdr"));
                            loProdct.setSoldQtyx(joDetail.getString("nSoldQtyx"));
                            loProdct.setUnitPrce(joDetail.getString("nUnitPrce"));
                            loProdct.setListStrt(joDetail.getString("dListStrt"));
                            loProdct.setListEndx(joDetail.getString("dListEndx"));
                            loProdct.setTranStat(joDetail.getString("cTranStat"));
                            loProdct.setTimeStmp(joDetail.getString("dTimeStmp"));
                            poDao.SaveProductInfo(loProdct);
                            Log.d(TAG, "New product listing save!");
                        } else {
                            Date ldDate1 = SQLUtil.toDate(loDetail.getTimeStmp(), SQLUtil.FORMAT_TIMESTAMP);
                            Date ldDate2 = SQLUtil.toDate((String) joDetail.get("dTimeStmp"), SQLUtil.FORMAT_TIMESTAMP);
                            if (!ldDate1.equals(ldDate2)) {
                                loDetail.setBriefDsc(joDetail.getString("sBriefDsc"));
                                loDetail.setStockIDx(joDetail.getString("sStockIDx"));
                                loDetail.setDescript(joDetail.getString("sDescript"));
                                loDetail.setRatingxx(joDetail.getString("nRatingxx"));
                                loDetail.setBarCodex(joDetail.getString("xBarCodex"));
                                loDetail.setDescrptx(joDetail.getString("xDescript"));
                                loDetail.setBrandNme(joDetail.getString("xBrandNme"));
                                loDetail.setModelNme(joDetail.getString("xModelNme"));
                                loDetail.setModelIDx(joDetail.getString("sModelIDx"));
                                loDetail.setImagesxx(joDetail.getString("sImagesxx"));
                                loDetail.setColorNme(joDetail.getString("xColorNme"));
                                loDetail.setCategrNm(joDetail.getString("xCategrNm"));
                                loDetail.setTotalQty(joDetail.getString("nTotalQty"));
                                loDetail.setQtyOnHnd(joDetail.getString("nQtyOnHnd"));
                                loDetail.setAllwCrdt(joDetail.getString("cAllwCrdt"));
                                loDetail.setResvOrdr(joDetail.getString("nResvOrdr"));
                                loDetail.setSoldQtyx(joDetail.getString("nSoldQtyx"));
                                loDetail.setUnitPrce(joDetail.getString("nUnitPrce"));
                                loDetail.setListStrt(joDetail.getString("dListStrt"));
                                loDetail.setListEndx(joDetail.getString("dListEndx"));
                                loDetail.setTranStat(joDetail.getString("cTranStat"));
                                loDetail.setTimeStmp(joDetail.getString("dTimeStmp"));
                                poDao.UpdateProductInfo(loDetail);
                                Log.d(TAG, "New product listing updated!");
                            }
                        }
                    }
                    return true;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public LiveData<List<EProducts>> GetProductList(int fnIndex){
        return poDao.GetProductList(fnIndex);
    }

    public LiveData<EProducts> GetProductInfo(String fsListID){
        return poDao.GetProductInfo(fsListID);
    }

    public boolean GetProductRatings(String fsVal){
        try{
            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());
            JSONObject params = new JSONObject();
            params.put("sListIDxx", fsVal);
            String lsResponse = WebClient.httpsPostJSon(
                    loApis.getImportReviewsAPI(),
                    params.toString(),
                    new HttpHeaders(mContext).getHeaders());
            if(lsResponse == null){
                message = "Unable to retrieve server response.";
                Log.e(TAG, "Unable to retrieve server response.");
                return false;
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                Log.d(TAG, lsResponse);
                String lsResult = loResponse.getString("result");
                if(!lsResult.equalsIgnoreCase("success")){
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = loError.getString("message");
                    return false;
                } else {
                    data = loResponse;
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public boolean GetQuestionsAndAnswers(String fsVal){
        try{
            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());
            JSONObject params = new JSONObject();
            params.put("sListIDxx", fsVal);
            String lsResponse = WebClient.httpsPostJSon(
                    loApis.getQuestionsAndAnswersAPI(),
                    params.toString(),
                    new HttpHeaders(mContext).getHeaders());
            if(lsResponse == null){
                message = "Unable to retrieve server response.";
                return false;
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if(!lsResult.equalsIgnoreCase("success")){
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = loError.getString("message");
                    return false;
                } else {
                    data = loResponse;
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public void setFilterType(oFilterx foVal){
        this.poProdType.setValue(foVal);
    }

    public LiveData<oFilterx> GetFilterType(){
        return poProdType;
    }

    public LiveData<List<DProduct.oProduct>> GetProductsList(int fnIndex, FilterType foType, String fsArgs1, String fsArgs2){
        switch (foType){
            case PRICE_DESCENDING:
                return poDao.GetProductsListPriceSortDESC(fnIndex);
            case PRICE_ASCENDING:
                return poDao.GetProductsListPriceSortASC(fnIndex);
            case CATEGORY:
                return poDao.GetProductsListFilterCategory(fnIndex, fsArgs1);
            case BRAND_NAME:
                return poDao.GetProductsListFilterBrandName(fnIndex, fsArgs1);
            case PRICE_RANGE:
                return poDao.GetProductsListFilterPriceRange(fnIndex, fsArgs1, fsArgs2);
            default:
                return poDao.GetProductsList(fnIndex);
        }
    }

    public LiveData<List<DProduct.oProduct>> GetProductsForLoanApplication(){
        return poDao.GetProductsForLoanApplication();
    }

    public LiveData<List<DProduct.oProduct>> SearchLoanProducts(String fsVal){
        return poDao.SearchLoanProducts(fsVal);
    }

    public static class oFilterx{
        private final FilterType oFilter;
        private final String fnArgs1, fnArgs2;

        public oFilterx(FilterType oFilter, String fnArgs1, String fnArgs2) {
            this.oFilter = oFilter;
            this.fnArgs1 = fnArgs1;
            this.fnArgs2 = fnArgs2;
        }

        public FilterType getFilter() {
            return oFilter;
        }

        public String getArgs1() {
            return fnArgs1;
        }

        public String getArgs2() {
            return fnArgs2;
        }
    }

    public LiveData<List<DProduct.oProduct>> SearchProducts(String fsVal){
        return poDao.SearchProducts(fsVal);
    }

    public LiveData<List<DProduct.oProduct>> GetProductsOnBrand(String fsArgs, String fsArgs1){
        return poDao.GetProductsOnBrand(fsArgs, fsArgs1);
    }

    public boolean SearchProduct(String fsVal){
        try {
            AddNewSearch(fsVal);
            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());
            JSONObject params = new JSONObject();
            params.put("bsearch", true);
            params.put("descript", fsVal);
            String lsResponse = WebClient.httpsPostJSon(
                    loApis.getImportProducts(),
                    params.toString(),
                    new HttpHeaders(mContext).getHeaders());
            if (lsResponse == null) {
                message = "Unable to retrieve server response.";
                return false;
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if (!lsResult.equalsIgnoreCase("success")) {
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = loError.getString("message");
                    return false;
                } else {
                    JSONArray laDetail = loResponse.getJSONArray("detail");
                    for (int x = 0; x < laDetail.length(); x++) {
                        JSONObject joDetail = laDetail.getJSONObject(x);
                        if (poDao.GetProductIfExist(joDetail.getString("sListngID")) == null) {
                            EProducts loProdct = new EProducts();
                            loProdct.setListngID(joDetail.getString("sListngID"));
                            loProdct.setBriefDsc(joDetail.getString("sBriefDsc"));
                            loProdct.setDescript(joDetail.getString("sDescript"));
                            loProdct.setBarCodex(joDetail.getString("xBarCodex"));
                            loProdct.setDescrptx(joDetail.getString("xDescript"));
                            loProdct.setRatingxx(joDetail.getString("nRatingxx"));
                            loProdct.setBrandNme(joDetail.getString("xBrandNme"));
                            loProdct.setModelNme(joDetail.getString("xModelNme"));
                            loProdct.setImagesxx(joDetail.getString("sImagesxx"));
                            loProdct.setColorNme(joDetail.getString("xColorNme"));
                            loProdct.setCategrNm(joDetail.getString("xCategrNm"));
                            loProdct.setTotalQty(joDetail.getString("nTotalQty"));
                            loProdct.setQtyOnHnd(joDetail.getString("nQtyOnHnd"));
                            loProdct.setResvOrdr(joDetail.getString("nResvOrdr"));
                            loProdct.setSoldQtyx(joDetail.getString("nSoldQtyx"));
                            loProdct.setUnitPrce(joDetail.getString("nUnitPrce"));
                            loProdct.setListStrt(joDetail.getString("dListStrt"));
                            loProdct.setListEndx(joDetail.getString("dListEndx"));
                            loProdct.setTranStat(joDetail.getString("cTranStat"));
                            loProdct.setTimeStmp(joDetail.getString("dTimeStmp"));
                            poDao.SaveProductInfo(loProdct);
                            Log.d(TAG, "New product listing save!");
                        } else {
                            EProducts loProdct = poDao.GetProductIfExist(joDetail.getString("sListngID"));
                            Date ldDate1 = SQLUtil.toDate(loProdct.getTimeStmp(), SQLUtil.FORMAT_TIMESTAMP);
                            Date ldDate2 = SQLUtil.toDate((String) joDetail.get("dTimeStmp"), SQLUtil.FORMAT_TIMESTAMP);
                            if (!ldDate1.equals(ldDate2)) {
                                poDao.UpdateProductListing(joDetail.getString("sListngID"),
                                        joDetail.getString("nTotalQty"),
                                        joDetail.getString("nQtyOnHnd"),
                                        joDetail.getString("nResvOrdr"),
                                        joDetail.getString("nSoldQtyx"),
                                        joDetail.getString("nUnitPrce"),
                                        joDetail.getString("dListStrt"),
                                        joDetail.getString("dListEndx"),
                                        joDetail.getString("cTranStat"),
                                        joDetail.getString("dTimeStmp"));
                                Log.d(TAG, "New product listing updated!");
                            }
                        }
                    }
                    return true;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public boolean SendProductInquiry(String ListID, String Question){
        try{
            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());
            JSONObject params = new JSONObject();
            AccountInfo loUser = new AccountInfo(mContext);
            params.put("sListngID", ListID);
            params.put("sQuestion", Question);
            params.put("sCreatedx", loUser.getUserID());
            params.put("dCreatedx", new AppConstants().DATE_MODIFIED);
            String lsResponse = WebClient.httpsPostJSon(
                    loApis.getSubmitInquiryAPI(),
                    params.toString(),
                    new HttpHeaders(mContext).getHeaders());
            if(lsResponse == null){
                message = "Unable to retrieve server response.";
                return false;
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if(!lsResult.equalsIgnoreCase("success")){
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = loError.getString("message");
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public boolean SendProductReview(String OrderID, String ListID, int Rate, String Remarks){
        try{
            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());
            JSONObject params = new JSONObject();
            AccountInfo loUser = new AccountInfo(mContext);
            params.put("sTransNox", OrderID);
            params.put("sListngID", ListID);
            params.put("nRatingxx", Rate);
            params.put("sRemarksx", Remarks);
            params.put("sCreatedx", loUser.getUserID());
            params.put("dCreatedx", new AppConstants().DATE_MODIFIED);
            String lsResponse = WebClient.httpsPostJSon(
                    loApis.getSubmitReviewAPI(),
                    params.toString(),
                    new HttpHeaders(mContext).getHeaders());
            if(lsResponse == null){
                message = "Unable to retrieve server response.";
                return false;
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if(!lsResult.equalsIgnoreCase("success")){
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = loError.getString("message");
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public boolean AddNewSearch(String fsArgs){
        try{
            if(poSearch.CheckIfExist(fsArgs) == null){
                ESearchLog loSearch = new ESearchLog();
                int nEntryNox = poSearch.CreateNewEntryNox();
                loSearch.setEntryNox(nEntryNox);
                loSearch.setSearchxx(fsArgs);
                loSearch.setTimeStmp(new AppConstants().DATE_MODIFIED);
                poSearch.SaveNewSearch(loSearch);
                return true;
            } else {
                ESearchLog loSearch = poSearch.CheckIfExist(fsArgs);
                poSearch.UpdateSearch(loSearch.getEntryNox(), new AppConstants().DATE_MODIFIED);
                return true;
            }
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }
}
