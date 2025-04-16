package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import org.rmj.g3appdriver.dev.Database.Entities.EVoteLogs;

@Dao
public interface DVoteLogs {

    @Upsert
    void save(EVoteLogs foVal);

    @Query("DELETE FROM Vote_History")
    void deleteAll();

    @Query("SELECT nNoVotesx total, dVoted dTimeStmp FROM Vote_History " +
            "WHERE sSubEventIDxx = :categoryID AND sUserIDxx = :sUserIDxx")
    LiveData<LatestVote> ObserveVoteCounts(String categoryID, String sUserIDxx);

    @Query("SELECT nNoVotesx total, dVoted dTimeStmp FROM Vote_History " +
            "WHERE sSubEventIDxx = :categoryID AND sUserIDxx = :sUserIDxx")
    LatestVote GetVoteCounts(String categoryID, String sUserIDxx);

    @Query("SELECT nNoVotesx FROM Vote_History " +
            "WHERE sGroupIDx = :sGroupIDx " +
            "AND sSubEventIDxx = :categoryID " +
            "AND sUserIDxx = :sUserIDxx")
    int GetCandidateVotes(String sGroupIDx, String categoryID, String sUserIDxx);

    class LatestVote{
        int total;
        String dTimeStmp;

        public LatestVote(int total, String dTimeStmp) {
            this.total = total;
            this.dTimeStmp = dTimeStmp;
        }

        public int getTotal() {
            return total;
        }

        public String getdTimeStmp() {
            return dTimeStmp;
        }

    }
}
