package org.rmj.g3appdriver.lib.FiestaEventModels;

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

                //TODO: LOOP UNTIL 5 NUMBERS ARE GENERATED
                while (loNumbers.length() < 5){

                    Random random = new Random();

                    Log.d("BINGO", String.valueOf(loNumbers.length()));

                    //TODO: GENERATE RANDOM NUMBER, WITHIN A RANGE. BASED ON LENGTH
                    switch (loNumbers.length()){

                        //TODO: O FOR LETTER 'B' IN BINGO, RANGING TO 0 - 15
                        case 0:

                            //TODO: IF JSON OBJECT LIST IS NOT EMPTY, CHECK IF NUMBER EXISTS IN THIS SAME INDEX ELSE ADD
                            if (loArray.length() > 0){

                                int randomNum = random.nextInt(15 + 1);

                                if (!CheckNumberExist(loArray, "B", String.valueOf(randomNum))){
                                    loNumbers.put("B", String.valueOf(randomNum));
                                }

                            }else {
                                loNumbers.put("B", String.valueOf(random.nextInt(15 + 1)));
                            }

                            break;

                        //TODO: 1 FOR LETTER 'I' IN BINGO, RANGING TO 16 - 30
                        case 1:

                            //TODO: IF JSON OBJECT LIST IS NOT EMPTY, CHECK IF NUMBER EXISTS IN THIS SAME INDEX ELSE ADD
                            if (loArray.length() > 0){

                                int randomNum = random.nextInt(30 - 15 + 1) + 15;

                                if (!CheckNumberExist(loArray, "I", String.valueOf(randomNum))){
                                    loNumbers.put("I", String.valueOf(randomNum));
                                }

                            }else {
                                loNumbers.put("I", String.valueOf(random.nextInt(30 - 15 + 1) + 15));
                            }

                            break;

                        //TODO: 2 FOR LETTER 'N' IN BINGO, RANGING TO 31 - 45
                        case 2:

                            //TODO: IF JSON OBJECT LIST IS NOT EMPTY, CHECK IF NUMBER EXISTS IN THIS SAME INDEX ELSE ADD
                            if (loArray.length() > 0){

                                int randomNum = random.nextInt(45 - 30 + 1) + 30;

                                if (!CheckNumberExist(loArray, "N", String.valueOf(randomNum))){
                                    loNumbers.put("N", String.valueOf(randomNum));
                                }

                            }else {
                                loNumbers.put("N", String.valueOf(random.nextInt(45 - 30 + 1) + 30));
                            }

                            break;

                        //TODO: 3 FOR LETTER 'G' IN BINGO, RANGING TO 46 - 60
                        case 3:

                            //TODO: IF JSON OBJECT LIST IS NOT EMPTY, CHECK IF NUMBER EXISTS IN THIS SAME INDEX ELSE ADD
                            if (loArray.length() > 0){

                                int randomNum = random.nextInt(60 - 45 + 1) + 45;

                                if (!CheckNumberExist(loArray, "G", String.valueOf(randomNum))){
                                    loNumbers.put("G", String.valueOf(randomNum));
                                }

                            }else {
                                loNumbers.put("G", String.valueOf(random.nextInt(60 - 45 + 1) + 45));
                            }

                            break;

                        //TODO: 4 FOR LETTER 'O' IN BINGO, RANGING TO 61 - 75
                        case 4:

                            //TODO: IF JSON OBJECT LIST IS NOT EMPTY, CHECK IF NUMBER EXISTS IN THIS SAME INDEX ELSE ADD
                            if (loArray.length() > 0){

                                int randomNum = random.nextInt(75 - 60 + 1) + 60;

                                if (!CheckNumberExist(loArray, "O", String.valueOf(randomNum))){
                                    loNumbers.put("O", String.valueOf(randomNum));
                                }

                            }else {
                                loNumbers.put("O", String.valueOf(random.nextInt(75 - 60 + 1) + 60));
                            }

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

    public Boolean CheckNumberExist(JSONArray loArr, String id, String number){

        boolean result = false;

        //TODO: SCAN PER LIST
        for (int i = 0; i < loArr.length(); i++){

            try {

                //TODO: SCAN PER OBJECT IN LIST, CHECK IF CURRENT ID EXIST IN OBJECT
                if (loArr.getJSONObject(i).has(id)){

                    //TODO: GET OBJECT VALUE BY ID, CHECK IF ALREADY EXIST ON OBJECT LIST
                    if (loArr.getJSONObject(i).getString(id).equals(number)){
                        result = true;
                        break;
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
                result = true;
                break;
            }
        }

        return result;

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
