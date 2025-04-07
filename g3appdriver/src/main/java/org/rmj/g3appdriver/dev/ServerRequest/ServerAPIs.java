package org.rmj.g3appdriver.dev.ServerRequest;

import static org.rmj.g3appdriver.dev.ServerRequest.APILocation.GCARD;
import static org.rmj.g3appdriver.dev.ServerRequest.APILocation.LIVE;
import static org.rmj.g3appdriver.dev.ServerRequest.APILocation.LOCAL;
import static org.rmj.g3appdriver.dev.ServerRequest.APILocation.SECURITY;

import android.util.Log;

public class ServerAPIs {

    private boolean isTestUnit;

    private static final String SIGN_IN = SECURITY + "signin.php";
    private static final String RESEND_OTP = SECURITY + "send_otp.php";
    private static final String REGISTRATION = SECURITY + "signup.php";
    private static final String RETRIEVE_PASSWORD = SECURITY + "forgotpswd.php";
    private static final String URL_SUBMIT_APP_VERSION = SECURITY + "updateUserAppVersion.php";

    private static final String URL_CHECK_APP_VERSION = GCARD + "version_checker.php";
    private static final String URL_ADD_NEW_GCARD = GCARD + "add_gcardnumber.php";
    private static final String URL_IMPORT_PROMOLINK = GCARD + "import_promo_link.php";
    private static final String URL_IMPORT_PLACE_ORDER = GCARD + "import_placed_orders.php";
    private static final String URL_IMPORT_REDEEM_ITEMS = GCARD + "import_redeem_item.php";
    private static final String URL_IMPORT_TRANSACTIONS_OFFLINE = GCARD + "import_trans_offline.php";
    private static final String URL_IMPORT_TRANSACTIONS_ONLINE = GCARD + "import_trans_online.php";
    private static final String URL_IMPORT_TRANSACTIONS_PREORDER = GCARD + "import_trans_preorder.php";
    private static final String URL_IMPORT_TRANSACTIONS_REDEMPTION = GCARD + "import_trans_redemption.php";
    private static final String URL_IMPORT_MC_REGISTRATION = GCARD + "import_registration.php";
    private static final String URL_IMPORT_SERVICE = GCARD + "import_service.php";
    private static final String URL_IMPORT_BRANCH = GCARD + "import_branch.php";
    private static final String URL_IMPORT_GCARD = GCARD + "import_gcard.php";
    private static final String URL_REQUEST_AVAIL_POINTS = GCARD + "request_avl_points.php";
    private static final String URL_PLACE_ODER = GCARD + "place_order.php";
    private static final String URL_CANCEL_ORDER = GCARD + "cancel_order_item.php";
    private static final String URL_IMPORT_EVENTS = GCARD + "import_events.php";
    private static final String URL_IMPORT_EVENTS_PAGEANT = "gfest/getEvents.php";
    private static final String URL_CHANGE_PASSWORD = SECURITY + "acctupdate.php";
    private static final String URL_REQUEST_DEVICES = SECURITY + "acctdevice.php";
    private static final String URL_CLIENT_LOGIN = SECURITY + "signin.php";
    private static final String IMPORT_BARANGAY = "integsys/param/download_barangay.php";
    private static final String IMPORT_TOWN = "integsys/param/download_town.php";
    private static final String IMPORT_PROVINCE = "integsys/param/download_province.php";
    private static final String IMPORT_COUNTRIES = "integsys/param/download_country.php";
    private static final String RETRIEVE_CLIENT_INFO = "integsys/marketplace/get_clientby_id.php";
    private static final String RETRIEVE_PROFILE_PICTURE = "";
    private static final String RETRIEVE_VERIFIED_ID = "";
//    private static final String REGISTER_ACCOUNT = "integsys/marketplace/add_new_client.php";
    private static final String REGISTER_ACCOUNT = "integsys/marketplace/complete_account_detail.php";
    private static final String SUBMIT_SELFIE_VERIFICATION = "integsys/marketplace/submit_selfie_verification.php";
    private static final String SUBMIT_ID_VERIFICATION = "integsys/marketplace/submit_id_verification.php";
    private static final String IMPORT_ACCOUNT_INFO = "integsys/marketplace/get_client_info.php";
    private static final String IMPORT_CLIENT_INFO = "integsys/marketplace/get_client_exist.php";
    private static final String IMPORT_VALID_ID_CODE = "integsys/marketplace/import_valid_id_code.php";
//    private static final String IMPORT_ACCOUNT_INFO = "integsys/marketplace/import_client_detail.php";
    private static final String UPDATE_ACCOUNT_INFO = "integsys/marketplace/update_client_info.php";
    private static final String UPDATE_MOBILE_NO = "integsys/marketplace/request_mobile_update.php";
    private static final String UPDATE_CLIENT_MOBILE_NO = "integsys/marketplace/update_client_mobile.php";
    private static final String VERIFY_CLIENT_MOBILE_NO = "integsys/marketplace/verify_client_mobile.php";
    private static final String IMPORT_MOBILE_NO = "integsys/marketplace/import_client_mobile.php";
    private static final String UPDATE_ADDRESS = "integsys/marketplace/request_address_update.php";
    private static final String URL_UPDATE_ADDRESS = "integsys/marketplace/update_client_address.php";
    private static final String URL_UPDATE_MOBILE = "integsys/marketplace/update_client_mobile.php";
    private static final String URL_UPDATE_EMAIL = "integsys/marketplace/update_client_email.php";
    private static final String VERIFY_UPDATE_EMAIL = "integsys/marketplace/verify_client_email.php";
    private static final String IMPORT_EMAILS = "integsys/marketplace/import_client_email.php";
    private static final String IMPORT_PAYMENT_METHODS = "";
    private static final String UPDATE_PAYMENT_METHOD = "";
    private static final String IMPORT_SHIPPING_INFO = "";
    private static final String IMPORT_TOP_SELLING_ITEMS = "";
    private static final String IMPORT_PROMO_ITEMS = "integsys/marketplace/download_item_list.php";
    private static final String IMPORT_PRICE_LIST = "integsys/marketplace/download_pricelist.php";
    private static final String IMPORT_SEARCH_RESULTS = "";
    private static final String IMPORT_PRODUCT_INFO = "";
    private static final String ADD_TO_CART_ITEM = "integsys/marketplace/add_to_cart.php";
    private static final String UPDATE_CART_ITEM = "integsys/marketplace/remove_to_cart.php";
    private static final String IMPORT_CART_ITEMS = "integsys/marketplace/download_cart.php";
    private static final String MKTPL_PLACE_ORDER = "integsys/marketplace/place_order.php";
    private static final String MKTPL_CANCEL_ORDER = "integsys/marketplace/cancel_order.php";
    private static final String IMPORT_MKTPL_CANCELLATIONS = "integsys/marketplace/import_cancellation_detail.php";
    private static final String MKTPL_ORDER_PAYMENT = "integsys/marketplace/place_order_payment.php";
    private static final String IMPORT_PURCHASES = "integsys/marketplace/download_purchases.php";
    private static final String IMPORT_RETRIEVE_SHIPPING_ADDRESS = "";
    private static final String UPDATE_SHIPPING_ADDRESS = "";
    private static final String REQUEST_ADDRESS_UPDATE = "integsys/marketplace/request_address_update.php";
    private static final String REQUEST_MOBILE_UPDATE = "integsys/marketplace/request_mobile_update.php";
    private static final String IMPORT_DEFAULT_PAYMENT_METHOD = "";
    private static final String UPDATE_DEFAULT_PAYMENT_METHOD = "";
    private static final String IMPORT_SHIPPING_STATUS = "";
    private static final String IMPORT_ORDER_SUMMARY = "";
    private static final String IMPORT_BANK_ACC = "integsys/marketplace/download_bank_account.php";
    private static final String IMPORT_REVIEWS = "integsys/marketplace/import_product_reviews.php";
    private static final String IMPORT_INQUIRIES = "integsys/marketplace/import_product_inquiries.php";
    private static final String SUBMIT_REVIEW = "integsys/marketplace/submit_product_review.php";
    private static final String SUBMIT_INQUIRY = "integsys/marketplace/submit_product_inquiry.php";

