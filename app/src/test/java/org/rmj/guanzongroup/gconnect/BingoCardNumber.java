package org.rmj.guanzongroup.gconnect;

import org.json.JSONArray;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BingoCardNumber {

    @Test
    public void GenerateID(){

//        String dateToday = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
//        System.out.println("BNG" + dateToday);

        JSONArray loIEMI = new JSONArray();
        loIEMI.put("35628794");
        loIEMI.put("35628794");
        loIEMI.put("35628794");

        System.out.println(loIEMI.toString());


    }
}
