package org.rmj.guanzongroup.digitalgcard;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Random;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void GenerateRandom() {
        StringBuilder sBuilder = new StringBuilder();

        do {
            int randomNumber = new Random().nextInt(9);
            sBuilder.append(randomNumber);
        }while (sBuilder.toString().length() < 6);

        System.out.println(sBuilder);
    }
}