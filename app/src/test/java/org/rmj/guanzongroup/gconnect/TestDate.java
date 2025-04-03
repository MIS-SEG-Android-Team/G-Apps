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

        LocalDateTime loLastVote = LocalDateTime.parse("2025-04-02 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDate loToday = LocalDateTime.now().toLocalDate();

        System.out.println(loLastVote.toLocalDate().isEqual(loToday));

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date dtLastVote = dtFormat.parse(dtFormat.format(dtFormat.parse("2025-04-03 00:00:00")));
        Date dtToday = dtFormat.parse(dtFormat.format(Calendar.getInstance().getTime()));

        System.out.println(dtLastVote.equals(dtToday));

    }
}
