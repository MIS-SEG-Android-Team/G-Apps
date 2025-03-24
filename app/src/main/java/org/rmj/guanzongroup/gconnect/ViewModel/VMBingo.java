package org.rmj.guanzongroup.gconnect.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.rmj.g3appdriver.dev.Database.Entities.EBingoCard;
import org.rmj.g3appdriver.etc.Bingo;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMBingo extends AndroidViewModel {

    private final Bingo bingo;
    private String message;

    public VMBingo(@NonNull Application application) {
        super(application);

        this.bingo = new Bingo(application);

    }

    public LiveData<List<EBingoCard>> GetCardNumbers(){
        return bingo.GetCardNumbers();
    }

    public void SaveCardNumbers(OnSaveCardNumbers callback){

        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onGenerate();
            }

            @Override
            public Object DoInBackground(Object args) {

                String cardIDxx = bingo.GenerateID();
                JSONArray cardNumbers = bingo.GenerateNumbers();

                if (cardIDxx.isEmpty()){
                    message = "Error generating card ID";
                    return false;
                }

                if (cardNumbers.length() < 0){
                    message= "Error generating card numbers";
                    return false;
                }

                bingo.SaveCardNumber(cardIDxx, cardNumbers);

                return true;
            }

            @Override
            public void OnPostExecute(Object object) {

                Boolean result = (Boolean) object;

                if (result){
                    callback.onSuccess();
                }else {
                    callback.onError(message);
                }
            }
        });

    }

    public interface OnSaveCardNumbers{
        void onGenerate();
        void onSuccess();
        void onError(String message);
    }
}
