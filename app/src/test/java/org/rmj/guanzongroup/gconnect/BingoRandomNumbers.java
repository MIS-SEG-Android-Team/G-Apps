package org.rmj.guanzongroup.gconnect;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

public class BingoRandomNumbers {

    @Test
    public void GenerateNumbers(){

        ArrayList<Integer> bingoNumbers = new ArrayList<Integer>();

        while (bingoNumbers.size() < 20){

            Random random = new Random();

            bingoNumbers.add(random.nextInt(9));

        }

        System.out.println(bingoNumbers);
    }
}
