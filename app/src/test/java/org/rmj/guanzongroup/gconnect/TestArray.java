package org.rmj.guanzongroup.gconnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class TestArray {

    @Test
    public void TestArrayString() throws JSONException {

        JSONObject loVal = new JSONObject("{\"master\":\"https:\\/\\/restgk.guanzongroup.com.ph\\/img\\/GF2025\\/campus\\/1\\/1.jpg\",\"details\":\"[\\\"https:\\/\\/restgk.guanzongroup.com.ph\\/img\\/GF2025\\/campus\\/1\\/1.jpg\\\", \\\"https:\\/\\/restgk.guanzongroup.com.ph\\/img\\/GF2025\\/campus\\/1\\/2.jpg\\\", \\\"https:\\/\\/restgk.guanzongroup.com.ph\\/img\\/GF2025\\/campus\\/1\\/3.jpg\\\", \\\"https:\\/\\/restgk.guanzongroup.com.ph\\/img\\/GF2025\\/campus\\/1\\/4.jpg\\\", \\\"https:\\/\\/restgk.guanzongroup.com.ph\\/img\\/GF2025\\/campus\\/1\\/5.jpg\\\"]\"}");

        String lsDetails = loVal.getString("details");
        JSONArray laDetails = new JSONArray(lsDetails);
    }
}
