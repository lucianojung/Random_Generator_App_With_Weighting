package de.lucianojung.random_generator;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@SuppressWarnings("serial")
@Entity(tableName = "RandomVariable")
public class RandomVariable implements Serializable{

    //    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "vid")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "vid")
    private long vid;
    @ForeignKey(entity = RandomGenerator.class, parentColumns = "gid",
            childColumns = "gid", onDelete = CASCADE)
    @ColumnInfo(name = "gid")
    private long gid;
    @ColumnInfo(name = "value")
    private String value; //make it object for different values
    @ColumnInfo(name = "weighting")
    private int weighting;

    public RandomVariable(long vid, long gid, String value, int weighting) {
        this.vid = vid;
        this.gid = gid;
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

    public long getVid() {
        return vid;
    }

    public void setVid(long vid) {
        this.vid = vid;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }
}
