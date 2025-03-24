package org.rmj.guanzongroup.digitalgcard.ViewModel;

import static org.rmj.guanzongroup.digitalgcard.TestConstants.APPLICATION;
import static org.rmj.guanzongroup.digitalgcard.TestConstants.CART_ITEM;
import static org.rmj.guanzongroup.digitalgcard.TestConstants.FAKE_DATE;
import static org.rmj.guanzongroup.digitalgcard.TestConstants.GCARD_CREDENTIALS;

import android.graphics.Bitmap;
import android.os.Build;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1}, manifest = Config.NONE)
public class VMGCardSystemTest extends TestCase {
    private iGCardSystem poGcard;
    private VMGCardSystem mViewModel;
    private boolean isSuccess = false;

    @Before
    public void setUp() {
        mViewModel = new VMGCardSystem(APPLICATION);
        mViewModel.setmContext(GCardSystem.CoreFunctions.GCARD);
    }

    @After
    public void tearDown() {
        mViewModel = null;
    }

    @Test
    public void testGetGCardList() {
        mViewModel.GetGCardList().observeForever(eGcardApps -> assertNotNull(eGcardApps));
    }

    @Test
    public void testGetRedeemablesList() {
//        mViewModel.GetRedeemablesList().observeForever(eRedeems -> assertNotNull(eRedeems));
    }

    @Test
    public void testGetGcardTransactions() {
        mViewModel.GetGcardTransactions().observeForever(eLedger -> assertNotNull(eLedger));
    }

    @Test
    public void testGetPointsEntryTransactions() {
        mViewModel.GetPointsEntryTransactions().observeForever(eLedger -> assertNotNull(eLedger));
    }

    @Test
    public void testGetRedemptionTransactions() {
        mViewModel.GetRedemptionTransactions().observeForever(eRedeems -> assertNotNull(eRedeems));
    }

    @Test
    public void testAddGcard() {
        mViewModel.addGcard(GCARD_CREDENTIALS, new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onSuccess(String fsMessage) {
                isSuccess = true;
                assertTrue(isSuccess);
            }

            @Override
            public void onFailed(String fsMessage) {
                isSuccess = false;
                assertTrue(isSuccess);
            }

            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }

    @Test
    public void testAddGCardQrCode() {
        mViewModel.addGCardQrCode(new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onSuccess(String fsMessage) {
                isSuccess = true;
                assertTrue(isSuccess);
            }

            @Override
            public void onFailed(String fsMessage) {
                isSuccess = false;
                assertTrue(isSuccess);
            }

            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }

    @Test
    public void testDownloadGcardNumbers() {
        mViewModel.downloadGcardNumbers(new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onSuccess(String fsMessage) {
                isSuccess = true;
                assertTrue(isSuccess);
            }

            @Override
            public void onFailed(String fsMessage) {
                isSuccess = false;
                assertTrue(isSuccess);
            }

            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }

    @Test
    public void testActivateGcard() {
        mViewModel.activateGcard("09216365216", new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onSuccess(String fsMessage) {
                isSuccess = true;
                assertTrue(isSuccess);
            }

            @Override
            public void onFailed(String fsMessage) {
                isSuccess = false;
                assertTrue(isSuccess);
            }

            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }

    @Test
    public void testGenerateGCardQrCode() {
        mViewModel.generateGCardQrCode(new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onSuccess(String fsMessage) {
                isSuccess = true;
                assertTrue(isSuccess);
            }

            @Override
            public void onFailed(String fsMessage) {
                isSuccess = false;
                assertTrue(isSuccess);
            }

            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }

    @Test
    public void TestDownloadRedeemables() {
        mViewModel.downloadRedeemables(new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onSuccess(String fsMessage) {
                isSuccess = true;
                assertTrue(isSuccess);
            }

            @Override
            public void onFailed(String fsMessage) {
                isSuccess = false;
                assertTrue(isSuccess);
            }

            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }

    @Test
    public void testAddToCart() {
        mViewModel.addToCart(CART_ITEM, new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onSuccess(String fsMessage) {
                isSuccess = true;
                assertTrue(isSuccess);
            }

            @Override
            public void onFailed(String fsMessage) {
                isSuccess = false;
                assertTrue(isSuccess);
            }

            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }

    @Test
    public void testUpdateCartItem() {
        mViewModel.UpdateCartItem(CART_ITEM);
    }

    @Test
    public void testPlaceOrder() {
//        mViewModel.PlaceOrder(GCARD_CART_ITEMS, new VMGCardSystem.GcardTransactionCallback() {
//            @Override
//            public void onLoad() {
//
//            }
//
//            @Override
//            public void onSuccess(String fsMessage) {
//                isSuccess = true;
//                assertTrue(isSuccess);
//            }
//
//            @Override
//            public void onFailed(String fsMessage) {
//                isSuccess = false;
//                assertTrue(isSuccess);
//            }
//
//            @Override
//            public void onQrGenerate(Bitmap foBitmap) {
//
//            }
//        });
    }

    @Test
    public void testGenerateGCardOrderQrCode() {
        mViewModel.generateGCardOrderQrCode(new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onSuccess(String fsMessage) {
                isSuccess = true;
                assertTrue(isSuccess);
            }

            @Override
            public void onFailed(String fsMessage) {
                isSuccess = false;
                assertTrue(isSuccess);
            }

            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }

    @Test
    public void testDownloadTransactions() {
        mViewModel.downloadTransactions(new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onSuccess(String fsMessage) {
                isSuccess = true;
                assertTrue(isSuccess);
            }

            @Override
            public void onFailed(String fsMessage) {
                isSuccess = false;
                assertTrue(isSuccess);
            }

            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }

    @Test
    public void testSaveTransactions() {
        JSONObject loJson = new JSONObject();
        mViewModel.saveTransactions(loJson, new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onSuccess(String fsMessage) {
                isSuccess = true;
                assertTrue(isSuccess);
            }

            @Override
            public void onFailed(String fsMessage) {
                isSuccess = false;
                assertTrue(isSuccess);
            }

            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }

    @Test
    public void testDownloadMCServiceInfo() {
        mViewModel.downloadMCServiceInfo(new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onSuccess(String fsMessage) {
                isSuccess = true;
                assertTrue(isSuccess);
            }

            @Override
            public void onFailed(String fsMessage) {
                isSuccess = false;
                assertTrue(isSuccess);
            }

            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }

    @Test
    public void testDownloadRegistrationInfo() {
        mViewModel.downloadRegistrationInfo(new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onSuccess(String fsMessage) {
                isSuccess = true;
                assertTrue(isSuccess);
            }

            @Override
            public void onFailed(String fsMessage) {
                isSuccess = false;
                assertTrue(isSuccess);
            }

            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }

    @Test
    public void testScheduleNextServiceDate() {
        mViewModel.scheduleNextServiceDate(FAKE_DATE, new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onSuccess(String fsMessage) {
                isSuccess = true;
                assertTrue(isSuccess);
            }

            @Override
            public void onFailed(String fsMessage) {
                isSuccess = false;
                assertTrue(isSuccess);
            }

            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }


}
