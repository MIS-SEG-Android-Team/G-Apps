package org.rmj.guanzongroup.gconnect;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class BingoRandomNumbers {

    @Test
    public void GenerateNumbers(){

        try {

            JSONArray loArray = new JSONArray();

            while (loArray.length() < 5){

                JSONObject loNumbers = new JSONObject();

                for (int ctr = 0; loNumbers.length() < 5; ctr++){

                    Random random = new Random();
                    loNumbers.put(String.valueOf(ctr), random.nextInt(9));

                }

                loArray.put(loNumbers);

            }

            for (int i = 0; i < loArray.length(); i++) {

                System.out.println(loArray.getJSONObject(i));

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
