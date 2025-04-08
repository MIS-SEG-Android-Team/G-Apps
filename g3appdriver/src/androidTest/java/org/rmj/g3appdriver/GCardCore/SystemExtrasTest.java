package org.rmj.g3appdriver.GCardCore;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.annotation.UiThread;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class SystemExtrasTest {

    private Context mContext;
    private iGCardSystem poSystem;
    private static boolean isSuccess = false;
    private static JSONObject loJson;

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        poSystem = new GCardSystem(mContext).getInstance(GCardSystem.CoreFunctions.EXTRAS);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test01DownloadBranchesList() throws Exception{
        poSystem.DownloadBranchesList(new GCardSystem.GCardSystemCallback() {
            @Override
            public void OnSuccess(String args) {
                try {
                    isSuccess = true;
                    loJson = new JSONObject(args);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFailed(String message) {
                isSuccess = false;
            }
        });
        assertTrue(isSuccess);
        assertNotNull(loJson);
    }

    @Test
    public void test02SaveBranchesList() throws Exception{
        poSystem.SaveBranchesList(loJson);
    }

    @Test @UiThread
    public void test03GetMobileBranchList() {
        poSystem.GetMobileBranchList().observeForever(eBranchInfos -> assertEquals(1, eBranchInfos.size()));
    }

    @Test @UiThread
    public void test04GetMotorcycleBranchList() {
        poSystem.GetMotorcycleBranchList().observeForever(eBranchInfos -> assertEquals(1, eBranchInfos.size()));
    }

    @Test
    public void test05DownloadPromotions() throws Exception{
        poSystem.DownloadPromotions(new GCardSystem.GCardSystemCallback() {
            @Override
            public void OnSuccess(String args) {
                try {
                    isSuccess = true;
                    loJson = new JSONObject(args);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFailed(String message) {
                isSuccess = false;
            }
        });
        assertTrue(isSuccess);
        assertNotNull(loJson);
    }

    @Test
    public void test06SavePromotions() throws Exception{
        poSystem.SavePromotions(loJson);
    }

    @Test @UiThread
    public void test07GetPromotions() {
        poSystem.GetPromotions().observeForever(ePromos -> assertEquals(1, ePromos.size()));
    }

    @Test
    public void test08DownloadNewsEvents() throws Exception{
        poSystem.DownloadNewsEvents(new GCardSystem.GCardSystemCallback() {
            @Override
            public void OnSuccess(String args) {
                try {
                    isSuccess = true;
                    loJson = new JSONObject(args);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFailed(String message) {
                isSuccess = false;
            }
        });
        assertTrue(isSuccess);
        assertNotNull(loJson);
    }

    @Test
    public void test09SaveNewsEvents() throws Exception{
        poSystem.SaveNewsEvents(loJson);
    }

    @Test @UiThread
    public void test10GetNewsEvents() {
        poSystem.GetNewsEvents().observeForever(eEvents -> assertEquals(1, eEvents.size()));
    }
}