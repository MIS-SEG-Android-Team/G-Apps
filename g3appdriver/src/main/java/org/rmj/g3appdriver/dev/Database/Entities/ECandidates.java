package org.rmj.g3appdriver.dev.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "Guanzon_Candidates", primaryKeys = {"categoryID", "pageantID"})
public class ECandidates {

    @NonNull
    @ColumnInfo(name = "categoryID")
    private String categoryID;

    @NonNull
    @ColumnInfo(name = "pageantID")
    private String pageantID;

    @ColumnInfo(name = "userID")
    private String userID;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "school")
    private String school;

    @ColumnInfo(name = "urlImgs")
    private String urlImgs;

    @ColumnInfo(name = "votes")
    private String votes;

    @ColumnInfo(name = "dVoted")
    public String dVoted;

    @NonNull
    public String getCategoryID() {
        return categoryID;
    }
    public void setCategoryID(@NonNull String categoryID) {
        this.categoryID = categoryID;
    }

    @NonNull
    public String getPageantID() {
        return pageantID;
    }
    public void setPageantID(@NonNull String pageantID) {
        this.pageantID = pageantID;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }
    public void setSchool(String school) {
        this.school = school;
    }

    public String getUrlImgs() {
        return urlImgs;
    }
    public void setUrlImgs(String urlImgs) {
        this.urlImgs = urlImgs;
    }

    public String getVotes() {
        return votes;
    }
    public void setVotes(String votes) {
        this.votes = votes;
    }


    public String getdTimeStmp() {
        return dVoted;
    }

    public void setdTimeStmp(String dTimeStmp) {
        this.dVoted = dTimeStmp;
    }
}
