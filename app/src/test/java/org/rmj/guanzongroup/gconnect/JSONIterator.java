package org.rmj.guanzongroup.gconnect;

import org.json.JSONObject;
import org.junit.Test;
import org.w3c.dom.Node;

public class JSONIterator {

    @Test
    public void TestJSon(){
        try {

            JSONObject obj = new JSONObject();
            obj.put("B", "1");
            obj.put("I", "2");
            obj.put("N", "3");
            obj.put("G", "4");
            obj.put("O", "5");

            for (int i = 0; i < obj.names().length(); i++){
                System.out.println(obj.names().get(i));
                System.out.println(obj.getString(obj.names().getString(i)));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