    private static final String SUBMIT_MEANS_INFO = "integsys/gocas/mp_submit_means_info.php";
    private static final String SUBMIT_OTHER_INFO = "integsys/gocas/mp_submit_other_info.php";
    private static final String GET_OTHER_APPLICATION_INFO = "integsys/gocas/mp_get_other_application_info.php";
    private static final String SUBMIT_LOAN_APPLICATION = "integsys/gocas/mp_submit_credit_app.php";

    private static final String GET_PANALO_REWARDS = "gconnect/upload/getUserPanalo.php";

    private static final String IMPORT_NOTIFICATIONS = "integsys/notification/request_previous_notifications.php";
    private static final String SEND_RESPONSE = "https://restgk.guanzongroup.com.ph/notification/send_response.php";
    private static final String SEND_REQUEST = "https://restgk.guanzongroup.com.ph/notification/send_request.php";
    private static final String CHECK_UPDATE_URL = "https://restgk.guanzongroup.com.ph/gcard/ms/version_checker.php";

    private static final String URL_DOWNLOAD_RELATION = "gcircle/params/download_relation.php";
    private static final String URL_SUBMIT_INQUIRY = "gcircle/ganado/submit_inquiry.php";
    private static final String URL_DOWNLOAD_INQUIRIES = "gcircle/ganado/import_inquiries.php";

