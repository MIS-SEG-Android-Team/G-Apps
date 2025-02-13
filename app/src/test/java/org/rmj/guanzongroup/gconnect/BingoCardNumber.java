package org.rmj.guanzongroup.gconnect;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BingoCardNumber {

    @Test
    public void GenerateID(){

        String dateToday = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
        System.out.println("BNG" + dateToday);
    }
}
