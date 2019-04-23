package de.lucianojung.random_generator;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RandomVariable implements Serializable{

//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "vid")
    private long id;
    private long chooserID;
    private String value; //make it object for different values
    private int weighting;

    public RandomVariable(long chooserID, String value, int weighting) {
        this.id = id;
        this.chooserID = chooserID;
        this.value = value;
        this.weighting = weighting;
    }

    //getter and setter//

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getWeighting() {
        return weighting;
    }

    public void setWeighting(int weighting) {
        this.weighting = weighting;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChooserID() {
        return chooserID;
    }

    public void setChooserID(long chooserID) {
        this.chooserID = chooserID;
    }
}
