package org.rmj.g3appdriver;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = ApplicationProvider.getApplicationContext();

        assertEquals("org.rmj.g3appdriver.test", appContext.getPackageName());
    }

    @Test
    public void test01JSonObjectValArray() throws Exception {
        JSONObject loJSon = new JSONObject();
        loJSon.put("json", "sample_data");
        JSONObject detail = new JSONObject();
        detail.put("alphabet", "abc");
        detail.put("numericx", "123");
        detail.put("rvsAlpha", "zyx");
        detail.put("rvsNumrc", "987");
        JSONArray laJson = new JSONArray();
        laJson.put(detail);
        loJSon.put("detail", laJson);

        JSONArray laResult = loJSon.getJSONArray("detail");
        assertNotNull(laResult);
    }


    @Test
    public void test01JSonObjectValNullArray() throws Exception {
        JSONObject loJSon = new JSONObject();
        loJSon.put("json", "sample_data");
        JSONObject detail = new JSONObject();
        detail.put("alphabet", "abc");
        detail.put("numericx", "123");
        detail.put("rvsAlpha", "zyx");
        detail.put("rvsNumrc", "987");
        JSONArray laJson = new JSONArray();
        loJSon.put("detail", laJson);

        JSONArray laResult = loJSon.getJSONArray("detail");
        assertNotNull(laResult);
    }
}
