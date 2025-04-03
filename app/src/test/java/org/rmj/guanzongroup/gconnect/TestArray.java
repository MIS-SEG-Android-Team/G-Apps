package org.rmj.guanzongroup.gconnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class TestArray {

    @Test
    public void TestArrayString() throws JSONException {

        JSONObject loObj =  new JSONObject("{'primary': 'http://192.165.10.65/candidatesimage/dreamboy/akio/1.jpg'" +
                ", 'detail': ['http://192.165.10.65/candidatesimage/dreamboy/akio/1.jpg'] }");

        System.out.println(loObj.get("primary"));

        JSONArray loArray = loObj.getJSONArray("detail");

        System.out.println(loArray.get(0));
    }
}
