package org.rmj.guanzongroup.gconnect;

import org.junit.Test;

import java.time.LocalDate;

public class TestDate {

    @Test
    public void testDate() {

        LocalDate localDateNow = LocalDate.now();
        LocalDate eventDateFrom = LocalDate.parse("2025-03-29");

        System.out.println(
                localDateNow.equals(eventDateFrom)
        );

        System.out.println(
                localDateNow.isBefore(eventDateFrom)
        );

        System.out.println(
                localDateNow.isAfter(eventDateFrom)
        );

    }
}
