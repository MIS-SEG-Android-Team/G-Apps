package org.rmj.guanzongroup.ganado;

import org.junit.Test;

import java.util.regex.Pattern;

public class TestPattern {

    @Test
    public void PatternTest(){

        String chars = "09275408234";
        Boolean match = Pattern.matches(String.format("[0-9]{%s}?", chars.length()), chars);

        System.out.println(match);
    }
}
