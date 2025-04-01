package org.rmj.guanzongroup.gconnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

public class TestArray {

    @Test
    public void TestArrayString() throws JSONException {

        JSONArray sArr =  new JSONArray("['http://192.165.10.65/candidatesimage/dreamboy/akio/1.jpg']");

        System.out.println(sArr.get(0));
    }
}
