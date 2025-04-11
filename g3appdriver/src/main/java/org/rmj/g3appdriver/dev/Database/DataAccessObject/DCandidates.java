package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;

import java.util.List;

@Dao
public interface DCandidates {

    @Insert
    void insert(ECandidates candidates);

    @Query("DELETE FROM Guanzon_Candidates")
    void deleteAll();

    @Query("SELECT * FROM Guanzon_Candidates WHERE sEvntIDxx = :categoryID ORDER BY sGroupIDx ASC")
    LiveData<List<ECandidates>> GetAllCandidates(String categoryID);

    @Query("SELECT COUNT(*) as total, dVoted as dTimeStmp FROM Guanzon_Candidates " +
            "WHERE sEvntIDxx = :categoryID AND nVotes > 0 AND sUserIDxx = :userIDxx " +
            "ORDER BY dVoted DESC LIMIT 1")
    LiveData<LatestVote> ObserveVoteCounts(String categoryID, String userIDxx);

    @Query("SELECT COUNT(*) as total, dVoted as dTimeStmp FROM Guanzon_Candidates " +
            "WHERE sEvntIDxx = :categoryID AND nVotes > 0 AND sUserIDxx = :userIDxx")
    LatestVote GetVoteCount(String categoryID, String userIDxx);

    @Query("UPDATE Guanzon_Candidates SET nVotes = nVotes + 1, dVoted = :dTimeStmp " +
            "WHERE sGroupIDx = :pageantID AND sEvntIDxx = :categoryID")
    void SubmitVote(String dTimeStmp, String pageantID, String categoryID);

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