    private static final String URL_IMPORT_MC_MODEL = "integsys/param/download_mc_model.php";
    private static final String URL_IMPORT_MC_COLOR = "integsys/param/download_mc_model_color.php";
    private static final String URL_IMPORT_MC_PRICES = "gcircle/ganado/download_cash_price.php";
    private static final String URL_IMPORT_MC_MODEL_PRICE = "integsys/param/download_mc_model_price.php";
    private static final String URL_IMPORT_BRAND = "integsys/param/download_brand.php";
    private static final String URL_IMPORT_MC_CATEGORY = "integsys/param/download_mc_category.php";
    private static final String URL_IMPORT_TERM_CATEGORY = "integsys/param/download_mc_term_category.php";
    private static final String URL_IMPORT_BRANCHES = "integsys/param/download_branch.php";
    private static final String URL_IMPORT_TOWN = "integsys/param/download_town.php";
    private static final String URL_IMPORT_GCARD_POINTS = GCARD + "dgcard_points_request.php";

    public ServerAPIs(boolean isUnitTest) {
        this.isTestUnit = isUnitTest;
    }

    public String getSIGN_IN() {
        if(isTestUnit){
            return LOCAL + SIGN_IN;
        }
        return LIVE + SIGN_IN;
    }

    public String getRegisterAcountAPI() {
        if(isTestUnit){
            return LOCAL + REGISTRATION;
        }
        return LIVE + REGISTRATION;
    }

    public String getRetrievePasswordAPI() {
        if(isTestUnit){
            return LOCAL + RETRIEVE_PASSWORD;
        }
        return LIVE + RETRIEVE_PASSWORD;
    }

    public String getCheckAppVersionAPI() {
        if(isTestUnit){
            return LOCAL + URL_CHECK_APP_VERSION;
        }
        return LIVE + URL_CHECK_APP_VERSION;
    }

    public String getAddNewGCardAPI() {
        if(isTestUnit){
            return LOCAL + URL_ADD_NEW_GCARD;
        }
        return LIVE + URL_ADD_NEW_GCARD;
    }

