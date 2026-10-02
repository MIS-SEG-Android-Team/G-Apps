package org.rmj.guanzongroup.gconnect;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class TestDate {

    @Test
    public void testDate() throws ParseException {

        LocalDate currentDt = LocalDate.now();
        LocalDate evntDt = LocalDateTime.parse("2025-04-30 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date loToday = dtFormat.parse(dtFormat.format(Calendar.getInstance().getTime()));
        Date loEvntFrom = dtFormat.parse(dtFormat.format(dtFormat.parse("2025-04-30 00:00:00")));

        System.out.println(loToday.before(loEvntFrom));
    }
}
