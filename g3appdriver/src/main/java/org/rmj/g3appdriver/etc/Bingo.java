package org.rmj.g3appdriver.etc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DBingoCard;
import org.rmj.g3appdriver.dev.Database.Entities.EBingoCard;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class Bingo {

    private Context context;
    private DBingoCard dbBingoCard;

    public Bingo(Context context){

        this.dbBingoCard = GGC_GuanzonAppDB.getInstance(context).BingoCardDao();
    }

    public JSONArray GenerateNumbers(){

        try {

            //TODO: INITIATE JSON ARRAY OBJECT
            JSONArray loArray = new JSONArray();

            //TODO: GENERATE 5 RANDOM LIST OF NUMBERS
            while (loArray.length() < 5){

                //TODO: INITIATE JSON OBJECT
                JSONObject loNumbers = new JSONObject();

                //TODO: GENERATE 5 RANDOM NUMBERS, ADD TO LIST
                for (int ctr = 0; loNumbers.length() < 5; ctr++){

                    Random random = new Random();

                    switch (ctr){

                        case 0:
                            loNumbers.put(String.valueOf(ctr), random.nextInt(15 + 1));
                            break;

                        case 1:
                            loNumbers.put(String.valueOf(ctr), random.nextInt(30 + 1) + 15);
                            break;

                        case 2:
                            loNumbers.put(String.valueOf(ctr), random.nextInt(45 + 1) + 30);
                            break;

                        case 3:
                            loNumbers.put(String.valueOf(ctr), random.nextInt(60 + 1) + 45);
                            break;

                        case 4:
                            loNumbers.put(String.valueOf(ctr), random.nextInt(75 + 1) + 60);
                            break;
                    }

                }

                //TODO: ADD LIST TO ARRAY
                loArray.put(loNumbers);

            }

            return loArray;

        }catch (Exception e){

            e.printStackTrace();
            return new JSONArray();
        }

    }

    @SuppressLint("NewApi")
    public String GenerateID(){

        //TODO: GET TODAY'S DATE
        String dateToday = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());

        //TODO: GET CARD COUNT
        int countCards = dbBingoCard.count();

        //TODO: RETURN CONCATENATED STRING
        return "BNGO" + dateToday + countCards;
    }

    public void SaveCardNumber(String cardID, JSONArray cardNumbers){

        try {

            for (int i = 0; i < cardNumbers.length(); i++) {

                EBingoCard bingoCard = new EBingoCard();
                bingoCard.setCardIDxx(cardID);
                bingoCard.setColList(cardNumbers.getJSONObject(i).toString());
                bingoCard.setRowIndex(String.valueOf(i));

                dbBingoCard.insert(bingoCard);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public LiveData<List<EBingoCard>> GetCardNumbers(){
        return dbBingoCard.getCard();
    }

}