    public String getImportPromosAPI() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_PROMOLINK;
        }
        return LIVE + URL_IMPORT_PROMOLINK;
    }

    public String getImportPlaceOrderAPI() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_PLACE_ORDER;
        }
        return LIVE + URL_IMPORT_PLACE_ORDER;
    }

    public String getImportRedeemItemsAPI() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_REDEEM_ITEMS;
        }
        return LIVE + URL_IMPORT_REDEEM_ITEMS;
    }

    public String getImportOfflineTransAPI() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_TRANSACTIONS_OFFLINE;
        }
        return LIVE + URL_IMPORT_TRANSACTIONS_OFFLINE;
    }

    public String getImportOnlineTransAPI() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_TRANSACTIONS_ONLINE;
        }
        return LIVE + URL_IMPORT_TRANSACTIONS_ONLINE;
    }

    public String getImportPreOrderAPI() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_TRANSACTIONS_PREORDER;
        }
        return LIVE + URL_IMPORT_TRANSACTIONS_PREORDER;
    }

    public String getImportReedemptionsAPI() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_TRANSACTIONS_REDEMPTION;
        }
        return LIVE + URL_IMPORT_TRANSACTIONS_REDEMPTION;
    }

    public String getMCRegistrationAPI() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_MC_REGISTRATION;
        }
        return LIVE + URL_IMPORT_MC_REGISTRATION;
    }

    public String getServiceInfoAPI() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_SERVICE;
        }
        return LIVE + URL_IMPORT_SERVICE;
    }

    public String getImportBranchesAPI() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_BRANCH;
        }
        return LIVE + URL_IMPORT_BRANCH;
    }

    public String getImportGCardAPI() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_GCARD;
        }
        return LIVE + URL_IMPORT_GCARD;
    }

    public String getRequestAvlPointsAPI() {
        if(isTestUnit){
            return LOCAL + URL_REQUEST_AVAIL_POINTS;
        }
        return LIVE + URL_REQUEST_AVAIL_POINTS;
    }

    public String getPlaceOrderAPI() {
        if(isTestUnit){
            return LOCAL + URL_PLACE_ODER;
        }
        return LIVE + URL_PLACE_ODER;
    }

    public String getCancelOrderAPI() {
        if(isTestUnit){
            return LOCAL + URL_CANCEL_ORDER;
        }
        return LIVE + URL_CANCEL_ORDER;
    }

    public String getImportEventsAPI(Boolean isPageant) {

        //todo same table to import, different api urls for different events
        if(isTestUnit){
            if (!isPageant){
                return LOCAL + URL_IMPORT_EVENTS;
            }else {
                return LOCAL + URL_IMPORT_EVENTS_PAGEANT;
            }
        }

        //todo same table to import, different api urls for different events
        if (!isPageant){
            return LIVE + URL_IMPORT_EVENTS;
        }else {
            return LIVE + URL_IMPORT_EVENTS_PAGEANT;
        }
    }

    public String getChangePasswordAPI() {
        if(isTestUnit){
            return LOCAL + URL_CHANGE_PASSWORD;
        }
        return LIVE + URL_CHANGE_PASSWORD;
    }

    public String getDevicesInfoAPI() {
        if(isTestUnit){
            return LOCAL + URL_REQUEST_DEVICES;
        }
        return LIVE + URL_REQUEST_DEVICES;
    }

    public String getClientLoginAPI() {
        return URL_CLIENT_LOGIN;
    }

    public String getSendResponseAPI() {
        return SEND_RESPONSE;
    }

    public String getSendRequestAPI() {
        return SEND_REQUEST;
    }

    public String getCheckUpdateAPI() {
        if(isTestUnit){
            return LOCAL + CHECK_UPDATE_URL;
        }
        return LIVE + CHECK_UPDATE_URL;
    }

    public String getCreateNewClientAPI(){
        if(isTestUnit){
            Log.d("SERVER API", LOCAL + REGISTER_ACCOUNT);
            return LOCAL + REGISTER_ACCOUNT;

        }
        return LIVE + REGISTER_ACCOUNT;
    }

    public String getImportAccountInfoAPI() {
        if(isTestUnit){
            return LOCAL + IMPORT_ACCOUNT_INFO;
        }
        return LIVE + IMPORT_ACCOUNT_INFO;
    }
    public String getImportClientInfoAPI() {
        if(isTestUnit){
            return LOCAL + IMPORT_CLIENT_INFO;
        }
        return LIVE + IMPORT_CLIENT_INFO;
    }
    public String getSearchClientInfo() {
        if(isTestUnit){
            return LOCAL + RETRIEVE_CLIENT_INFO;
        }
        return LIVE + RETRIEVE_CLIENT_INFO;
    }


    public String getUpdateAccountInfo() {
        if(isTestUnit){
            return LOCAL + UPDATE_ACCOUNT_INFO;
        }
        return LIVE + UPDATE_ACCOUNT_INFO;
    }

    public String getImportPaymentMethods() {
        if(isTestUnit){
            return LOCAL + IMPORT_PAYMENT_METHODS;
        }
        return LIVE + IMPORT_PAYMENT_METHODS;
    }

    public String getUpdatePaymentMethod() {
        if(isTestUnit){
            return LOCAL + UPDATE_PAYMENT_METHOD;
        }
        return LIVE + UPDATE_PAYMENT_METHOD;
    }

    public String getImportShippingInfo() {
        if(isTestUnit){
            return LOCAL + IMPORT_SHIPPING_INFO;
        }
        return LIVE + IMPORT_SHIPPING_INFO;
    }

    public String getImportTopSellingItems() {
        if(isTestUnit){
            return LOCAL + IMPORT_TOP_SELLING_ITEMS;
        }
        return LIVE + IMPORT_TOP_SELLING_ITEMS;
    }

    public String getImportProducts() {
        if(isTestUnit){
            return LOCAL + IMPORT_PROMO_ITEMS;
        }
        return LIVE + IMPORT_PROMO_ITEMS;
    }

    public String getImportSearchResults() {
        if(isTestUnit){
            return LOCAL + IMPORT_SEARCH_RESULTS;
        }
        return LIVE + IMPORT_SEARCH_RESULTS;
    }

    public String getImportProductInfo() {
        if(isTestUnit){
            return LOCAL + IMPORT_PRODUCT_INFO;
        }
        return LIVE + IMPORT_PRODUCT_INFO;
    }

    public String getRemoveCartItemAPI() {
        if(isTestUnit){
            return LOCAL + UPDATE_CART_ITEM;
        }
        return LIVE + UPDATE_CART_ITEM;
    }

    public String getImportCartItems() {
        if(isTestUnit){
            return LOCAL + IMPORT_CART_ITEMS;
        }
        return LIVE + IMPORT_CART_ITEMS;
    }

    public String getImportPurchasesAPI() {
        if(isTestUnit){
            return LOCAL + IMPORT_PURCHASES;
        }
        return LIVE + IMPORT_PURCHASES;
    }

    public String getImportRetrieveShippingAddress() {
        if(isTestUnit){
            return LOCAL + IMPORT_RETRIEVE_SHIPPING_ADDRESS;
        }
        return LIVE + IMPORT_RETRIEVE_SHIPPING_ADDRESS;
    }

    public String getUpdateShippingAddress() {
        if(isTestUnit){
            return LOCAL + UPDATE_SHIPPING_ADDRESS;
        }
        return LIVE + UPDATE_SHIPPING_ADDRESS;
    }

    public String getImportDefaultPaymentMethod() {
        if(isTestUnit){
            return LOCAL + IMPORT_DEFAULT_PAYMENT_METHOD;
        }
        return LIVE + IMPORT_DEFAULT_PAYMENT_METHOD;
    }

    public String getUpdateDefaultPaymentMethod() {
        if(isTestUnit){
            return LOCAL + UPDATE_DEFAULT_PAYMENT_METHOD;
        }
        return LIVE + UPDATE_DEFAULT_PAYMENT_METHOD;
    }

    public String getImportShippingStatus() {
        if(isTestUnit){
            return LOCAL + IMPORT_SHIPPING_STATUS;
        }
        return LIVE + IMPORT_SHIPPING_STATUS;
    }

    public String getImportOrderSummary() {
        if(isTestUnit){
            return LOCAL + IMPORT_ORDER_SUMMARY;
        }
        return LIVE + IMPORT_ORDER_SUMMARY;
    }

    public String getImportReviewsAPI() {
        if(isTestUnit){
            return LOCAL + IMPORT_REVIEWS;
        }
        return LIVE + IMPORT_REVIEWS;
    }

    public String getQuestionsAndAnswersAPI() {
        if(isTestUnit){
            return LOCAL + IMPORT_INQUIRIES;
        }
        return LIVE + IMPORT_INQUIRIES;
    }

    public String getUpdateMobileNo() {
        if(isTestUnit){
            return LOCAL + UPDATE_MOBILE_NO;
        }
        return LIVE + UPDATE_MOBILE_NO;
    }

    public String getUpdateAddressAPI() {
        if(isTestUnit){
            return LOCAL + UPDATE_ADDRESS;
        }
        return LIVE + UPDATE_ADDRESS;
    }

    public String getImportBarangayAPI(){
        if(isTestUnit){
            return LOCAL + IMPORT_BARANGAY;
        }
        return LIVE + IMPORT_BARANGAY;
    }

    public String getImportTownAPI(){
        if(isTestUnit){
            return LOCAL + IMPORT_TOWN;
        }
        return LIVE + IMPORT_TOWN;
    }

    public String getImportProvinceAPI(){
        if(isTestUnit){
            return LOCAL + IMPORT_PROVINCE;
        }
        return LIVE + IMPORT_PROVINCE;
    }

    public String getImportCountriesAPI(){
        if(isTestUnit){
            return LOCAL + IMPORT_COUNTRIES;
        }
        return LIVE + IMPORT_COUNTRIES;
    }

    public String getAddressUpdateAPI(){
        if(isTestUnit){
            return LOCAL + URL_UPDATE_ADDRESS;
        }
        return LIVE + URL_UPDATE_ADDRESS;
    }

    public String getMobileUpdateAPI(){
        if(isTestUnit){
            return LOCAL + URL_UPDATE_MOBILE;
        }
        return LIVE + URL_UPDATE_MOBILE;
    }

    public String getEmailUpdateAPI(){
        if(isTestUnit){
            return LOCAL + URL_UPDATE_EMAIL;
        }
        return LIVE + URL_UPDATE_EMAIL;
    }
    public String getVerifyEmailUpdateAPI(){
        if(isTestUnit){
            return LOCAL + VERIFY_UPDATE_EMAIL;
        }
        return LIVE + VERIFY_UPDATE_EMAIL;
    }

    public String getAddToCartAPI(){
        if(isTestUnit){
            return LOCAL + ADD_TO_CART_ITEM;
        }
        return LIVE + ADD_TO_CART_ITEM;
    }

    public String getMarketPlaceOrderAPI(){
        if(isTestUnit){
            return LOCAL + MKTPL_PLACE_ORDER;
        }
        return LIVE + MKTPL_PLACE_ORDER;
    }

    public String getOrderPaymentAPI(){
        if(isTestUnit){
            return LOCAL + MKTPL_ORDER_PAYMENT;
        }
        return LIVE + MKTPL_ORDER_PAYMENT;
    }

    public String getImportNotificationsAPI(){
        if(isTestUnit){
            return LOCAL + IMPORT_NOTIFICATIONS;
        }
        return LIVE + IMPORT_NOTIFICATIONS;
    }

    public String getAddressUpdateRequestAPI(){
        if(isTestUnit){
            return LOCAL + REQUEST_ADDRESS_UPDATE;
        }
        return LIVE + REQUEST_ADDRESS_UPDATE;
    }

    public String getMobileUpdateRequestAPI(){
        if(isTestUnit){
            return LOCAL + REQUEST_MOBILE_UPDATE;
        }
        return LIVE + REQUEST_MOBILE_UPDATE;
    }

    public String getSubmitInquiryAPI(){
        if(isTestUnit){
            return LOCAL + SUBMIT_INQUIRY;
        }
        return LIVE + SUBMIT_INQUIRY;
    }

    public String getSubmitReviewAPI(){
        if(isTestUnit){
            return LOCAL + SUBMIT_REVIEW;
        }
        return LIVE + SUBMIT_REVIEW;
    }

    public String getSendOtpAPI(){
        if(isTestUnit){
            return LOCAL + RESEND_OTP;
        }
        return LIVE + RESEND_OTP;
    }

    public String getCancelMarketplaceOrderAPI(){
        if(isTestUnit){
            return LOCAL + MKTPL_CANCEL_ORDER;
        }
        return LIVE + MKTPL_CANCEL_ORDER;
    }

    public String getDownloadBankAccountAPI(){
        if(isTestUnit){
            return LOCAL + IMPORT_BANK_ACC;
        }
        return LIVE + IMPORT_BANK_ACC;
    }

    public String getDownloadCancellationsAPI(){
        if(isTestUnit){
            return LOCAL + IMPORT_MKTPL_CANCELLATIONS;
        }
        return LIVE + IMPORT_MKTPL_CANCELLATIONS;
    }

    public String getRetrieveProfilePictureAPI(){
        if(isTestUnit){
            return LOCAL + RETRIEVE_PROFILE_PICTURE;
        }
        return LIVE + RETRIEVE_PROFILE_PICTURE;
    }

    public String getRetrieveVerifiedIDAPI(){
        if(isTestUnit){
            return LOCAL + RETRIEVE_VERIFIED_ID;
        }
        return LIVE + RETRIEVE_VERIFIED_ID;
    }

    public String getImportEmailsAPI(){
        if(isTestUnit){
            return LOCAL + IMPORT_EMAILS;
        }
        return LIVE + IMPORT_EMAILS;
    }

    public String getImportMobileNoAPI(){
        if(isTestUnit){
            return LOCAL + IMPORT_MOBILE_NO;
        }
        return LIVE + IMPORT_MOBILE_NO;
    }


    public String getSubmitSelfieVerificationAPI(){
        if(isTestUnit){
            return LOCAL + SUBMIT_SELFIE_VERIFICATION;
        }
        return LIVE + SUBMIT_SELFIE_VERIFICATION;
    }

    public String getSubmitIdVerificationAPI(){
        if(isTestUnit){
            return LOCAL + SUBMIT_ID_VERIFICATION;
        }
        return LIVE + SUBMIT_ID_VERIFICATION;
    }

    public String getImportValidIdCodeAPI(){
        if(isTestUnit){
            return LOCAL + IMPORT_VALID_ID_CODE;
        }
        return LIVE + IMPORT_VALID_ID_CODE;
    }

    public String getSubmitMeansInfo(){
        if(isTestUnit){
            return LOCAL + SUBMIT_MEANS_INFO;
        }
        return LIVE + SUBMIT_MEANS_INFO;
    }

    public String getSubmitOtherInfo(){
        if(isTestUnit){
            return LOCAL + SUBMIT_OTHER_INFO;
        }
        return LIVE + SUBMIT_OTHER_INFO;
    }

    public String getOtherApplicationInfo(){
        if(isTestUnit){
            return LOCAL + GET_OTHER_APPLICATION_INFO;
        }
        return LIVE + GET_OTHER_APPLICATION_INFO;
    }

    public String getSubmitLoanApplication(){
        if(isTestUnit){
            return LOCAL + SUBMIT_LOAN_APPLICATION;
        }
        return LIVE + SUBMIT_LOAN_APPLICATION;
    }

    public String getDownloadPriceListAPI(){
        if(isTestUnit){
            return LOCAL + IMPORT_PRICE_LIST;
        }
        return LIVE + IMPORT_PRICE_LIST;
    }

    public String getUserPanaloRewardsAPI(){
        if(isTestUnit){
            return LOCAL + GET_PANALO_REWARDS;
        }
        return LIVE + GET_PANALO_REWARDS;
    }

    public String getUpdateClientMobileAPI(){
        if(isTestUnit){
            return LOCAL + UPDATE_CLIENT_MOBILE_NO;
        }
        return LIVE + UPDATE_CLIENT_MOBILE_NO;
    }

    public String getVerifyClientMobileAPI(){
        if(isTestUnit){
            return LOCAL + VERIFY_CLIENT_MOBILE_NO;
        }
        return LIVE + VERIFY_CLIENT_MOBILE_NO;
    }

    public String getUrlDownloadRelation() {
        if(isTestUnit){
            return LOCAL + URL_DOWNLOAD_RELATION;
        }
        return LIVE + URL_DOWNLOAD_RELATION;
    }

    public String getSubmitInquiry() {
        if(isTestUnit){
            return LOCAL + URL_SUBMIT_INQUIRY;
        }
        return LIVE + URL_SUBMIT_INQUIRY;
    }

    public String getDownloadInquiries() {
        if(isTestUnit){
            return LOCAL + URL_DOWNLOAD_INQUIRIES;
        }
        return LIVE + URL_DOWNLOAD_INQUIRIES;
    }


    public String getUrlImportMcModel() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_MC_MODEL;
        }
        return LIVE + URL_IMPORT_MC_MODEL;
    }

    public String getUrlImportMcModelColor() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_MC_COLOR;
        }
        return LIVE + URL_IMPORT_MC_COLOR;
    }

    public String getUrlImportMcModelPrices() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_MC_PRICES;
        }
        return LIVE + URL_IMPORT_MC_PRICES;
    }

    public String getUrlImportMcModelPrice() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_MC_MODEL_PRICE;
        }
        return LIVE + URL_IMPORT_MC_MODEL_PRICE;
    }

    public String getUrlImportBrand() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_BRAND;
        }
        return LIVE + URL_IMPORT_BRAND;
    }

    public String getUrlImportMcCategory() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_MC_CATEGORY;
        }
        return LIVE + URL_IMPORT_MC_CATEGORY;
    }

    public String getUrlImportTermCategory() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_TERM_CATEGORY;
        }
        return LIVE + URL_IMPORT_TERM_CATEGORY;
    }


    public String getUrlImportTown() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_TOWN;
        }
        return LIVE + URL_IMPORT_TOWN;
    }

    public String getUrlImportGcardPoints() {
        if(isTestUnit){
            return LOCAL + URL_IMPORT_GCARD_POINTS;
        }
        return LIVE + URL_IMPORT_GCARD_POINTS;
    }

    public String getUrlSubmitAppVersion() {
        if(isTestUnit) {
            return LOCAL + URL_SUBMIT_APP_VERSION;
        }
        return LIVE + URL_SUBMIT_APP_VERSION;
    }
}
